package com.example.shankar.learnera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button mSignUp = (Button) findViewById(R.id.button_signup);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
            }
        });

        Button mLogIn = (Button) findViewById(R.id.button_login);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        Button mAnnouncement = (Button) findViewById(R.id.button_announcement);
        mAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AnnouncementsActivity.class));
            }
        });

        Button mAttendance = (Button) findViewById(R.id.button_attendance);
        mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AttendanceActivity.class));
            }
        });

        Button mActivityPoints = (Button)findViewById(R.id.button_activity_points);
        mActivityPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, ActivityPointsActivity.class));
            }
        });

        Button mMarks = (Button) findViewById(R.id.button_marks);
        mMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MarksActivity.class));
            }
        });

        Button mContacts = (Button) findViewById(R.id.button_contacts);
        mContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, ContactsActivity.class));
            }
        });

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted = preferences.getBoolean(getString(R.string.pref_previously_started), false);
                if(!previouslyStarted) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(getString(R.string.pref_previously_started), true);
                    editor.apply();
                    startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
                }
            }
        });
        mThread.start();
    }
}