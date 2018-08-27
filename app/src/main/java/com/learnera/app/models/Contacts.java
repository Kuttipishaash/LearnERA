package com.learnera.app.models;

/**
 * Created by praji on 2/16/2017.
 */

public class Contacts {
    private String name;
    private String mailId;
    private String phoneNo;
    private int photo;

    public Contacts(String name, String mailId, String phoneNo, int photo) {
        this.name = name;
        this.mailId = mailId;
        this.phoneNo = phoneNo;
        this.photo = photo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }
}
