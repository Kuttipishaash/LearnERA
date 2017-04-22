package com.learnera.app.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shankar on 23-04-2017.
 */

public class Announcement {
    private String mAnnouncement;
    private String mAuthor;
    private Date mDate;

    public Announcement(String mAnnouncement, String mAuthor, Date mDate) {
        this.mAnnouncement = mAnnouncement;
        this.mAuthor = mAuthor;
        this.mDate = mDate;
    }

    public String getmAnnouncement() {
        return mAnnouncement;
    }

    public void setmAnnouncement(String mAnnouncement) {
        this.mAnnouncement = mAnnouncement;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmDate() {
        String date;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        date = df.format(mDate);
        return date;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }
}
