package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOUtils.enhancedClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FeedBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class FeedDAO implements FeedInterface {
    private static final String TableName = "Feed";
    private static final String ReceiverAliasAttr = "receiver_alias";
    private static final String TimestampAttr = "timestamp";
    private static final DynamoDbTable<FeedBean> table =
            enhancedClient.table(TableName, TableSchema.fromBean(FeedBean.class));

    public Pair<List<Pair<Status, String>>, Boolean> getFeed(String targetUserAlias, int limit, Status lastStatus) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(limit);

        if(lastStatus != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(ReceiverAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().n(lastStatus.getTimestamp().toString()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();
        DataPage<FeedBean> result = new DataPage<>();

        PageIterable<FeedBean> pages = table.query(queryRequest);
        pages.stream().limit(1).forEach((Page<FeedBean> page) -> {
            result.setHasMorePages(page.lastEvaluatedKey() != null);
            page.items().forEach(item -> result.getValues().add(item));
        });

        List<Pair<Status, String>> statusInfo = new ArrayList<>();
        for (FeedBean value : result.getValues()) {
            Status status = new Status(value.getPost(),
                    null, value.getTimestamp(), value.getUrls(), value.getMentions());
            statusInfo.add(new Pair<>(status, value.getUser_alias()));
        }

        return new Pair<>(statusInfo, result.hasMorePages());
    }

    public void postStatus(String userAlias, String post, List<String> mentions,
                                         List<String> urls, long timestamp, List<User> followers) {
        for (User follower: followers) {
            FeedBean feedBean = new FeedBean();
            feedBean.setReceiver_alias(follower.getAlias());
            feedBean.setUser_alias(userAlias);
            feedBean.setPost(post);
            feedBean.setMentions(mentions);
            feedBean.setUrls(urls);
            feedBean.setTimestamp(timestamp);
            table.putItem(feedBean);
        }
    }

    public void addFeedBatch(List<User> followers, String user, Status post) {
        List<FeedBean> batchToWrite = new ArrayList<>();
        for (User follower : followers) {
            FeedBean bean = new FeedBean();
            bean.setReceiver_alias(follower.getAlias());
            bean.setUser_alias(user);
            bean.setMentions(post.getMentions());
            bean.setUrls(post.getUrls());
            bean.setTimestamp(post.getTimestamp());
            bean.setPost(post.getPost());
            batchToWrite.add(bean);
            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfFeedDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfFeedDTOs(batchToWrite);
        }
    }

    private void writeChunkOfFeedDTOs(List<FeedBean> feedDTOs) {
        if(feedDTOs.size() > 25)
            throw new RuntimeException("Too many statuses to write");

        WriteBatch.Builder<FeedBean> writeBuilder = WriteBatch.builder(FeedBean.class).mappedTableResource(table);
        for (FeedBean item : feedDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFeedDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
