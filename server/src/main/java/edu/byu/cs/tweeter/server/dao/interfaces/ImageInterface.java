package edu.byu.cs.tweeter.server.dao.interfaces;

public interface ImageInterface {
    String saveImageToS3(String image, String username);
}
