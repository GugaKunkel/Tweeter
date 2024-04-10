package edu.byu.cs.tweeter.client.model.service;

import static edu.byu.cs.tweeter.client.model.service.ServiceUtils.runTask;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.AuthenticationTaskHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.LoginTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.LogoutTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.RegisterTask;

public class LoginService {

    public void logoutUser(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer,
                "Failed to logout: ", "Failed to logout because of exception: "));
        runTask(logoutTask);
    }

    public void loginUser(String alias, String password, AuthenticateObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticationTaskHandler(observer,
                "Failed to login: ","Failed to login because of exception: "));
        runTask(loginTask);
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String image,
                             AuthenticateObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, image, new AuthenticationTaskHandler(observer,
                "Failed to register: ", "Failed to register because of exception: "));
        runTask(registerTask);
    }
}
