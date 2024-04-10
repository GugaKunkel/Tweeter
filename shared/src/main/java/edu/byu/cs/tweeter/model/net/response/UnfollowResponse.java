package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class UnfollowResponse extends Response{

    public UnfollowResponse() {
        super(true);
    }

    public UnfollowResponse(String message) {
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

        UnfollowResponse that = (UnfollowResponse) param;

        return (Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
