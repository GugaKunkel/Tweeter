package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOUtils.enhancedClient;
import static edu.byu.cs.tweeter.server.dao.DAOUtils.isNonEmptyString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FollowBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
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

public class FollowsDAO implements FollowInterface {
    private static final String TableName = "Follows";
    public static final String IndexName = "follows-index";
    private static final String FollowerAliasAttr = "follower_alias";
    private static final String FolloweeAliasAttr = "followee_alias";
    private static final DynamoDbTable<FollowBean> table =
            enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
    private static final DynamoDbIndex<FollowBean> index =
            enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);

    public Pair<List<String>, Boolean> getFollowees(String targetUserAlias, String lastUserAlias, int limit) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FolloweeAliasAttr, AttributeValue.builder().s(lastUserAlias).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();
        DataPage<FollowBean> result = new DataPage<>();

        PageIterable<FollowBean> pages = table.query(queryRequest);
        pages.stream().limit(1).forEach((Page<FollowBean> page) -> {
            result.setHasMorePages(page.lastEvaluatedKey() != null);
            page.items().forEach(item -> result.getValues().add(item));
        });

        List<String> userAliases = new ArrayList<>();
        for (FollowBean value : result.getValues()) {
            userAliases.add(value.getFollowee_alias());
        }

        return new Pair<> (userAliases, result.hasMorePages());
    }

    public Pair<List<String>, Boolean> getFollowers(String targetUserAlias, String lastUserAlias, int limit) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAliasAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerAliasAttr, AttributeValue.builder().s(lastUserAlias).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();
        DataPage<FollowBean> result = new DataPage<>();

        SdkIterable<Page<FollowBean>> sdkIterable = index.query(queryRequest);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream().limit(1).forEach((Page<FollowBean> page) -> {
            result.setHasMorePages(page.lastEvaluatedKey() != null);
            page.items().forEach(item -> result.getValues().add(item));
        });

        List<String> userAliases = new ArrayList<>();
        for (FollowBean value : result.getValues()) {
            userAliases.add(value.getFollower_alias());
        }

        return new Pair<> (userAliases, result.hasMorePages());
    }

    public List<String> getAllFollowers(String targetUserAlias) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest queryRequest = requestBuilder.build();
        DataPage<FollowBean> result = new DataPage<>();

        SdkIterable<Page<FollowBean>> sdkIterable = index.query(queryRequest);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream().limit(1).forEach((Page<FollowBean> page) -> {
            result.setHasMorePages(page.lastEvaluatedKey() != null);
            page.items().forEach(visit -> result.getValues().add(visit));
        });

        List<String> userAliases = new ArrayList<>();
        for (FollowBean value : result.getValues()) {
            userAliases.add(value.getFollower_alias());
        }

        return userAliases;
    }

    public boolean getFollowStatus(User follower, List<User> allFollowers) {
        return allFollowers.contains(follower);
    }

    public void followUser(String followee, String follower) {
        FollowBean followBean = new FollowBean();
        followBean.setFollower_alias(follower);
        followBean.setFollowee_alias(followee);
        table.putItem(followBean);
    }

    public void unfollowUser(String followee, String follower) {
        Key followsKey = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();
        FollowBean followBean = table.getItem(followsKey);
        if(followBean != null) {
            table.deleteItem(followsKey);
        }
    }

    public void addFollowersBatch(List<String> followers, String followTarget) {
        List<FollowBean> batchToWrite = new ArrayList<>();
        for (String follower : followers) {
            FollowBean bean = new FollowBean();
            bean.setFollower_alias(follower);
            bean.setFollowee_alias(followTarget);
            batchToWrite.add(bean);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfFollowDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfFollowDTOs(batchToWrite);
        }
    }
    private void writeChunkOfFollowDTOs(List<FollowBean> followDTOs) {
        if(followDTOs.size() > 25)
            throw new RuntimeException("Too many follow relationships to write");

        WriteBatch.Builder<FollowBean> writeBuilder = WriteBatch.builder(FollowBean.class).mappedTableResource(table);
        for (FollowBean item : followDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFollowDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
