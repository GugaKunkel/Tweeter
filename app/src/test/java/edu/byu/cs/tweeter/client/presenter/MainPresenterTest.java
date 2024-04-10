package edu.byu.cs.tweeter.client.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.observers.LogoutObserver;
import edu.byu.cs.tweeter.client.presenter.observers.StatusObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterTest {

    private MainPresenter.View mockView;
    private LoginService mockLoginService;
    private StatusService mockStatusService;
    private Cache mockCache;
    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        // Create mocks
        mockView = Mockito.mock(MainPresenter.View.class);
        mockLoginService = Mockito.mock(LoginService.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mockCache = Mockito.mock(Cache.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        // This mock will work on void methods
        // Mockito.doReturn(mockLoginService).when(mainPresenterSpy).getLoginService();
        Mockito.when(mainPresenterSpy.getLoginService()).thenReturn(mockLoginService);
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    @Test
    public void testLogout_Success(){
        // Make and answer callback to use observer functions
        Answer<Void> answer = invocation -> {
            LogoutObserver observer = invocation.getArgument(0, LogoutObserver.class);
            observer.handleSuccess();
            return null;
        };
        // Have Mockito mock using our Answer
        Mockito.doAnswer(answer).when(mockLoginService).logoutUser(any());

        mainPresenterSpy.logoutUser();
        Mockito.verify(mockCache).clearCache();
        Mockito.verify(mockView).logout();
    }

    @Test
    public void testLogout_Failure(){
        // Make and answer callback to use observer functions
        Answer<Void> answer = invocation -> {
            LogoutObserver observer = invocation.getArgument(0, LogoutObserver.class);
            observer.handleFailure("Error message");
            return null;
        };
        // Have Mockito mock using our Answer
        Mockito.doAnswer(answer).when(mockLoginService).logoutUser(any());

        mainPresenterSpy.logoutUser();
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        Mockito.verify(mockView).displayMessage("Error message");
    }

    @Test
    public void testPostStatus_Success(){
        // Make and answer callback to use observer functions
        Answer<Void> statusObserverAnswer = invocation -> {
            StatusObserver observer = invocation.getArgument(1, StatusObserver.class);
            observer.handleSuccess();
            return null;
        };

        // Have Mockito mock using our Answer
        Mockito.doAnswer(statusObserverAnswer).when(mockStatusService).postStatus(any(),any());

        mainPresenterSpy.postStatus("Hello World");
        // Checks that the status service postStatus is called with correct params
        Mockito.verify(mockStatusService).postStatus(isA(Status.class), isA(StatusObserver.class));
        // Calls the view PostStatus class where the "Successfully Posted!" toast is also shown
        Mockito.verify(mockView).postStatus();
    }

    @Test
    public void testPostStatus_Failure(){
        // Make and answer callback to use observer functions
        Answer<Void> answer = invocation -> {
            StatusObserver observer = invocation.getArgument(1, StatusObserver.class);

            observer.handleFailure("Error message");
            return null;
        };

        // Have Mockito mock using our Answer
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(any(),any());

        mainPresenterSpy.postStatus("Hello World");
        // Makes sure the the postStatus call is not made verifying that the handleSuccess wasn't called
        Mockito.verify(mockView, Mockito.times(0)).postStatus();
        // Makes sure that the error message passed in from the backgroundHandler is displayed
        Mockito.verify(mockView).displayMessage("Error message");
    }
}