package com.aryutalks.Models;

public class CommentModel {
    private String userid;
    private String comment;
    private String name;
    private String imageurl;



    public CommentModel(String userid, String comment, String name,String imageurl) {
        this.userid = userid;
        this.comment = comment;
        this.name = name;
        this.imageurl = imageurl;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
