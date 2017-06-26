package com.learnera.app.data;

/**
 * Created by Shankar on 23-04-2017.
 */

public class Announcement {
    private String announcementHead;
    // private String announcementDetails;
    private String mDate;

    public Announcement() {
    }

    public Announcement(String announcementHead, String announcementDetails, String mDate) {
        this.announcementHead = announcementHead;
        //this.announcementDetails = announcementDetails;
        this.mDate = mDate;
    }


    public String getAnnouncementHead() {
        return announcementHead;
    }

    public void setAnnouncementHead(String announcementHead) {
        this.announcementHead = announcementHead;
    }


    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
