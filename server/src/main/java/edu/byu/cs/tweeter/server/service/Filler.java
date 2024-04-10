package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserInterface;
import edu.byu.cs.tweeter.server.factory.Factory;

public class Filler {
    private final UserInterface userDao;
    private final FollowInterface followDao;

    public Filler(Factory databaseFactory) {
        this.followDao = databaseFactory.getFollowDAO();
        this.userDao = databaseFactory.getUserDAO();
    }

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@AllMight";

    public void fillDatabase() {
        List<String> followers = new ArrayList<>();
//        List<UserBean> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {
//            String firstName = "Hero ";
//            String lastName = "Fan" + i;
            String alias = "@HeroFan" + i;
//            String password = "password" + i;
//            String imageURL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
//
//            // Note that in this example, a UserDTO only has a name and an alias.
//            // The url for the profile image can be derived from the alias in this example
//            UserBean user = new UserBean();
//            user.setAlias(alias);
//            user.setFirst_name(firstName);
//            user.setLast_name(lastName);
//            user.setImageUrl(imageURL);
//            user.setPassword(password);
//            user.setFollowerCount(0);
//            user.setFolloweeCount(1);
//            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }
        if (followers.size() > 0) {
            followDao.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }
}