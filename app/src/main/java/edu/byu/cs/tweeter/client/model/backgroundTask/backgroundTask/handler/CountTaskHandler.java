package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetCountTask;

public class CountTaskHandler extends BackgroundTaskHandler<SingleArgObserver<Integer>> {
    public CountTaskHandler(SingleArgObserver<Integer> observer, String failureString, String exceptionString) {
        super(observer, failureString, exceptionString);
    }

    @Override
    protected void handleSuccess(Bundle data, SingleArgObserver<Integer> observer) {
        int count = data.getInt(GetCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
