package com.learnera.app.models;

/**
 * Created by Shankar on 24-06-2017.
 */

public class Marks {
    private String mOutOf;
    private String mSubName;
    private String mSubCode;
    private String mSubMarks;

    public Marks(String mOutOf, String mSubName, String mSubCode, String mSubMarks) {
        this.mOutOf = mOutOf;
        this.mSubName = mSubName;
        this.mSubCode = mSubCode;
        this.mSubMarks = mSubMarks;
    }

    public String getmOutOf() {

        return mOutOf;
    }

    public void setmOutOf(String mOutOf) {
        this.mOutOf = mOutOf;
    }

    public String getmSubName() {
        return mSubName;
    }

    public Marks() {
    }

    public void setmSubName(String mSubName) {
        this.mSubName = mSubName;
    }

    public String getmSubCode() {
        return mSubCode;
    }

    public void setmSubCode(String mSubCode) {
        this.mSubCode = mSubCode;
    }


    public String getmSubMarks() {
        return mSubMarks;
    }

    public void setmSubMarks(String mSubMarks) {
        this.mSubMarks = mSubMarks;
    }
}
