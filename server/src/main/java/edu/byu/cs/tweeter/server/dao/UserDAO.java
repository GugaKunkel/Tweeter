package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOUtils.enhancedClient;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class UserDAO implements UserInterface {
    private static final String TableName = "User";
    private static final DynamoDbTable<UserBean> table =
            enhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));

    public User getUser(String targetUserAlias) {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();
        UserBean userBean = table.getItem(key);

        if (userBean == null) {
            throw new RuntimeException("User Not Found In Database");
        }
        return new User(userBean.getFirst_name(), userBean.getLast_name(),
                userBean.getAlias(), userBean.getImageUrl());
    }

    public void followUser(String followee, String follower){

        Key followeeKey = Key.builder()
                .partitionValue(followee)
                .build();
        // load it if it exists
        UserBean followeeBean = table.getItem(followeeKey);

        Key followerKey = Key.builder()
                .partitionValue(follower)
                .build();
        // load it if it exists
        UserBean followerBean = table.getItem(followerKey);

        if(followeeBean != null && followerBean != null) {
            followeeBean.setFollowerCount(followeeBean.getFollowerCount() + 1);
            followerBean.setFolloweeCount(followerBean.getFolloweeCount() + 1);
            table.updateItem(followeeBean);
            table.updateItem(followerBean);
        }
        else{
            throw new RuntimeException("Users Not Found In Database");
        }
    }

    public void unfollowUser(String followee, String follower) {
        Key followeeKey = Key.builder()
                .partitionValue(followee)
                .build();
        // load it if it exists
        UserBean followeeBean = table.getItem(followeeKey);

        Key followerKey = Key.builder()
                .partitionValue(follower)
                .build();
        // load it if it exists
        UserBean followerBean = table.getItem(followerKey);

        if(followeeBean != null && followerBean != null) {
            followeeBean.setFollowerCount(followeeBean.getFollowerCount() - 1);
            followerBean.setFolloweeCount(followerBean.getFolloweeCount() - 1);
            table.updateItem(followeeBean);
            table.updateItem(followerBean);
        }
        else{
            throw new RuntimeException("Users Not Found In Database");
        }
    }

    public int getFolloweeCount(User targetUser) {
        Key key = Key.builder()
                .partitionValue(targetUser.getAlias())
                .build();
        UserBean userBean = table.getItem(key);

        if (userBean == null) {
            throw new RuntimeException("User Not Found In Database");
        }

        return userBean.getFolloweeCount();
    }

    public int getFollowerCount(User targetUser) {
        Key key = Key.builder()
                .partitionValue(targetUser.getAlias())
                .build();
        UserBean userBean = table.getItem(key);

        if (userBean == null) {
            throw new RuntimeException("User Not Found In Database");
        }

        return userBean.getFollowerCount();
    }

    public Pair<User, String> loginUser(String username) {
        Key key = Key.builder()
                .partitionValue(username)
                .build();
        UserBean userBean = table.getItem(key);

        if (userBean == null) {
            throw new RuntimeException("User Not Found In Database");
        }

        User user = new User(userBean.getFirst_name(), userBean.getLast_name(),
                userBean.getAlias(), userBean.getImageUrl());
        return new Pair<>(user, userBean.getPassword());
    }

    public User registerUser(String username, String password, String firstName, String lastName,
                                 String image) {
        UserBean userBean = new UserBean();
        userBean.setFollowerCount(0);
        userBean.setFolloweeCount(0);
        userBean.setAlias(username);
        userBean.setPassword(password);
        userBean.setFirst_name(firstName);
        userBean.setLast_name(lastName);
        userBean.setImageUrl(image);
        table.putItem(userBean);

        return new User(userBean.getFirst_name(), userBean.getLast_name(),
                userBean.getAlias(), userBean.getImageUrl());
    }

    public void addUserBatch(List<UserBean> users) {
        List<UserBean> batchToWrite = new ArrayList<>();
        for (UserBean user : users) {
            batchToWrite.add(user);
            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }

    private void writeChunkOfUserDTOs(List<UserBean> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        WriteBatch.Builder<UserBean> writeBuilder = WriteBatch.builder(UserBean.class).mappedTableResource(table);
        for (UserBean item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
