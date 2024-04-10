package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserPresenter extends PagedPresenter<User> {

    private final FollowService followService;

    public FollowService getFollowService() {
        return followService;
    }

    public UserPresenter(PagedView<User> view) {
        super(view);
        this.followService = new FollowService();
    }

}
