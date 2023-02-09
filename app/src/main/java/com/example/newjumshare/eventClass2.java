package com.example.newjumshare;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class eventClass2 implements Serializable {

    String End_date,End_time,Event_description, Event_location, Event_name,Phone, Start_date,Start_time,type;

    public eventClass2() {
    }

    public eventClass2(String End_date, String End_time, String Event_description, String Event_location, String Event_name, String Phone, String Start_date, String Start_time, String type) {
        this.End_date = End_date;
        this.End_time = End_time;
        this.Event_description = Event_description;
        this.Event_location = Event_location;
        this.Event_name = Event_name;
        this.Phone = Phone;
        this.Start_date = Start_date;
        this.Start_time = Start_time;
        this.type = type;
    }

    @PropertyName("End_date")
    public String getEnd_date() {
        return End_date;
    }

    @PropertyName("End_date")
    public void setEnd_date(String End_date) {
        this.End_date = End_date;
    }

    @PropertyName("End_time")
    public String getEnd_time() {
        return End_time;
    }

    @PropertyName("End_time")
    public void setEnd_time(String End_time) {
        this.End_time = End_time;
    }

    @PropertyName("Event_description")
    public String getEvent_description() {
        return Event_description;
    }

    @PropertyName("Event_description")
    public void setEvent_description(String Event_description) {
        this.Event_description = Event_description;
    }

    @PropertyName("Event_location")
    public String getEvent_location() {
        return Event_location;
    }

    @PropertyName("Event_location")
    public void setEvent_location(String Event_location) {
        this.Event_location = Event_location;
    }

    @PropertyName("Event_name")
    public String getEvent_name() {
        return Event_name;
    }

    @PropertyName("Event_name")
    public void setEvent_name(String Event_name) {
        this.Event_name = Event_name;
    }

    @PropertyName("Phone")
    public String getPhone() {
        return Phone;
    }

    @PropertyName("Phone")
    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    @PropertyName("Start_date")
    public String getStart_date() {
        return Start_date;
    }

    @PropertyName("Start_date")
    public void setStart_date(String Start_date) {
        this.Start_date = Start_date;
    }

    @PropertyName("Start_time")
    public String getStart_time() {
        return Start_time;
    }

    @PropertyName("Start_time")
    public void setStart_time(String Start_time) {
        this.Start_time = Start_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
