package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends UserPresenter{

    public FollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    void getItems(User user, User lastItem) {
        getFollowService().loadMoreFollowees(user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }
}
