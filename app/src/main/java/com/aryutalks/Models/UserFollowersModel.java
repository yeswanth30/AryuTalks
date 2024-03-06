package com.aryutalks.Models;

public class UserFollowersModel {
    private String userId;
    private String username;
    private String imageUrl;

    public UserFollowersModel() {
        // Empty constructor needed for Firebase
    }

    public UserFollowersModel(String userId, String username, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
