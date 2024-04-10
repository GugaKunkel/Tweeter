package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class StatusPresenter extends PagedPresenter<Status> {

    private final StatusService statusService;

    public StatusPresenter(PagedView<Status> view) {
        super(view);
        this.statusService = new StatusService();
    }

    public StatusService getStatusService() {
        return statusService;
    }
}
