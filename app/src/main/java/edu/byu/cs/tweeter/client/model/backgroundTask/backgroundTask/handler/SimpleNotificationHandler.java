package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;

public class SimpleNotificationHandler extends BackgroundTaskHandler<SimpleNotificationObserver> {
    public SimpleNotificationHandler(SimpleNotificationObserver observer, String failureString, String exceptionString) {
        super(observer, failureString, exceptionString);
    }

    @Override
    protected void handleSuccess(Bundle data, SimpleNotificationObserver observer) {
        observer.handleSuccess();
    }
}
