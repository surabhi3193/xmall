package com.mindinfo.xchangemall.xchangemall.beans;

import java.util.ArrayList;

public class categories {
    private String id;
    private String title;
    private ArrayList<String> selectedCAt;

    public categories() {
    }

    public categories(String id, String title) {
        this.id = id;
        this.title = title;
    }
    public categories(ArrayList<String> selectdCAt) {
        this.selectedCAt = selectdCAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public ArrayList<String> getSelectedCAt() {
        return selectedCAt;
    }

    public void setSelectedCAt(ArrayList<String> selectedCAt) {
        this.selectedCAt = selectedCAt;
    }
}
