package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOUtils.enhancedClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.bean.StoryBean;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class StoryDAO implements StoryInterface {
    private static final String TableName = "Story";
    private static final DynamoDbTable<StoryBean> table =
            enhancedClient.table(TableName, TableSchema.fromBean(StoryBean.class));
    private static final String UserAliasAttr = "user_alias";
    private static final String TimestampAttr = "timestamp";

    public Pair<List<Status>, Boolean> getStory(String targetUserAlias, int limit, Status lastStatus) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(limit);

        if(lastStatus != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().n(lastStatus.getTimestamp().toString()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();
        DataPage<StoryBean> result = new DataPage<>();

        PageIterable<StoryBean> pages = table.query(queryRequest);
        pages.stream().limit(1).forEach((Page<StoryBean> page) -> {
            result.setHasMorePages(page.lastEvaluatedKey() != null);
            page.items().forEach(item -> result.getValues().add(item));
        });

        List<Status> story = new ArrayList<>();
        for (StoryBean value : result.getValues()) {
            Status status = new Status(value.getPost(), null, value.getTimestamp(),
                    value.getUrls(), value.getMentions());
            story.add(status);
        }

        return new Pair<>(story, result.hasMorePages());
    }

    public void postStatus(String userAlias, String post, List<String> mentions,
                           List<String> urls, long timestamp) {
        StoryBean storyBean = new StoryBean();
        storyBean.setUser_alias(userAlias);
        storyBean.setPost(post);
        storyBean.setMentions(mentions);
        storyBean.setUrls(urls);
        storyBean.setTimestamp(timestamp);
        table.putItem(storyBean);
    }
}
