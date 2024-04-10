package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String URL_PATH = "/login";

    private LoginResponse response;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Response doRequest() throws IOException, TweeterRemoteException {
        LoginRequest request = new LoginRequest(username, password);
        response = getServerFacade().login(request, URL_PATH);
        return response;
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        User loggedInUser = response.getUser();
        AuthToken authToken = response.getAuthToken();
        return new Pair<>(loggedInUser, authToken);
    }
}
