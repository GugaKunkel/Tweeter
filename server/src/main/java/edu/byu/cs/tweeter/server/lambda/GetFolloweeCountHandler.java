package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetFolloweeCountHandler implements RequestHandler<CountRequest, CountResponse> {

    @Override
    public CountResponse handleRequest(CountRequest request, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        UserService service = new UserService(dynamoFactory);
        return service.getFolloweeCount(request);
    }
}
