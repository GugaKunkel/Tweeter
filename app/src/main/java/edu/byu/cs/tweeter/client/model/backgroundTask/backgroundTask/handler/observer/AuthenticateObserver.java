package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticateObserver extends ServiceObserver {
    void handleSuccess(User user, AuthToken authToken);
}
