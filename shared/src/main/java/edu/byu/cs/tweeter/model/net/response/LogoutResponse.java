package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class LogoutResponse extends Response {

    public LogoutResponse() {
        super(true);
    }

    public LogoutResponse(String message) {
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

        LogoutResponse that = (LogoutResponse) param;

        return (Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
