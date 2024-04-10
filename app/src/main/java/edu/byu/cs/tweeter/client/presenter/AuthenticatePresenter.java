package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.presenter.observers.MainObserver;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends BasePresenter<AuthenticateView> {

    private final LoginService loginService;

    public AuthenticatePresenter(AuthenticateView view) {
        super(view);
        this.loginService = new LoginService();
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public static class AuthenticatePresenterObserver extends MainObserver<AuthenticateView> implements AuthenticateObserver {

        public AuthenticatePresenterObserver(AuthenticateView view) {
            super(view);
        }

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            getView().loginAuthenticatedUser(user);
        }
    }

}
