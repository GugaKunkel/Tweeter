package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowersRequest {

    private AuthToken authToken;
    private String followeeAlias;
    private int limit;
    private String lastFollowerAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowersRequest() {}


    public FollowersRequest(AuthToken authToken, String followeeAlias, int limit, String lastFollowerAlias) {
        this.authToken = authToken;
        this.followeeAlias = followeeAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    /**
     * Sets the follower.
     *
     * @param followeeAlias the follower.
     */
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    /**
     * Sets the last followee.
     *
     * @param lastFollowerAlias the last followee.
     */
    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }
}
