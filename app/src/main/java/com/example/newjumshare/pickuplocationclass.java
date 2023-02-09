package com.example.newjumshare;

public class pickuplocationclass {
    String name, picture;

    public pickuplocationclass(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public pickuplocationclass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
