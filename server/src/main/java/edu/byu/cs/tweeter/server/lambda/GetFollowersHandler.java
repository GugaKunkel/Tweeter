package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {

    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        FollowService service = new FollowService(dynamoFactory);
        return service.getFollowers(request);
    }
}
