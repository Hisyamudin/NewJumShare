package com.example.newjumshare;

public class reportClass {

    String currentUserId,Reporteduserid, reportitem,currentname,reportedname;

    public reportClass() {
    }

    public reportClass(String currentUserId, String reporteduserid, String reportitem, String currentname, String reportedname) {
        this.currentUserId = currentUserId;
        Reporteduserid = reporteduserid;
        this.reportitem = reportitem;
        this.currentname = currentname;
        this.reportedname = reportedname;
    }

    //    public reportClass(String currentUserId, String reporteduserid, String reportitem) {
//        this.currentUserId = currentUserId;
//        Reporteduserid = reporteduserid;
//        this.reportitem = reportitem;
//    }

    public String getCurrentname() {
        return currentname;
    }

    public void setCurrentname(String currentname) {
        this.currentname = currentname;
    }

    public String getReportedname() {
        return reportedname;
    }

    public void setReportedname(String reportedname) {
        this.reportedname = reportedname;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getReporteduserid() {
        return Reporteduserid;
    }

    public void setReporteduserid(String reporteduserid) {
        Reporteduserid = reporteduserid;
    }

    public String getReportitem() {
        return reportitem;
    }

    public void setReportitem(String reportitem) {
        this.reportitem = reportitem;
    }
}
