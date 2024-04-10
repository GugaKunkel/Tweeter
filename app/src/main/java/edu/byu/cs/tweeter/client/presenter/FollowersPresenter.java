package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends UserPresenter {

    public FollowersPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    void getItems(User user, User lastItem) {
        getFollowService().loadMoreFollowers(user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }
}
