package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.factory.DynamoDBFactory;
import edu.byu.cs.tweeter.server.queueModel.BatchShareTweet;
import edu.byu.cs.tweeter.server.queueModel.JsonSerializer;
import edu.byu.cs.tweeter.server.queueModel.StatusPost;
import edu.byu.cs.tweeter.server.service.FollowService;

public class PostUpdateFeedMessages implements RequestHandler<SQSEvent, Void> {

    private final int PAGE_SIZE = 25;
    private final String QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/187351745941/UpdateFeedQueue";

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DynamoDBFactory dynamoFactory = new DynamoDBFactory();
        FollowService service = new FollowService(dynamoFactory);

        for(SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println("POST_UPDATE_FEED_MESSAGES: " + msg.getBody());
            StatusPost body = JsonSerializer.deserialize(msg.getBody(), StatusPost.class);

            String lastFollowerAlias = null;
            AuthToken token = body.getToken();
            String user = body.getUserAlias();
            Status post = body.getUserPost();
            boolean hasMorePages = true;

            do {
                FollowersRequest request = new FollowersRequest(token, user, PAGE_SIZE, lastFollowerAlias);
                FollowersResponse response = service.getFollowers(request);
                if (!response.isSuccess()) {
                    throw new RuntimeException("[Server Error] The request to add a post failed");
                }

                BatchShareTweet batch = new BatchShareTweet(response.getFollowers(), post, token);

                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(QUEUE_URL)
                        .withMessageBody(JsonSerializer.serialize(batch));

                AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

                SendMessageResult sendMessageResult = sqs.sendMessage(send_msg_request);
                System.out.println(sendMessageResult.getMessageId());

                if (!response.getHasMorePages()){
                    hasMorePages = false;
                } else {
                    try {
                        lastFollowerAlias = response.getFollowers().get(PAGE_SIZE - 1).getAlias();
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } while (hasMorePages);
        }
        return null;
    }
}
