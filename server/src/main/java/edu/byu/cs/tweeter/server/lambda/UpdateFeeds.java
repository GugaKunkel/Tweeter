package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.queueModel.BatchShareTweet;
import edu.byu.cs.tweeter.server.queueModel.JsonSerializer;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeeds implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        StatusService service = new StatusService(dynamoFactory);

        for(SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println("UPDATE_FEEDS: " + msg.getBody());
            BatchShareTweet body = JsonSerializer.deserialize(msg.getBody(), BatchShareTweet.class);
            service.batchFeedUpdate(body);
        }
        return null;
    }
}
