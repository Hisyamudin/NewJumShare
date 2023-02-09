package com.example.newjumshare;

public class carClass {

    String cartype, carcolour,carId,userId,carplate, num_passenger;

    public String getNum_passenger() {
        return num_passenger;
    }

    public void setNum_passenger(String num_passenger) {
        this.num_passenger = num_passenger;
    }

    public carClass(String cartype, String carcolour, String carId, String userId, String carplate, String num_passenger) {
        this.cartype = cartype;
        this.carcolour = carcolour;
        this.carId = carId;
        this.userId = userId;
        this.carplate = carplate;
        this.num_passenger = num_passenger;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getCarcolour() {
        return carcolour;
    }

    public void setCarcolour(String carcolour) {
        this.carcolour = carcolour;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarplate() {
        return carplate;
    }

    public void setCarplate(String carplate) {
        this.carplate = carplate;
    }

    public carClass() {
    }
}
