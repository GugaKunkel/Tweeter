package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String URL_PATH = "/getfollowercount";

    private CountResponse response;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        return response.getCount();
    }

    @Override
    protected CountResponse doRequest() throws IOException, TweeterRemoteException {
        CountRequest request = new CountRequest(getTargetUser(), getAuthToken());
        response = getServerFacade().getCount(request, URL_PATH);

        return response;
    }
}
