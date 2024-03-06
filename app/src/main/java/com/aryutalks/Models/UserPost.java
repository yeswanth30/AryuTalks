package com.aryutalks.Models;

public class UserPost {
    private String postId;
    private String imageUrl;

    public UserPost() {
        // Default constructor required for Firebase
    }

    public UserPost(String postId, String imageUrl) {
        this.postId = postId;
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
