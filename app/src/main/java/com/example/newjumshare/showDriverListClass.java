package com.example.newjumshare;

public class showDriverListClass {

    String event_name;
    String date;
    String time;
    String location;
    String number_of_passenger;
    String driver_id;
    String car_id;
    int count;
    String date_data, booked_data_key;

    public showDriverListClass(String event_name, String date, String time, String location, String number_of_passenger, String id, String driver_id,String car_id,String date_data, String booked_data_key) {
        this.event_name = event_name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.number_of_passenger = number_of_passenger;
        this.id = id;
        this.driver_id = driver_id;
        this.car_id =car_id;
        this.date_data =date_data;
        this.booked_data_key = booked_data_key;
    }

    public String getBooked_data_key() {
        return booked_data_key;
    }

    public void setBooked_data_key(String booked_data_key) {
        this.booked_data_key = booked_data_key;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber_of_passenger() {
        return number_of_passenger;
    }

    public void setNumber_of_passenger(String number_of_passenger) {
        this.number_of_passenger = number_of_passenger;
    }

    public String getDate_data() {
        return date_data;
    }

    public void setDate_data(String date_data) {
        this.date_data = date_data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public showDriverListClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
}
