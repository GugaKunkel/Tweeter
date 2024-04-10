package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.util.Pair;

public interface UserInterface {
    User getUser(String targetUserAlias);
    int getFolloweeCount(User targetUser);
    int getFollowerCount(User targetUser);
    Pair<User, String> loginUser(String username);
    User registerUser(String username, String password, String firstName, String lastName, String image);
    void followUser(String followee, String follower);
    void unfollowUser(String followee, String follower);
    void addUserBatch(List<UserBean> users);
}
