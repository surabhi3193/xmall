package com.mindinfo.xchangemall.xchangemall.other;

/**
 * Created by Mind Info- Android on 19-Dec-17.
 */

public class Data
{
    private String name;
    private int imagePath;
    private String gender;
    private String occupation;
    private String age;

    public Data(int imagePath, String name,String gender, String occupation,String age) {
        this.imagePath = imagePath;
        this.name = name;
        this.gender = gender;
        this.occupation = occupation;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getImagePath() {
        return imagePath;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}
