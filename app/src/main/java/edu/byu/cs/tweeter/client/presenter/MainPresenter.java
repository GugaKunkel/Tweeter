package edu.byu.cs.tweeter.client.presenter;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.observers.FollowObserver;
import edu.byu.cs.tweeter.client.presenter.observers.FolloweeCountObserver;
import edu.byu.cs.tweeter.client.presenter.observers.FollowerCountObserver;
import edu.byu.cs.tweeter.client.presenter.observers.IsFollowingObserver;
import edu.byu.cs.tweeter.client.presenter.observers.LogoutObserver;
import edu.byu.cs.tweeter.client.presenter.observers.StatusObserver;
import edu.byu.cs.tweeter.client.presenter.observers.UnfollowObserver;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.View> {

    public interface View extends BaseView {
        void returnIsFollower(boolean isFollower);
        void displayUnfollowMessage(String message);
        void displayFollowMessage(String message);
        void updateUserFollowingAndFollowers(boolean isFollowing);
        void returnFollowerCount(int count);
        void returnFollowingCount(int count);
        void postStatus();
        void logout();
    }

    private FollowService followService;
    private StatusService statusService;
    private LoginService loginService;

    private Status newStatus; //FOR TESTING

    public MainPresenter(View view) {
        super(view);
    }

    //This is for mocking in unit tests
    protected LoginService getLoginService() {
        if(loginService == null){
            loginService = new LoginService();
        }
        return loginService;
    }
    protected FollowService getFollowService() {
        if(followService == null){
            followService = new FollowService();
        }
        return followService;
    }

    protected StatusService getStatusService() {
        if(statusService == null){
            statusService = new StatusService();
        }
        return statusService;
    }

    public void getIsFollower(User selectedUser) {
        getFollowService().getIsFollower(selectedUser, new IsFollowingObserver(getView()));
    }

    public void unfollow(User selectedUser) {
        getFollowService().unfollowUser(selectedUser, new UnfollowObserver(getView()));
    }

    public void follow(User selectedUser) {
        getFollowService().followUser(selectedUser, new FollowObserver(getView()));
    }

    public void getFollowerCount(User selectedUser) {
        getFollowService().getFollowerCount(selectedUser, new FollowerCountObserver(getView()));
    }

    public void getFolloweeCount(User selectedUser) {
        getFollowService().getFolloweeCount(selectedUser, new FolloweeCountObserver(getView()));
    }

    public void postStatus(String post) {
        newStatus = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(), parseURLs(post), parseMentions(post));
        getStatusService().postStatus(newStatus, new StatusObserver(getView()));
    }

    public Status getNewStatus() {
        return newStatus;
    }

    public void logoutUser() {
        getLoginService().logoutUser(new LogoutObserver(getView()));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z\\d]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }
}
