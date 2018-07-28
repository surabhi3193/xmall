package com.mindinfo.xchangemall.xchangemall.beans;

import java.util.ArrayList;

public class NewsLocation {
    private String id;
    private String name;
    

    public NewsLocation() {
    }

    public NewsLocation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
