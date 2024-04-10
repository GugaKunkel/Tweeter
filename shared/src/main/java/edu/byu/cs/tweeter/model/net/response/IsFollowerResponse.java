package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class IsFollowerResponse extends Response {
    private boolean followStatus;

    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public IsFollowerResponse(boolean followStatus) {
        super(true, null);
        this.followStatus = followStatus;
    }

    public boolean getFollowStatus() {
        return followStatus;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        IsFollowerResponse that = (IsFollowerResponse) param;

        return (Objects.equals(followStatus, that.followStatus) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(followStatus);
    }
}
