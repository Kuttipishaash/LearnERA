package com.learnera.app.utils;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learnera.app.R;
import com.learnera.app.activities.AnnouncementsActivity;
import com.learnera.app.activities.AttendanceActivity;
import com.learnera.app.activities.LoginActivity;
import com.learnera.app.activities.MarksActivity;
import com.learnera.app.activities.SyllabusActivity;
import com.learnera.app.activities.WelcomeActivity;
import com.learnera.app.fragments.AboutFragment;
import com.learnera.app.fragments.LoginFragment;
import com.learnera.app.fragments.NetworkNotAvailableFragment;
import com.learnera.app.models.AttendanceTableRow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by praji on 7/4/2017.
 */
public class Utils {

    //To check network connection
    public static boolean isNetworkAvailable(FragmentActivity fragmentActivity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) fragmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //To check internet connection. handler runs for 10000microseconds and then cancels the asynctask
    public static void testInternetConnectivity(final AsyncTask asyncTask, Handler handler) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (asyncTask.getStatus() == AsyncTask.Status.RUNNING)
                    asyncTask.cancel(true);
            }
        }, 15000);
    }

    // TODO: 8/10/2017 About us for announcements
    //Show About us fragment
    public static void showAbout(FragmentActivity fragmentActivity) {
        Fragment fragment = new AboutFragment();
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_out_right, R.anim.slide_in_right);

        if (fragmentActivity instanceof AttendanceActivity) {
            fragmentTransaction.addToBackStack(fragmentActivity.getString(R.string.label_fragment_attendance));
            fragmentTransaction.add(R.id.fragment_attendance, fragment);
        } else if (fragmentActivity instanceof MarksActivity) {
            fragmentTransaction.addToBackStack(fragmentActivity.getString(R.string.label_fragment_marks));
            fragmentTransaction.add(R.id.marks_fragment, fragment);
        } else if (fragmentActivity instanceof AnnouncementsActivity) {
            fragmentTransaction.addToBackStack(fragmentActivity.getString(R.string.label_fragment_announcements));
            fragmentTransaction.add(R.id.announcement_network, fragment);
        } else if (fragmentActivity instanceof WelcomeActivity) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.add(R.id.welcome_root, fragment);
        } else if (fragmentActivity instanceof SyllabusActivity) {
            fragmentTransaction.addToBackStack(fragmentActivity.getString(R.string.label_fragment_syllabus));
            fragmentTransaction.add(R.id.fragment_syllabus, fragment);
        }
        fragmentTransaction.commit();
    }

    //Used to open network not available fragment whenever network is not present
    public static void doWhenNoNetwork(FragmentActivity fragmentActivity) {
        Fragment fragment = new NetworkNotAvailableFragment();
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentActivity instanceof MarksActivity) {
            fragmentTransaction.replace(R.id.marks_fragment, fragment);
        } else if (fragmentActivity instanceof AttendanceActivity) {
            fragmentTransaction.replace(R.id.fragment_attendance, fragment);
        } else if (fragmentActivity instanceof LoginActivity) {
            fragmentTransaction.replace(R.id.fragment_login, fragment);
        } else if (fragmentActivity instanceof SyllabusActivity) {
            fragmentTransaction.replace(R.id.fragment_syllabus, fragment);
        } else if (fragmentActivity instanceof AnnouncementsActivity) {
            AnnouncementsActivity.network.setVisibility(View.VISIBLE);
            AnnouncementsActivity.mViewPager.setVisibility(View.GONE);
            AnnouncementsActivity.tabLayout.setVisibility(View.GONE);
        }
        fragmentTransaction.commit();
    }

    //open up login fragment when not logged in
    public static void doWhenNotLoggedIn(FragmentActivity fragmentActivity) {
        Fragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (fragmentActivity instanceof AttendanceActivity)
            fragmentTransaction.replace(R.id.fragment_attendance, fragment);
        else if (fragmentActivity instanceof MarksActivity)
            fragmentTransaction.replace(R.id.marks_fragment, fragment);
        fragmentTransaction.commit();
    }

    @TypeConverter
    public String fromStringList(List<String> stringList) {
        if (stringList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(stringList, type);
        return json;
    }

    @TypeConverter
    public List<String> toStringList(String incomingString) {
        if (incomingString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> stringList = gson.fromJson(incomingString, type);
        return stringList;
    }

    @TypeConverter
    public String fromTableList(ArrayList<AttendanceTableRow> tableRows) {
        if (tableRows == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AttendanceTableRow>>() {}.getType();
        String json = gson.toJson(tableRows, type);
        return json;
    }

    @TypeConverter
    public ArrayList<AttendanceTableRow> toTableList(String tableRowsString) {
        if (tableRowsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AttendanceTableRow>>() {}.getType();
        ArrayList<AttendanceTableRow> tableRowsList = gson.fromJson(tableRowsString, type);
        return tableRowsList;
    }
}
