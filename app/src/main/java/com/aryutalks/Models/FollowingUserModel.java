package com.aryutalks.Models;

public class FollowingUserModel {
    private String userid;
    private String name;
    private String email;
    private String imageurl;

    public FollowingUserModel() {
    }

    public FollowingUserModel(String userid, String name, String email, String imageurl) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
