package com.aryutalks.Models;

import java.util.List;

public class PostModel {
    private String imageUrl;
    private String hashtag;
    private String userId;
    private String content;
    private String postId;
    private String heading;
    private boolean likedByCurrentUser;

    private  String currentUsername;
    private List<CommentModel> comments;





    public PostModel(String imageUrl, String hashtag, String userId, String content,String postId,String heading,boolean likedByCurrentUser, List<CommentModel> comments) {
        this.imageUrl = imageUrl;
        this.hashtag = hashtag;
        this.userId = userId;
        this.content = content;
        this.postId = postId;
        this.heading = heading;
        this.likedByCurrentUser = likedByCurrentUser;
        this.comments = comments;

    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }
}
