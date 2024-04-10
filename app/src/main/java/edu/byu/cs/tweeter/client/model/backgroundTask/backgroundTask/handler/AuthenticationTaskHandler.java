package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.AuthenticateTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationTaskHandler extends BackgroundTaskHandler<AuthenticateObserver> {

    public AuthenticationTaskHandler(AuthenticateObserver observer, String failureString, String exceptionString) {
        super(observer, failureString, exceptionString);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticateObserver observer) {
        User userToLogin = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);
        observer.handleSuccess(userToLogin, authToken);
    }
}
