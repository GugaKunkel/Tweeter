package edu.byu.cs.tweeter.server.factory;

import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;

public interface Factory {
    FollowInterface getFollowDAO();
    UserInterface getUserDAO();
    FeedInterface getFeedDAO();
    StoryInterface getStoryDAO();
    ImageInterface getImageDAO();
    AuthTokenInterface getAuthTokenDAO();
}
