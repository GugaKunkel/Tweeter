package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public abstract class BasePresenter<T extends BaseView> {

    private final T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    public T getView() {
        return view;
    }
}
