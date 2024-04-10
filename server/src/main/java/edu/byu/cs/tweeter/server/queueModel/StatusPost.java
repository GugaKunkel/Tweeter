package edu.byu.cs.tweeter.server.queueModel;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class StatusPost {

    private String userAlias;
    private Status userPost;
    private AuthToken token;

    private StatusPost() {}

    public StatusPost(String userAlias, Status userPost, AuthToken token) {
        this.userAlias = userAlias;
        this.userPost = userPost;
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public Status getUserPost() {
        return userPost;
    }

    public void setUserPost(Status userPost) {
        this.userPost = userPost;
    }
}
