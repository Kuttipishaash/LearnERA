package com.learnera.app.data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.learnera.app.AttendanceActivity;

/**
 * Created by praji on 7/2/2017.
 */
//// TODO: 7/5/2017 Change implementation to static
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

    public User getLoginInfo(FragmentActivity fragmentActivity) {

        User result = new User();

        SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        result.setUserName(sharedPreferences.getString("username", null));
        result.setPassword(sharedPreferences.getInt("password", 0));
        return result;
    }

    public static void eraseUserInfo(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void logout(final Activity activity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Logout")
                .setTitle("Are you sure you want to logout")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eraseUserInfo(activity);
                        Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
