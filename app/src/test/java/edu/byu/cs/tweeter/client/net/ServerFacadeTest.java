package edu.byu.cs.tweeter.client.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {

    private static final String REGISTER_PATH = "/register";

    private static final String GET_FOLLOWERS_PATH = "/getfollowers";

    private static final String GET_FOLLOWERS_COUNT_PATH = "/getfollowercount";
    ServerFacade serverFacade = new ServerFacade();

    @Test
    public void register_Success(){
        RegisterResponse registerResponse = null;
        try {
            RegisterRequest registerRequest = new RegisterRequest(
                    "newUser", "12345", "New", "User", "image");
            registerResponse = serverFacade.register(registerRequest, REGISTER_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert registerResponse != null;
        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertEquals(registerResponse.getUser(), getFakeData().getFirstUser());
        Assertions.assertEquals(registerResponse.getAuthToken().getClass(), AuthToken.class);
    }

    @Test
    public void getFollowers_Success(){
        FollowersResponse followersResponse = null;
        try {
            FollowersRequest followersRequest = new FollowersRequest(
                    getFakeData().getAuthToken(), "@allen", 5, null);
            followersResponse = serverFacade.getFollowers(followersRequest, GET_FOLLOWERS_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert followersResponse != null;

        Assertions.assertTrue(followersResponse.isSuccess());
        Assertions.assertEquals(followersResponse.getFollowers(),
                getFakeData().getPageOfUsers(getFakeData().getFirstUser(), 5, null).getFirst());
        Assertions.assertTrue(followersResponse.getHasMorePages());
    }

    @Test
    public void getFollowersCount_Success(){
        CountResponse followerCountResponse = null;
        try {
            CountRequest followerCountRequest = new CountRequest(getFakeData().getFirstUser(), getFakeData().getAuthToken());
            followerCountResponse = serverFacade.getCount(followerCountRequest, GET_FOLLOWERS_COUNT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert followerCountResponse != null;
        Assertions.assertTrue(followerCountResponse.isSuccess());
        Assertions.assertEquals(followerCountResponse.getCount(), getFakeData().getFakeUsers().size());
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
