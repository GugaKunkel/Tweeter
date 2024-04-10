package edu.byu.cs.tweeter.server.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;
import edu.byu.cs.tweeter.server.factory.Factory;
import edu.byu.cs.tweeter.util.Pair;

public class UserService {

    AuthTokenInterface authTokenDao;
    UserInterface userDao;
    ImageInterface imageDao;

    public UserService(Factory databaseFactory) {
        this.authTokenDao = databaseFactory.getAuthTokenDAO();
        this.userDao = databaseFactory.getUserDAO();
        this.imageDao = databaseFactory.getImageDAO();
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        try {
            Pair<User, String> result = userDao.loginUser(request.getUsername());

            if (result.getSecond().equals(hashPassword(request.getPassword()))) {
                AuthToken token = authTokenDao.generateAuthToken();
                return new LoginResponse(result.getFirst(), token);
            }
            else {
                return new LoginResponse("Incorrect password for that username");
            }
        }
        catch (Exception e) {
            return new LoginResponse("[Server Error] " + e.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if(request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }

        try {
            String image  = imageDao.saveImageToS3(request.getImage(), request.getUsername());
            String hashedPassword = hashPassword(request.getPassword());

            User result = userDao.registerUser(request.getUsername(), hashedPassword,
                    request.getFirstName(), request.getLastName(), image);

            AuthToken token = authTokenDao.generateAuthToken();
            return new RegisterResponse(result, token);
        }
        catch (Exception e) {
            return new RegisterResponse("[Server Error] " + e.getMessage());
        }
    }

    public LogoutResponse logout (LogoutRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.deleteAuthToken(request.getAuthToken());
            return new LogoutResponse();
        }
        catch (Exception e) {
            return new LogoutResponse("[Server Error] " + e.getMessage());
        }
    }

    public UserResponse getChosenUser(UserRequest request) {
        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            User user = userDao.getUser(request.getTargetUserAlias());
            return new UserResponse(user);
        }
        catch (Exception e) {
            return new UserResponse("[Server Error] " + e.getMessage());
        }
    }

    public CountResponse getFolloweeCount(CountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            int followeeCount = userDao.getFolloweeCount(request.getTargetUser());
            return new CountResponse(followeeCount);
        }
        catch (Exception e) {
            return new CountResponse("[Server Error] " + e.getMessage());
        }
    }

    public CountResponse getFollowerCount(CountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }

        try {
            authTokenDao.validateToken(request.getAuthToken());
            int followerCount = userDao.getFollowerCount(request.getTargetUser());
            return new CountResponse(followerCount);
        }
        catch (Exception e) {
            return new CountResponse("[Server Error] " + e.getMessage());
        }
    }

    private String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH";
    }
}
