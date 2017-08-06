package com.learnera.app.data;

/**
 * Created by Shankar on 06-08-2017.
 */

public class AnnouncementRSET {
    private String announcement;
    private String date;

    public AnnouncementRSET(String announcement, String date) {
        this.announcement = announcement;
        this.date = date;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
