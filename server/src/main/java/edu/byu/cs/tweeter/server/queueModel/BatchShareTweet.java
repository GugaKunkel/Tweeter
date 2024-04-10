package edu.byu.cs.tweeter.server.queueModel;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class BatchShareTweet {

    private List<User> followers;
    private Status post;
    private AuthToken token;

    public BatchShareTweet(List<User> followers, Status post, AuthToken token) {
        this.followers = followers;
        this.post = post;
        this.token = token;
    }

    private BatchShareTweet(){}

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public Status getPost() {
        return post;
    }

    public void setPost(Status post) {
        this.post = post;
    }

    public AuthToken getToken() {
        return token;
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }
}
