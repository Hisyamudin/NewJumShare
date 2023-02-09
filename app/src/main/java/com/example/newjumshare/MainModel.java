package com.example.newjumshare;

public class MainModel {
    Integer eventpic;
    String stuevent;

    public MainModel (Integer eventpic, String stuevent){
        this.eventpic = eventpic;
        this.stuevent = stuevent;
    }

    public Integer getEventpic(){
        return eventpic;
    }

    public String getStuevent(){
        return stuevent;
    }
}
