package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class FollowerCountObserver extends MainObserver<MainPresenter.View> implements SingleArgObserver<Integer> {
    public FollowerCountObserver(MainPresenter.View view) {
        super(view);
    }

    @Override
    public void handleSuccess(Integer value) {
        getView().returnFollowerCount(value);
    }
}
