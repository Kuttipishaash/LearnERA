package com.learnera.app.utils;

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

/**
 * Created by praji on 7/4/2017.
 */

public class Utils {

    //To check network connection
    public static boolean isNetworkAvailable(FragmentActivity fragmentActivity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) fragmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        }, 10000);
    }

    // TODO: 8/10/2017 About us for announcements
    //Show About us fragment
    public static void showAbout(FragmentActivity fragmentActivity) {
        Fragment fragment = new AboutFragment();
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_out_right, R.anim.slide_in_right);

        if (fragmentActivity instanceof AttendanceActivity) {
            fragmentTransaction.addToBackStack("attendance");
            fragmentTransaction.add(R.id.fragment_attendance, fragment);
        } else if (fragmentActivity instanceof MarksActivity) {
            fragmentTransaction.addToBackStack("marks");
            fragmentTransaction.add(R.id.marks_fragment, fragment);
        } else if (fragmentActivity instanceof AnnouncementsActivity) {
            fragmentTransaction.addToBackStack("announcement");
            fragmentTransaction.add(R.id.announcement_network, fragment);
        } else if (fragmentActivity instanceof WelcomeActivity) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.add(R.id.welcome_root, fragment);
        } else if (fragmentActivity instanceof SyllabusActivity) {
            fragmentTransaction.addToBackStack("syllabus");
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

    /*public static void fetchRemoteConfig(Context context) {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.firebase_remote_config_defaults);
        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           //TODO: Do something if successful
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            firebaseRemoteConfig.activateFetched();
                        } else {

                        }
                        displayWelcomeMessage();
                    }
                });
    }*/
}
