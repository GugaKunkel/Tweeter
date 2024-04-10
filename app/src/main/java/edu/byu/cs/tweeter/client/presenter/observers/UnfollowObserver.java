package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class UnfollowObserver extends MainObserver<MainPresenter.View> implements SimpleNotificationObserver {
    public UnfollowObserver(MainPresenter.View view) {
        super(view);
    }

    @Override
    public void handleSuccess() {
        getView().updateUserFollowingAndFollowers(true);
    }
}
