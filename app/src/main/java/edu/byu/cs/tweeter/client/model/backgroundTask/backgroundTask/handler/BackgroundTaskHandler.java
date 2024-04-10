package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.BackgroundTask;

public abstract class BackgroundTaskHandler<T extends ServiceObserver> extends Handler {
    private final T observer;
    private final String failureString;
    private final String exceptionString;

    public BackgroundTaskHandler(T observer, String failureString, String exceptionString) {
        super(Looper.getMainLooper());
        this.observer = observer;
        this.failureString = failureString;
        this.exceptionString = exceptionString;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success) {
            handleSuccess(msg.getData(), observer);
        } else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            observer.handleFailure(failureString + message);
        } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
            observer.handleFailure(exceptionString + ex.getMessage());
        }
    }
    protected abstract void handleSuccess(Bundle data, T observer);
}
