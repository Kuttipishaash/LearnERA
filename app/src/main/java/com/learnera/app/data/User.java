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
    static private String userName;
    static private int password;
    static private String user;

    public User() {
    }

    public static void eraseUserInfo(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void logout(final Activity activity) {
        if(isLoggedIn(activity)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Logout")
                    .setMessage("Are you sure you want to logout")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eraseUserInfo(activity);
                            Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity, WelcomeActivity.class));
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            Toast.makeText(activity, "You are not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isLoggedIn(Activity activity) {
        User user = new User();

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        user.setUserName(sharedPreferences.getString("username", null));
        user.setPassword(sharedPreferences.getInt("password", 0));

        return user.getPassword() != 0 && user.getUserName() != null;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        User.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        User.userName = userName;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        User.password = password;
    }

    public User getLoginInfo(FragmentActivity fragmentActivity) {

        User result = new User();

        SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        result.setUserName(sharedPreferences.getString("user", null));
        result.setUserName(sharedPreferences.getString("username", null));
        result.setPassword(sharedPreferences.getInt("password", 0));
        return result;
    }
}
