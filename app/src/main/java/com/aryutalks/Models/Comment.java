package com.aryutalks.Models;

public class Comment {
    private String userId;
    private String comment;

    public Comment(String userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }
}
