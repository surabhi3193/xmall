package com.mindinfo.xchangemall.xchangemall.beans;

public class Contact {
    private String id,
            friend_number, friend_name, friend_place, friend_qbid;

    private int friend_pic;

    public Contact() {
    }

    public Contact(String id, String friend_name, int friend_pic, String friend_number, String friend_place, String friend_qbid) {
        this.id = id;
        this.friend_pic = friend_pic;
        this.friend_number = friend_number;
        this.friend_name = friend_name;
        this.friend_place = friend_place;
        this.friend_qbid = friend_qbid;

    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_place() {
        return friend_place;
    }

    public void setFriend_place(String friend_place) {
        this.friend_place = friend_place;
    }

    public String getFriend_qbid() {
        return friend_qbid;
    }

    public void setFriend_qbid(String friend_qbid) {
        this.friend_qbid = friend_qbid;
    }



    public int getfriend_pic() {
        return friend_pic;
    }

    public void setfriend_pic(int friend_pic) {
        this.friend_pic = friend_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfriend_number() {
        return friend_number;
    }

    public void setfriend_number(String friend_number) {
        this.friend_number = friend_number;
    }

}
