package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.ServiceUtils.runTask;

import android.content.Context;
import android.content.Intent;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.UserTaskHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetUserTask;

public class UserService {
    public void loadUser(String userAlias, SingleArgObserver<Intent> observer, Context context) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserTaskHandler(observer, context,
                "Failed to get user's profile: ",
                "Failed to get user's profile because of exception: "));
        runTask(getUserTask);
    }
}
