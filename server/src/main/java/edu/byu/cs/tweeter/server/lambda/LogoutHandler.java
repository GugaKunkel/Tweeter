package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {

    @Override
    public LogoutResponse handleRequest(LogoutRequest request, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        UserService service = new UserService(dynamoFactory);
        return service.logout(request);
    }
}
