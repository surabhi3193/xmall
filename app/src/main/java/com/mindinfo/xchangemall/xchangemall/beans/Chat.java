package com.mindinfo.xchangemall.xchangemall.beans;

public class Chat {
    private String id,message_text,
            date,sender_name;

    private int sender_pic;

    public Chat() {
    }

    public Chat(String id, String message_text,String sender_name, int sender_pic, String date) {
        this.id = id;
        this.sender_pic = sender_pic;
        this.date = date;
        this.message_text = message_text;
        this.sender_name = sender_name;

    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
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
