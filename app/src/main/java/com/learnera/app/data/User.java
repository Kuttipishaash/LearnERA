package com.learnera.app.data;

/**
 * Created by praji on 7/2/2017.
 */

public class User {
    private String userName;
    private int password;

    public User(String userName, int password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

}
