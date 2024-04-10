package edu.byu.cs.tweeter.client.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {

    private Status currentStatus;
    private User statusUser;
    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;
    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        statusUser = FakeData.getInstance().getFirstUser();
        currentStatus = new Status("post", statusUser, 1L, null, null);
        statusServiceSpy = Mockito.spy(new StatusService());
        Cache mockCache = Mockito.mock(Cache.class);
        Mockito.doReturn(new AuthToken("token","datetime")).when(mockCache).getCurrUserAuthToken();

        // Setup an observer for the FollowService
        observer = new StatusServiceObserver();

        Cache.setInstance(mockCache);

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

    private class StatusServiceObserver implements PagedObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;

        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = items;
            this.hasMorePages = hasMorePages;
            countDownLatch.countDown();
        }

        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }
    }


    @Test
    public void testGetStory_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.loadStory(statusUser,3, currentStatus, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = FakeData.getInstance().getFakeStatuses().subList(0, 3);
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        for (int i = 0; i < expectedStatuses.size(); i ++) {
            Assertions.assertEquals(expectedStatuses.get(i).getPost(), observer.getStory().get(i).getPost());
            Assertions.assertEquals(expectedStatuses.get(i).getUser(), observer.getStory().get(i).getUser());
            Assertions.assertEquals(expectedStatuses.get(i).getMentions(), observer.getStory().get(i).getMentions());
            Assertions.assertEquals(expectedStatuses.get(i).getUrls(), observer.getStory().get(i).getUrls());
        }
//        Assertions.assertEquals(expectedStatuses, observer.getStory());
        Assertions.assertTrue(observer.getHasMorePages());
    }

    @Test
    public void testGetStory_validRequest_loadsPosts() throws InterruptedException {
        statusServiceSpy.loadStory(statusUser,3, currentStatus, observer);
        awaitCountDownLatch();
        List<Status> story = observer.getStory();
        Assertions.assertTrue(story.size() > 0);
    }

    @Test
    public void testGetFollowees_invalidRequest_returnsNoFollowees() throws InterruptedException {
        statusServiceSpy.loadStory(null, 0, null,  observer);
        awaitCountDownLatch();

        Assertions.assertFalse(observer.isSuccess());
        Assertions.assertNotNull(observer.getMessage());
        Assertions.assertNull(observer.getStory());
        Assertions.assertFalse(observer.getHasMorePages());
    }
}
