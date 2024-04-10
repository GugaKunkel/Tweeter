package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.ServiceUtils.runTask;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.PostStatusTask;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public void loadFeed(User user, int pageSize, Status lastStatus, PagedObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<>(observer,
                "Failed to get feed: ", "Failed to get feed because of exception: "));
        runTask(getFeedTask);
    }

    public void postStatus(Status newStatus, SimpleNotificationObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new SimpleNotificationHandler(observer,
                "Failed to post status: ", "Failed to post status because of exception: "));
        runTask(statusTask);
    }

    public void loadStory(User user, int pageSize, Status lastStatus, PagedObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<>(observer,
                "Failed to get story: ", "Failed to get story because of exception: "));
        runTask(getStoryTask);
    }
}
