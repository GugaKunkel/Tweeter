package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface StoryInterface {
    Pair<List<Status>, Boolean> getStory(String userAlias, int limit, Status lastStatus);
    void postStatus(String userAlias, String post, List<String> mentions, List<String> urls, long timestamp);
}
