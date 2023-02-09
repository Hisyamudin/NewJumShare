package com.example.newjumshare;

public class feedbackclass {

    String feedback, driverid, passengerid,feedbackid,currentname, feedbackname;
    double rating;

    public feedbackclass() {
    }

    public feedbackclass(String feedback, String driverid, String passengerid, double rating,String feedbackid, String currentname, String feedbackname) {
        this.feedback = feedback;
        this.driverid = driverid;
        this.passengerid = passengerid;
        this.rating = rating;
        this.feedbackid = feedbackid;
        this.currentname = currentname;
        this.feedbackname = feedbackname;
    }

    public String getCurrentname() {
        return currentname;
    }

    public void setCurrentname(String currentname) {
        this.currentname = currentname;
    }

    public String getFeedbackname() {
        return feedbackname;
    }

    public void setFeedbackname(String feedbackname) {
        this.feedbackname = feedbackname;
    }

    public String getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(String feedbackid) {
        this.feedbackid = feedbackid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getPassengerid() {
        return passengerid;
    }

    public void setPassengerid(String passengerid) {
        this.passengerid = passengerid;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
