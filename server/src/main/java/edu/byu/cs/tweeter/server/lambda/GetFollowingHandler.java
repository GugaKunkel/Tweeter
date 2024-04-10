package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse> {

    @Override
    public FollowingResponse handleRequest(FollowingRequest request, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        FollowService service = new FollowService(dynamoFactory);
        return service.getFollowees(request);
    }
}
