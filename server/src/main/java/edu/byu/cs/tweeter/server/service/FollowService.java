package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;
import edu.byu.cs.tweeter.server.factory.Factory;
import edu.byu.cs.tweeter.util.Pair;

public class FollowService {

    AuthTokenInterface authTokenDao;
    FollowInterface followDao;
    UserInterface userDao;

    public FollowService(Factory databaseFactory) {
        this.authTokenDao = databaseFactory.getAuthTokenDAO();
        this.followDao = databaseFactory.getFollowDAO();
        this.userDao = databaseFactory.getUserDAO();
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            Pair<List<String>, Boolean> result = followDao.getFollowees(
                    request.getFollowerAlias(), request.getLastFolloweeAlias(), request.getLimit());
            List<User> users = new ArrayList<>();
            for (String alias : result.getFirst()) {
                User user = userDao.getUser(alias);
                users.add(user);
            }

            return new FollowingResponse(users, result.getSecond());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new FollowingResponse("[Server Error] " + e.getMessage());
        }
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            Pair<List<String>, Boolean> result = followDao.getFollowers(
                    request.getFolloweeAlias(), request.getLastFollowerAlias(), request.getLimit());
            List<User> users = new ArrayList<>();
            for (String alias : result.getFirst()) {
                User user = userDao.getUser(alias);
                users.add(user);
            }
            return new FollowersResponse(users, result.getSecond());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new FollowersResponse("[Server Error] " + e.getMessage());
        }
    }

    public IsFollowerResponse getFollowStatus(IsFollowerRequest request) {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());

            // Gets the list of all the followers of the current user
            List<String> getFollowersResult = followDao.getAllFollowers(
                    request.getFollowee().getAlias());

            List<User> allFollowers = new ArrayList<>();
            for (String alias : getFollowersResult) {
                User user = userDao.getUser(alias);
                allFollowers.add(user);
            }

            boolean result = followDao.getFollowStatus(
                    request.getFollower(), allFollowers);

            return new IsFollowerResponse(result);
        }
        catch (Exception e) {
            return new IsFollowerResponse("[Server Error] " + e.getMessage());
        }
    }

    public FollowResponse followUser (FollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            followDao.followUser(request.getFollowee().getAlias(), request.getFollower().getAlias());
            userDao.followUser(request.getFollowee().getAlias(), request.getFollower().getAlias());

            return new FollowResponse();
        }
        catch (Exception e) {
            return new FollowResponse("[Server Error] " + e.getMessage());
        }
    }

    public UnfollowResponse unfollowUser (UnfollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            followDao.unfollowUser(request.getFollowee().getAlias(), request.getFollower().getAlias());
            userDao.unfollowUser(request.getFollowee().getAlias(), request.getFollower().getAlias());

            return new UnfollowResponse();
        }
        catch (Exception e) {
            return new UnfollowResponse("[Server Error] " + e.getMessage());
        }
    }
}
