package com.example.newjumshare;

public class Ongoingrideclass {

    String passengerkey, driverkey,carkey, eventkey,destination, passenger1, passenger2, passenger3, passsenger4, passenger5, passenger6, location1, location2,location3,location4,location5,location6;
    double currentlat,currentlng;
    boolean test;
    int count;

    public Ongoingrideclass() {
    }

    public Ongoingrideclass(String passengerkey, String driverkey, String carkey, String eventkey, String destination, int count, String passenger1, String passenger2, String passenger3, String passsenger4, String passenger5, String passenger6, String location1, String location2, String location3, String location4, String location5, String location6, double currentlat, double currentlng, boolean test) {
        this.passengerkey = passengerkey;
        this.driverkey = driverkey;
        this.carkey = carkey;
        this.eventkey = eventkey;
        this.passenger1 = passenger1;
        this.passenger2 = passenger2;
        this.passenger3 = passenger3;
        this.passsenger4 = passsenger4;
        this.passenger5 = passenger5;
        this.passenger6 = passenger6;
        this.location1 = location1;
        this.location2 = location2;
        this.location3 = location3;
        this.location4 = location4;
        this.location5 = location5;
        this.location6 = location6;
        this.currentlat = currentlat;
        this.currentlng = currentlng;
        this.test = test;
        this.destination = destination;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPassengerkey() {
        return passengerkey;
    }

    public void setPassengerkey(String passengerkey) {
        this.passengerkey = passengerkey;
    }

    public String getDriverkey() {
        return driverkey;
    }

    public void setDriverkey(String driverkey) {
        this.driverkey = driverkey;
    }

    public String getCarkey() {
        return carkey;
    }

    public void setCarkey(String carkey) {
        this.carkey = carkey;
    }

    public String getEventkey() {
        return eventkey;
    }

    public void setEventkey(String eventkey) {
        this.eventkey = eventkey;
    }

    public String getPassenger1() {
        return passenger1;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public String getPassenger3() {
        return passenger3;
    }

    public void setPassenger3(String passenger3) {
        this.passenger3 = passenger3;
    }

    public String getPasssenger4() {
        return passsenger4;
    }

    public void setPasssenger4(String passsenger4) {
        this.passsenger4 = passsenger4;
    }

    public String getPassenger5() {
        return passenger5;
    }

    public void setPassenger5(String passenger5) {
        this.passenger5 = passenger5;
    }

    public String getPassenger6() {
        return passenger6;
    }

    public void setPassenger6(String passenger6) {
        this.passenger6 = passenger6;
    }

    public String getLocation1() {
        return location1;
    }

    public void setLocation1(String location1) {
        this.location1 = location1;
    }

    public String getLocation2() {
        return location2;
    }

    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    public String getLocation3() {
        return location3;
    }

    public void setLocation3(String location3) {
        this.location3 = location3;
    }

    public String getLocation4() {
        return location4;
    }

    public void setLocation4(String location4) {
        this.location4 = location4;
    }

    public String getLocation5() {
        return location5;
    }

    public void setLocation5(String location5) {
        this.location5 = location5;
    }

    public String getLocation6() {
        return location6;
    }

    public void setLocation6(String location6) {
        this.location6 = location6;
    }

    public double getCurrentlat() {
        return currentlat;
    }

    public void setCurrentlat(double currentlat) {
        this.currentlat = currentlat;
    }

    public double getCurrentlng() {
        return currentlng;
    }

    public void setCurrentlng(double currentlng) {
        this.currentlng = currentlng;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
