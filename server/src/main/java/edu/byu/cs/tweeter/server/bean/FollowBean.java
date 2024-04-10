package edu.byu.cs.tweeter.server.bean;

import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FollowBean {
    private String follower_alias;
    private String followee_alias;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowsDAO.IndexName)
    public String getFollower_alias() {
        return follower_alias;
    }

    public void setFollower_alias(String follower_alias) {
        this.follower_alias = follower_alias;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FollowsDAO.IndexName)
    public String getFollowee_alias() {
        return followee_alias;
    }

    public void setFollowee_alias(String followee_alias) {
        this.followee_alias = followee_alias;
    }

    @Override
    public String toString() {
        return "{" +
                ", follower_handle='" + follower_alias + '\'' +
                ", followee_handle='" + followee_alias + '\'' +
                '}';
    }
}
