package com.learnera.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.learnera.app.data.Constants;
import com.learnera.app.data.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.button_login)
    Button mLogIn;
    @BindView(R.id.button_announcement)
    Button mAnnouncement;
    @BindView(R.id.button_attendance)
    Button mAttendance;
    @BindView(R.id.button_marks)
    Button mMarks;
    @BindView(R.id.button_contacts)
    Button mContacts;
    @BindView(R.id.login_status)
    TextView mLoginStatus;
    @BindView(R.id.button_syllabus)
    Button mSyllabus;

    private SharedPreferences sharedPreferences;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //Check if application is on first start
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted = preferences.getBoolean(getString(R.string.pref_previously_started), false);
                if (!previouslyStarted) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(getString(R.string.pref_previously_started), true);
                    editor.apply();
                    startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
                }
            }
        });
        mThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        }

    }

    @OnClick(R.id.button_login)
    void login() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.button_announcement)
    void announcement() {
        startActivity(new Intent(WelcomeActivity.this, AnnouncementsActivity.class));
    }

    @OnClick(R.id.button_attendance)
    void attendance() {
        startActivity(new Intent(WelcomeActivity.this, AttendanceActivity.class));
    }

    @OnClick(R.id.button_marks)
    void marks() {
        startActivity(new Intent(WelcomeActivity.this, MarksActivity.class));
    }

    @OnClick(R.id.button_contacts)
    void contacts() {
        startActivity(new Intent(WelcomeActivity.this, ContactsActivity.class));
    }

    //intent to syllabus app
    @OnClick(R.id.button_syllabus)
    void syllabus() {
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