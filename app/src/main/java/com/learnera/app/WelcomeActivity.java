package com.learnera.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.learnera.app.data.User;

public class WelcomeActivity extends AppCompatActivity {

    ImageView mAnnouncement;
    ImageView mAttendance;
    ImageView mContacts;
    ImageView mLogout;
    ImageView mSyllabus;
    ImageView mMarks;

    SharedPreferences preferences;
    boolean previouslyStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        previouslyStarted = preferences.getBoolean(getString(R.string.pref_previously_started), false);

        if (!previouslyStarted) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.pref_previously_started), true);
            editor.apply();
            startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
        }
        else if(!User.isLoggedIn(this)) {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new  Intent(WelcomeActivity.this, LoginActivity.class));
        }

        initImageViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", null);

        String longText;
        if(user != null) {
            longText = "LOGGED IN AS : " + user;
            mLoginStatus.setText(longText);
        }
        else {
            longText = "NOT LOGGED IN TO RSMS";
            mLoginStatus.setText(longText);
        } */
    }

    public void initImageViews() {
        mAnnouncement = (ImageView) findViewById(R.id.drawable_announcement);
        mAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AnnouncementsActivity.class));
            }
        });

        mAttendance = (ImageView) findViewById(R.id.drawable_attendance);
        mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AttendanceActivity.class));
            }
        });

        mContacts = (ImageView) findViewById(R.id.drawable_contacts);
        mContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, ContactsActivity.class));
            }
        });

        mLogout = (ImageView) findViewById(R.id.drawable_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(WelcomeActivity.this);
            }
        });

        mSyllabus = (ImageView) findViewById(R.id.drawable_syllabus);
        mSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSyllabus();
            }
        });

        mMarks = (ImageView) findViewById(R.id.drawable_marks);
        mMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MarksActivity.class));
            }
        });
    }

    //intent to syllabus app
    void getSyllabus() {
        PackageManager pm = this.getPackageManager();
        Intent i = pm.getLaunchIntentForPackage("com.du.shankar.syllabus");
        if (i != null) {
            startActivity(i);
        } else {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=com.du.shankar.syllabus"));
            startActivity(viewIntent);
        }
    }
}