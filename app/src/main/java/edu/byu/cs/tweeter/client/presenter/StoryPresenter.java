package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends StatusPresenter {

    public StoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    void getItems(User user, Status lastItem) {
        getStatusService().loadStory(user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }
}
