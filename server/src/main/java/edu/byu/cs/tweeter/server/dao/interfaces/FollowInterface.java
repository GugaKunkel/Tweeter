package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowInterface {
    Pair<List<String>, Boolean> getFollowees(String targetUserAlias, String lastUserAlias, int limit);
    Pair<List<String>, Boolean> getFollowers(String targetUserAlias, String lastUserAlias, int limit);
    boolean getFollowStatus(User follower, List<User> allFollowers);
    void followUser(String follower, String followee);
    void unfollowUser(String follower, String followee);
    List<String> getAllFollowers(String userAlias);
    void addFollowersBatch(List<String> followers, String followTarget);
}
