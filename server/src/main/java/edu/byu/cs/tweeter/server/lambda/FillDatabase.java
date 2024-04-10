package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.Filler;

public class FillDatabase implements RequestHandler<Void, Void> {

    @Override
    public Void handleRequest(Void input, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        Filler filler = new Filler(dynamoFactory);
        filler.fillDatabase();
        return null;
    }
}
