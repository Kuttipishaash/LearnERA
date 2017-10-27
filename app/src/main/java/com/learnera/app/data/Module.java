package com.learnera.app.data;

/**
 * Created by Shankar on 16-10-2017.
 */

public class Module {
    String mModNum;
    String mModName;
    String mModDetails;

    public Module(String ModNum, String ModName, String ModDetails) {
        mModNum = ModNum;
        mModName = ModName;
        mModDetails = ModDetails;
    }

    public String getmModNum() {
        return mModNum;
    }

    public String getmModName() {
        return mModName;
    }

    public String getmModDetails() {
        return mModDetails;
    }
}
