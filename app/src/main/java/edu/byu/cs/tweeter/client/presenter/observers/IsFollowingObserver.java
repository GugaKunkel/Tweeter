package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class IsFollowingObserver extends MainObserver<MainPresenter.View> implements SingleArgObserver<Boolean> {

    public IsFollowingObserver(MainPresenter.View view) {
        super(view);
    }

    @Override
    public void handleSuccess(Boolean value) {
        getView().returnIsFollower(value);
    }
}
