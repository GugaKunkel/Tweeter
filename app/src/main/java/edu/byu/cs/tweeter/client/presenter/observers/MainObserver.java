package edu.byu.cs.tweeter.client.presenter.observers;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public class MainObserver<T extends BaseView> extends BasePresenter<T> implements ServiceObserver {

    public MainObserver(T view) {
        super(view);
    }

    @Override
    public void handleFailure(String message) {
        getView().displayMessage(message);
    }
}

