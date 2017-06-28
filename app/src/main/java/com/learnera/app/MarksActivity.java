package com.learnera.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.learnera.app.fragments.MarksFragment;
import com.learnera.app.fragments.NetworkNotAvailableFragment;

/**
 * Created by Prejith on 8/8/2016.
 */
public class MarksActivity extends AppCompatActivity {

    private static FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        if (isNetworkAvailable()) {
            fragment = new MarksFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.marks_fragment, fragment);
            fragmentTransaction.commit();
        } else {
            doWhenNoNetwork();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_marks_help):
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MarksActivity.this);
        alert.setTitle(R.string.action_announcement_help)
                .setMessage(R.string.help_marks_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    public void doWhenNoNetwork() {
        fragment = new NetworkNotAvailableFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.marks_fragment, fragment);
        fragmentTransaction.commit();
    }

    public void doWhenNetworkPresent() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.marks_fragment, fragment);
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
