package com.learnera.app;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.learnera.app.data.User;
import com.learnera.app.fragments.AttendanceFragment;
import com.learnera.app.fragments.MarksFragment;
import com.learnera.app.fragments.NetworkNotAvailableFragment;

/**
 * Created by Prejith on 7/20/2016.
 */

public class AttendanceActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        if(isNetworkAvailable()) {
            doWhenNetworkPresent();
        }
        else {
            doWhenNoNetwork();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_attendance, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_attendance_help):
                showHelp();
                return true;
            case (R.id.action_logout):
                User.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AttendanceActivity.this);
        alert.setTitle(R.string.action_announcement_help)
                .setMessage(R.string.help_attendance_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public void doWhenNoNetwork() {
        fragment = new NetworkNotAvailableFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_attendance, fragment);
        fragmentTransaction.commit();
    }

    public void doWhenNetworkPresent() {
        fragment = new AttendanceFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_attendance, fragment);
        fragmentTransaction.commit();
    }

    //TO CHECK INTERNET CONNECTION
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}