package com.mindinfo.xchangemall.xchangemall.beans;

public class Call {
    private String id,
            date,sender_name;

    private int sender_pic;
    private boolean isAudio;

    public Call() {
    }

    public Call(String id, String sender_name, int sender_pic, String date, boolean isAudio) {
        this.id = id;
        this.sender_pic = sender_pic;
        this.date = date;
        this.isAudio = isAudio;
        this.sender_name = sender_name;

    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }


    public int getsender_pic() {
        return sender_pic;
    }

    public void setsender_pic(int sender_pic) {
        this.sender_pic = sender_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }


    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
    }
}
