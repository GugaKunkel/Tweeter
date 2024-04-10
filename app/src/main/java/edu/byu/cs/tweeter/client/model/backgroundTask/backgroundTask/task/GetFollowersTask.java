package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    private static final String URL_PATH = "/getfollowers";

    private FollowersResponse response;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected PagedResponse doRequest() throws IOException, TweeterRemoteException {
        String lastItem = null;
        if (getLastItem() != null) {
            lastItem = getLastItem().getAlias();
        }

        FollowersRequest request = new FollowersRequest(
                getAuthToken(), getTargetUser().getAlias(), getLimit(), lastItem);

        response = getServerFacade().getFollowers(request, URL_PATH);

        return response;
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        return new Pair<> (response.getFollowers(), response.getHasMorePages());
    }
}
