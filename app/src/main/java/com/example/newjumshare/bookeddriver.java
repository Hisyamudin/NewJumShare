package com.example.newjumshare;

public class bookeddriver {

    String id;
    String name;
    String gender;
    String phone;
    String event_id;
    String driver_id;
    String passenger_id;
    String driver_event;
    String  key_driver;
    String car_id;
    double latitude;
    double longitude;
    int count;

    public bookeddriver(String id, String name, String gender, String phone, String event_id, String driver_event, String driver_id, String passenger_id, String key_driver, String car_id,int count,double latitude,double longitude) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.event_id = event_id;
        this.driver_event = driver_event;
        this.driver_id = driver_id;
        this.passenger_id = passenger_id;
        this.key_driver=key_driver;
        this.car_id=car_id;
        this.count=count;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCar_id() {
        return car_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getKey_driver() {
        return key_driver;
    }

    public void setKey_driver(String key_driver) {
        this.key_driver = key_driver;
    }

    public bookeddriver() {
    }

    public String getDriver_event() {
        return driver_event;
    }

    public void setDriver_event(String driver_event) {
        this.driver_event = driver_event;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(String passenger_id) {
        this.passenger_id = passenger_id;
    }

}
