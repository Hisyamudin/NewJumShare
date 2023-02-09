package com.example.newjumshare;

public class event_driver {
    private String fullNames;
    private String gender;
    private String pic;
    private String event_name;
    private int count;
    private String car_id;
    String id;

    public event_driver(String fullNames, String gender, String pic, String event_name, int count,String car_id, String id) {
        this.fullNames = fullNames;
        this.gender = gender;
        this.pic = pic;
        this.event_name = event_name;
        this.count = count;
        this.car_id=car_id;
        this.id = id;
    }

    public event_driver() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getPic() {
        return pic;
    }
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //public String getImageURL() {
    //    return imageURL;
    //}

    //public void setImageURL(String imageURL) {
    //    this.imageURL = imageURL;
    //}
}
