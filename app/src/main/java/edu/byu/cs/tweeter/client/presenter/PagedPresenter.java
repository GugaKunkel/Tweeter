package edu.byu.cs.tweeter.client.presenter;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.observers.MainObserver;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends BasePresenter<PagedView<T>> {
    protected static final int PAGE_SIZE = 10;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private T lastItem;
    private final UserService userService;

    public PagedPresenter(PagedView<T> view) {
        super(view);
        this.userService = new UserService();
    }

    public void getUser(String userAlias, Context context) {
        userService.loadUser(userAlias, new GetUserObserver(getView()), context);
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            getView().setLoadingFooter(true);
            getItems(user, lastItem);
        }
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }

    public class PagedPresenterObserver implements PagedObserver<T> {
        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            getView().setLoadingFooter(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            getView().addMoreItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            getView().setLoadingFooter(false);
            getView().displayMessage(message);
        }
    }

    public class GetUserObserver extends MainObserver<PagedView<T>> implements SingleArgObserver<Intent> {
        public GetUserObserver(PagedView<T> view) {
            super(view);
        }

        @Override
        public void handleSuccess(Intent intent) {
            getView().returnUser(intent);
        }
    }

    abstract void getItems(User user, T lastItem);
}
