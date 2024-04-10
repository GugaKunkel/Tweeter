package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.IsFollowerTask;

// IsFollowerHandler
public class IsFollowerTaskHandler extends BackgroundTaskHandler<SingleArgObserver<Boolean>> {
    public IsFollowerTaskHandler(SingleArgObserver<Boolean> observer,  String failureString, String exceptionString) {
        super(observer, failureString, exceptionString);
    }

    @Override
    protected void handleSuccess(Bundle data, SingleArgObserver<Boolean> observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
