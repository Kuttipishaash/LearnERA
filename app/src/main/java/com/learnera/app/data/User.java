package com.learnera.app.data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.learnera.app.WelcomeActivity;

/**
 * Created by praji on 7/2/2017.
 */
public class User {
    private String userName; //UID
    private int password;
    private String user; //Name of the user
    private String sem;
    private String dept;

    public User() {
    }

    public static void eraseUserInfo(Activity activity) {
        //removes all contents of sharedpreference file
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void logout(final Activity activity) {
        if (isLoggedIn(activity)) {
            //shows confirmation dialog for logout operation
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Logout")
                    .setMessage("Are you sure you want to logout")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if user confirms logout erases user data and shows logout success message
                            eraseUserInfo(activity);
                            Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity, WelcomeActivity.class));
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            //if not logged in alerts user about it
            Toast.makeText(activity, "You are not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isLoggedIn(Activity activity) {
        User user = new User();
        //checks whether username and password in sharedpreferences file is null. If null user is not logged in else he is logged in
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        user.setUserName(sharedPreferences.getString("username", null));
        user.setPassword(sharedPreferences.getInt("password", 0));
        return user.getPassword() != 0 && user.getUserName() != null;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public User getLoginInfo(FragmentActivity fragmentActivity) {

        User result = new User();
        //loads ALL contents of sharedpreferences file to a new User object and returns it.
        SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        result.setUser(sharedPreferences.getString("user", null));
        result.setUserName(sharedPreferences.getString("username", null));
        result.setPassword(sharedPreferences.getInt("password", 0));
        result.setSem(sharedPreferences.getString("sem", null));
        result.setDept(sharedPreferences.getString("dept", null));
        return result;
    }

    public int getattendanceCutoff(FragmentActivity fragmentActivity) {
        SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        int attendanceCutoff = sharedPreferences.getInt("attendanceCutoff", 0);
        return attendanceCutoff;
    }

    public void setAttendenceCutoff(FragmentActivity fragmentActivity, int attendanceCutoff) {
        SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("attendanceCutoff",
                attendanceCutoff);
        editor.commit();

    }
}
