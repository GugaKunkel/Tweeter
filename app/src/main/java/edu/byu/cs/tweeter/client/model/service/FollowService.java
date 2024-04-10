package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.ServiceUtils.runTask;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.CountTaskHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.IsFollowerTaskHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.FollowTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public void loadMoreFollowees(User user, int pageSize, User lastFollowee, PagedObserver<User> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new PagedTaskHandler<>(observer,
                "Failed to get following: ", "Failed to get following because of exception: "));
        runTask(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollower, PagedObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new PagedTaskHandler<>(observer,
                "Failed to get followers: ", "Failed to get followers because of exception: "));
        runTask(getFollowersTask);
    }

    public void getFollowerCount(User selectedUser, SingleArgObserver<Integer> observer) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new CountTaskHandler(observer,
                "Failed to get follower count: ", "Failed to get follower count because of exception: "));
        runTask(followersCountTask);
    }

    public void getFolloweeCount(User selectedUser, SingleArgObserver<Integer> observer) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new CountTaskHandler(observer,
                "Failed to get following count: ", "Failed to get following count because of exception: "));
        runTask(followingCountTask);
    }

    public void getIsFollower(User selectedUser, SingleArgObserver<Boolean> observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerTaskHandler(observer,
                "Failed to determine following relationship: ",
                "Failed to determine following relationship because of exception: "));
        runTask(isFollowerTask);
    }

    public void unfollowUser(User selectedUser, SimpleNotificationObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer,
                "Failed to unfollow: ", "Failed to unfollow because of exception: "));
        runTask(unfollowTask);
    }

    public void followUser(User selectedUser, SimpleNotificationObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer,
                "Failed to follow: ", "Failed to follow because of exception: "));
        runTask(followTask);
    }
}
