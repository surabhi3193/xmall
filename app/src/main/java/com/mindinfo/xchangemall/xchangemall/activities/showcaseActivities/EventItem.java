package com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities;

/**
 * Created by Mind Info- Android on 21-Dec-17.
 */

public  class EventItem {
    private String  event_price, event_title, event_date, event_views,event_likes,event_chats;
 private int event_vdo_thumbnail;

    public EventItem() {
    }

    public EventItem(String item_title) {
        this.event_title = item_title;
    }


    public EventItem(int event_vdo_thumbnail, String event_price, String event_title, String event_date,
                     String event_views, String event_likes,String event_chats) {
     this.event_title=event_title;
     this.event_vdo_thumbnail=event_vdo_thumbnail;
     this.event_price=event_price;
     this.event_date=event_date;
     this.event_views=event_views;
     this.event_likes=event_likes;
     this.event_chats=event_chats;
    }

    public String getEvent_price() {
        return event_price;
    }

    public void setEvent_price(String event_price) {
        this.event_price = event_price;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_views() {
        return event_views;
    }

    public void setEvent_views(String event_views) {
        this.event_views = event_views;
    }

    public String getEvent_likes() {
        return event_likes;
    }

    public void setEvent_likes(String event_likes) {
        this.event_likes = event_likes;
    }

    public String getEvent_chats() {
        return event_chats;
    }

    public void setEvent_chats(String event_chats) {
        this.event_chats = event_chats;
    }

    public int getEvent_vdo_thumbnail() {
        return event_vdo_thumbnail;
    }

    public void setEvent_vdo_thumbnail(int event_vdo_thumbnail) {
        this.event_vdo_thumbnail = event_vdo_thumbnail;
    }
}
