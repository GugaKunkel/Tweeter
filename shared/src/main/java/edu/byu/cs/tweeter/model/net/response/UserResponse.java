package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response{
    private User user;

    public UserResponse(String message) {
        super(false, message);
    }

     public UserResponse(User user) {
        super(true, null);
        this.user = user;
    }

    public User getUser() {
       return user;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        UserResponse that = (UserResponse) param;

        return (Objects.equals(user, that.user) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
