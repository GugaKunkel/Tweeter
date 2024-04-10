package edu.byu.cs.tweeter.server.factory;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;

public class DynamoDBFactory implements Factory {

    FollowInterface followDao;
    FeedInterface feedDao;
    StoryInterface storyDao;
    UserInterface userDao;
    ImageInterface imageDao;
    AuthTokenInterface authTokenDao;

    public DynamoDBFactory() {
        this.followDao = new FollowsDAO();
        this.feedDao = new FeedDAO();
        this.storyDao = new StoryDAO();
        this.userDao = new UserDAO();
        this.imageDao = new ImageDAO();
        this.authTokenDao = new AuthTokenDAO();
    }

    @Override
    public FollowInterface getFollowDAO() {
        return followDao;
    }

    @Override
    public UserInterface getUserDAO() {
        return userDao;
    }

    @Override
    public FeedInterface getFeedDAO() {
        return feedDao;
    }

    @Override
    public StoryInterface getStoryDAO() {
        return storyDao;
    }

    @Override
    public ImageInterface getImageDAO() {
        return imageDao;
    }

    @Override
    public AuthTokenInterface getAuthTokenDAO() {
        return authTokenDao;
    }
}
