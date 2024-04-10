package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class LogoutObserver extends MainObserver<MainPresenter.View> implements SimpleNotificationObserver {

    public LogoutObserver(MainPresenter.View view) {
        super(view);
    }

    @Override
    public void handleSuccess() {
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        getView().logout();
    }
}
