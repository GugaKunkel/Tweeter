package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class FollowResponse extends Response {

    public FollowResponse() {
        super(true);
    }

    public FollowResponse(String message) {
        super(false, message);
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FollowResponse that = (FollowResponse) param;

        return (Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
