package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.PagedTask;

public class PagedTaskHandler<T> extends BackgroundTaskHandler<PagedObserver<T>>{
    public PagedTaskHandler(PagedObserver<T> observer, String failureString, String exceptionString) {
        super(observer, failureString, exceptionString);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleSuccess(Bundle data, PagedObserver<T> observer) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
