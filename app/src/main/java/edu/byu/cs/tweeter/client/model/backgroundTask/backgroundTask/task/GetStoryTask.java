package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String URL_PATH = "/getstory";

    private StoryResponse response;

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected PagedResponse doRequest() throws IOException, TweeterRemoteException {
        StoryRequest request = new StoryRequest(
                getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem());
        response = getServerFacade().getStory(request, URL_PATH);

        return response;
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        return new Pair<> (response.getStory(), response.getHasMorePages());
    }
}
