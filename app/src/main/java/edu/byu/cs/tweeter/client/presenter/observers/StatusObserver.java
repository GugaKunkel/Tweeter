package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class StatusObserver extends MainObserver<MainPresenter.View> implements SimpleNotificationObserver {

    public StatusObserver(MainPresenter.View view) {
        super(view);
    }

    @Override
    public void handleSuccess() {
        getView().postStatus();
    }
}
