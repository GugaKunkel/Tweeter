package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.queueModel.BatchShareTweet;
import edu.byu.cs.tweeter.util.Pair;

public interface FeedInterface {
    void postStatus(String userAlias, String post, List<String> mentions,
                    List<String> urls, long timestamp, List<User> users);
    Pair<List<Pair<Status, String>>, Boolean> getFeed(String userAlias, int limit, Status lastStatus);
    void addFeedBatch(List<User> followers, String user, Status post);
}
