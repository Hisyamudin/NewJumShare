package com.example.newjumshare;

public class driverClass {

    String fullNames, car, plate_number, CarColor, phoneNum, time, event_name, gender,pic,id,capacity,car_id,driver_id,date_data;
    int car_capacity, count;

    public String getDate_data() {
        return date_data;
    }

    public void setDate_data(String date_data) {
        this.date_data = date_data;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public driverClass(String fullNames, String car, String plate_number, String carColor, String phoneNum, String time, String event_name, String gender, String pic,String id,String capacity,String car_id,String driver_id, String date_data) {
        this.fullNames = fullNames;
        this.car = car;
        this.plate_number = plate_number;
        CarColor = carColor;
        this.phoneNum = phoneNum;
        this.time = time;
        this.event_name = event_name;
        this.gender = gender;
        this.pic = pic;
        this.id = id;
        this.capacity=capacity;
        this.car_id=car_id;
        this.driver_id=driver_id;
        this.date_data=date_data;
    }

    public driverClass() {
    }

    public int getCar_capacity() {
        return car_capacity;
    }

    public void setCar_capacity(int car_capacity) {
        this.car_capacity = car_capacity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public static UserHelperClass driver;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getCarColor() {
        return CarColor;
    }

    public void setCarColor(String carColor) {
        CarColor = carColor;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public static UserHelperClass getDriver() {
        return driver;
    }

    public static void setDriver(UserHelperClass driver) {
        driverClass.driver = driver;
    }
}
