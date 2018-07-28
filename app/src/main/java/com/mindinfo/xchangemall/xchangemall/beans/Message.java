package com.mindinfo.xchangemall.xchangemall.beans;

public class Message {
    private String id,message_text,
            date;

    private int sender_pic;
    private boolean isMine;
    public Message() {
    }

    public Message(String id, String message_text, int sender_pic, String date, boolean isMine) {
        this.id = id;
        this.sender_pic = sender_pic;
        this.date = date;
        this.message_text = message_text;
        this.isMine = isMine;

    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getmessage_text() {
        return message_text;
    }

    public void setmessage_text(String message_text) {
        this.message_text = message_text;
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


    
    
}
