package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class PostStatusResponse extends Response{

    public PostStatusResponse() {
        super(true);
    }

    public PostStatusResponse(String message) {
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

        FeedResponse that = (FeedResponse) param;

        return (Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
