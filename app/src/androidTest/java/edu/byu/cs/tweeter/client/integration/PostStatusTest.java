package edu.byu.cs.tweeter.client.integration;

import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class PostStatusTest {

    private ServerFacade serverFacade;
    private AuthToken token = null;
    private User currentUser = null;
    private MainPresenter mainPresenter;
    private MainActivity mainActivity;
    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        // Login the User
        serverFacade = new ServerFacade();
        try {
            LoginRequest request = new LoginRequest("@CallMeDad", "ryan");
            LoginResponse response = serverFacade.login(request, "/login");
            token = response.getAuthToken();
            currentUser = response.getUser();
        } catch (IOException | TweeterRemoteException e) {
            e.printStackTrace();
        }
        Cache.getInstance().setCurrUserAuthToken(token);
        Cache.getInstance().setCurrUser(currentUser);

        mainActivity = Mockito.mock(MainActivity.class);
        mainPresenter = Mockito.spy(new MainPresenter(mainActivity));

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }


    @Test
    public void testPostStatus() throws IOException, TweeterRemoteException, InterruptedException {
        Answer<Void> answer = invocationOnMock -> {
            countDownLatch.countDown();
            return null;
        };

        doAnswer(answer).when(mainActivity).postStatus();
        mainPresenter.postStatus("Integration Test");
        awaitCountDownLatch();

        // Verify Toast was shown
        Mockito.verify(mainActivity).postStatus();
        // Verify that user story updated
        StoryRequest storyRequest = new StoryRequest(token, currentUser.getAlias(), 30, null);
        StoryResponse storyResponse = serverFacade.getStory(storyRequest, "/getstory");
        List<Status> story = storyResponse.getStory();
        Assertions.assertEquals(14, story.size());
        Assertions.assertFalse(storyResponse.getHasMorePages());
        Assertions.assertTrue(story.contains(mainPresenter.getNewStatus()));
    }
}
