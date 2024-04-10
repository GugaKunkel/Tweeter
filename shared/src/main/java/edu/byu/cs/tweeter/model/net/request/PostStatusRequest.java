package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest {

    private AuthToken authToken;
    private Status post;

    public PostStatusRequest() {}

    public PostStatusRequest(AuthToken authToken, Status post) {
        this.authToken = authToken;
        this.post = post;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public Status getPost() {
        return post;
    }

    public void setPost(Status post) {
        this.post = post;
    }
}
