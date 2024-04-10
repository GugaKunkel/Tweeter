package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends StatusPresenter{

    public FeedPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    void getItems(User user, Status lastItem) {
        getStatusService().loadFeed(user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }
}
