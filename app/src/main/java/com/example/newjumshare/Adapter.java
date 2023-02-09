package com.example.newjumshare;

public class Adapter {
    private String Event_name, Start_date, End_date, Start_time, End_time, Event_location, Event_description, Phone,pic;
    private int count;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getEnd_date() {
        return End_date;
    }

    public void setEnd_date(String end_date) {
        End_date = end_date;
    }

    public String getStart_time() {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getEnd_time() {
        return End_time;
    }

    public void setEnd_time(String end_time) {
        End_time = end_time;
    }

    public String getEvent_location() {
        return Event_location;
    }

    public void setEvent_location(String event_location) {
        Event_location = event_location;
    }

    public String getEvent_description() {
        return Event_description;
    }

    public void setEvent_description(String event_description) {
        Event_description = event_description;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Adapter(String event_name, String start_date, String End_date, String Start_time, String End_time, String Event_location, String Event_description, String Phone, int count,String pic) {
        Event_name = event_name;
        Start_date = start_date;
        this.count = count;
        this.pic = pic;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String start_date) {
        Start_date = start_date;
    }

    public Adapter() {
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEvent_name() {
        return Event_name;
    }

    public void setEvent_name(String event_name) {
        Event_name = event_name;
    }
}
