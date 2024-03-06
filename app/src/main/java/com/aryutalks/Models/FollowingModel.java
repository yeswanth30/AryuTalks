package com.aryutalks.Models;


public class FollowingModel {
    private String userid;
    private String name;
    private String imageurl;

    public FollowingModel() {
        // Default constructor required for Firebase
    }

    public FollowingModel(String userid, String name, String imageurl) {
        this.userid = userid;
        this.name = name;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
