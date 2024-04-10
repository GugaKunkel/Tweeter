package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    private static final String URL_PATH = "/getfollowing";

    private FollowingResponse response;

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected PagedResponse doRequest() throws IOException, TweeterRemoteException {
        String lastItem = null;
        if (getLastItem() != null) {
            lastItem = getLastItem().getAlias();
        }

        FollowingRequest request = new FollowingRequest(
                getAuthToken(), getTargetUser().getAlias(), getLimit(), lastItem);

        response = getServerFacade().getFollowees(request, URL_PATH);

        return response;
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws IOException, TweeterRemoteException {
        return new Pair<> (response.getFollowees(), response.getHasMorePages());
    }
}
