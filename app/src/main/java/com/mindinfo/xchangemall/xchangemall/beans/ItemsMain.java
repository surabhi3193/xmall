package com.mindinfo.xchangemall.xchangemall.beans;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class ItemsMain {

    private String item_image, item_price, item_title, item_subtitle, item_review_text,main_cat;
    private LatLng latng;
private List<String> imagearr = new ArrayList<>();
    public ItemsMain() {
    }

    public ItemsMain(String item_title) {
        this.item_title = item_title;
    }


    public ItemsMain(String item_image, String item_price, String item_title, String item_subtitle,
                     String item_review_text, String main_cat,LatLng latlng) {
        this.item_image = item_image;
        this.item_price = item_price;
        this.item_title = item_title;
        this.item_subtitle = item_subtitle;
        this.item_review_text = item_review_text;
        this.main_cat = main_cat;
        this.latng=latlng;
        this.latng=latlng;
    }

    public ItemsMain(String item_image, String item_price, String item_title, String main_cat) {
        this.item_image = item_image;
        this.item_price = item_price;
        this.item_title = item_title;
        this.main_cat = main_cat;
    }

    public String getMain_cat() {
        return main_cat;
    }

    public void setMain_cat(String main_cat) {
        this.main_cat = main_cat;
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

    public String getItem_subtitle() {
        return item_subtitle;
    }

    public void setItem_subtitle(String item_subtitle) {
        this.item_subtitle = item_subtitle;
    }

    public String getItem_review_text() {
        return item_review_text;
    }

    public void setItem_review_text(String item_review_text) {
        this.item_review_text = item_review_text;
    }

    public LatLng getLatng() {
        return latng;
    }

    public void setLatng(LatLng latng) {
        this.latng = latng;
    }
}
