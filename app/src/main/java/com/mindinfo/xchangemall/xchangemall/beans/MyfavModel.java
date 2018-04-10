package com.mindinfo.xchangemall.xchangemall.beans;



public class MyfavModel {

    private String item_image, item_price, item_title, item_description;

    public MyfavModel() {
    }

    public MyfavModel(String item_image, String item_title, String item_description, String item_price) {
        this.item_image = item_image;
        this.item_price = item_price;
        this.item_title = item_title;
        this.item_description = item_description;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }
}
