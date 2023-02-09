package com.example.newjumshare;

public class ongoingclass {

    String driver_id, id, driver_event, passenger_id, phone, event_id,car_id;
    private String name;
    String  key_driver;
    int count;
    String key,booked_data_key;

    public ongoingclass() {
    }

    public ongoingclass(String driver_id, String id ,String driver_event,String name,String passenger_id,String phone,String event_id, String key_driver,String car_id , int count, String key,String booked_data_key) {
        this.driver_id = driver_id;
        this.id = id;
        this.driver_event = driver_event;
        this.passenger_id = passenger_id;
        this.phone = phone;
        this.event_id = event_id;
        this.car_id = car_id;
        this.name = name;
        this.key_driver = key_driver;
        this.count = count;
        this.key = key;
        this.booked_data_key = booked_data_key;
    }

    public String getBooked_data_key() {
        return booked_data_key;
    }

    public void setBooked_data_key(String booked_data_key) {
        this.booked_data_key = booked_data_key;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver_event() {
        return driver_event;
    }

    public void setDriver_event(String driver_event) {
        this.driver_event = driver_event;
    }

    public String getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(String passenger_id) {
        this.passenger_id = passenger_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey_driver() {
        return key_driver;
    }

    public void setKey_driver(String key_driver) {
        this.key_driver = key_driver;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
