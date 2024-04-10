package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler implements RequestHandler<UserRequest, UserResponse> {

    @Override
    public UserResponse handleRequest(UserRequest request, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        UserService service = new UserService(dynamoFactory);
        return service.getChosenUser(request);
    }
}
