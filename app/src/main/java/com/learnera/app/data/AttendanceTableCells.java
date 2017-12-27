package com.learnera.app.data;

/**
 * Created by Shankar on 18-10-2017.
 */

public class AttendanceTableCells {

    String mSubject;
    String color;

    public AttendanceTableCells() {
    }

    public AttendanceTableCells(String mSubject, String color) {
        this.mSubject = mSubject;
        this.color = color;
    }

    public String getmSubject() {
        return mSubject;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
