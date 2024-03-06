package com.aryutalks.Models;


public class FollowersModel {
    private String userid;
    private String username;
    private String name;
    private String imageurl;

    public FollowersModel() {
        // Default constructor required for Firebase
    }

    public FollowersModel(String userid, String name, String imageurl) {
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



    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
