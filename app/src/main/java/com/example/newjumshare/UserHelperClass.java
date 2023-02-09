package com.example.newjumshare;

public class UserHelperClass {

    String name;
    String email;
    String phoneNo;
    String password;
    String faculty;
    String gender;
    String Occupation;
    int report_num;
    String pic;
    String uid;
    double rating;
    int pending, drivernum,passengernum;

    public static UserHelperClass currentUser;

    public UserHelperClass(){

    }

    public static String buildWelcomeMessage(){
        if(UserHelperClass.currentUser !=null){
            return new StringBuilder("Welcome")
                    .append(UserHelperClass.currentUser.getName()).toString();

        }
        else
            return "";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserHelperClass(String name, String email, String phoneNo, String password, String faculty, String gender, String occupation, int report_num, String pic, String uid,double rating, int pending,int drivernum, int passengernum) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.faculty = faculty;
        this.gender = gender;
        Occupation = occupation;
        this.report_num = report_num;
        this.pic = pic;
        this.uid = uid;
        this.pending = pending;
        this.drivernum = drivernum;
        this.passengernum= passengernum;
    }

    public int getDrivernum() {
        return drivernum;
    }

    public void setDrivernum(int drivernum) {
        this.drivernum = drivernum;
    }

    public int getPassengernum() {
        return passengernum;
    }

    public void setPassengernum(int passengernum) {
        this.passengernum = passengernum;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public int getReport_num() {
        return report_num;
    }

    public void setReport_num(int report_num) {
        this.report_num = report_num;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
