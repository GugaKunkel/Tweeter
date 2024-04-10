package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;
import edu.byu.cs.tweeter.server.factory.Factory;
import edu.byu.cs.tweeter.server.queueModel.BatchShareTweet;
import edu.byu.cs.tweeter.server.queueModel.JsonSerializer;
import edu.byu.cs.tweeter.server.queueModel.StatusPost;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {
    AuthTokenInterface authTokenDao;
    StoryInterface storyDao;
    FeedInterface feedDao;
    UserInterface userDao;
    FollowInterface followDao;
    private final String QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/187351745941/PostStatusQueue";

    public StatusService(Factory databaseFactory) {
        this.authTokenDao = databaseFactory.getAuthTokenDAO();
        this.userDao = databaseFactory.getUserDAO();
        this.storyDao = databaseFactory.getStoryDAO();
        this.feedDao = databaseFactory.getFeedDAO();
        this.followDao = databaseFactory.getFollowDAO();
    }
    public StoryResponse getStory(StoryRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }
        try {
            authTokenDao.validateToken(request.getAuthToken());
            Pair<List<Status>, Boolean> storyResult = storyDao.getStory(request.getUserAlias(),
                    request.getLimit(), request.getLastStatus());

            List<Status> story = new ArrayList<>();
            for (Status status : storyResult.getFirst()) {
                User user = userDao.getUser(request.getUserAlias());
                status.setUser(user);
                story.add(status);
            }

            return new StoryResponse(story, storyResult.getSecond());
        }
        catch (Exception e) {
            return new StoryResponse("[Server Error] " + e.getMessage());
        }
    }

    public FeedResponse getFeed(FeedRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            Pair<List<Pair<Status, String>>, Boolean> feedResult = feedDao.getFeed(request.getUserAlias(),
                    request.getLimit(), request.getLastStatus());

            List<Status> feed = new ArrayList<>();
            for (Pair<Status, String> values : feedResult.getFirst()) {
                User user = userDao.getUser(values.getSecond());
                Status status = values.getFirst();
                status.setUser(user);
                feed.add(status);
            }

            return new FeedResponse(feed, feedResult.getSecond());
        }
        catch (Exception e) {
            return new FeedResponse("[Server Error] " + e.getMessage());
        }
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getPost() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a post value");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }
        try {
            authTokenDao.validateToken(request.getAuthToken());
            String userAlias = request.getPost().getUser().getAlias();
            String post = request.getPost().getPost();
            List<String> mentions = request.getPost().getMentions();
            List<String> urls = request.getPost().getUrls();
            long timestamp = request.getPost().getTimestamp();

            // Adds post to the users story
            storyDao.postStatus(userAlias, post, mentions, urls, timestamp);
            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(QUEUE_URL)
                    .withMessageBody(JsonSerializer.serialize(new StatusPost(userAlias, request.getPost(), request.getAuthToken())));

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

            SendMessageResult sendMessageResult = sqs.sendMessage(send_msg_request);
            System.out.println(sendMessageResult.getMessageId());

            return new PostStatusResponse();
        }
        catch (Exception e) {
            return new PostStatusResponse("[Server Error] " + e.getMessage());
        }
    }

    public void batchFeedUpdate(BatchShareTweet body){
        try {
            List<User> followers = body.getFollowers();
            String user = body.getPost().getUser().getAlias();
            Status post = body.getPost();
            feedDao.addFeedBatch(followers, user, post);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
