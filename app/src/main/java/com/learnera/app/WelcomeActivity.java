package com.learnera.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.data.Constants;
import com.learnera.app.data.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WelcomeActivity extends AppCompatActivity {

    ImageView mAnnouncement;
    ImageView mAttendance;
    ImageView mLogout;
    ImageView mSyllabus;
    ImageView mMarks;
    ImageView mSeating;
    TextView mLoginStatus;
    TextView mAppName;
    AlertDialog.Builder mSeatingDialogAlert;

    SharedPreferences sharedPreferences;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = preferences.getBoolean(getString(R.string.pref_previously_started), false);

        if (!previouslyStarted) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.pref_previously_started), true);
            editor.apply();
            startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
        } else if (!User.isLoggedIn(this)) {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }

        initViews();

        //Set font
        Typeface appName = Typeface.createFromAsset(getAssets(), "fonts/Pasajero.otf");
        Typeface loginName = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-ExtraLight.ttf");
        mLoginStatus.setTypeface(loginName);
        mAppName.setTypeface(appName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Set logged in user status
        setUserStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    public void setUserStatus() {

        user = sharedPreferences.getString("user", null);

        String longText;
        if (user != null) {
            longText = "Logged in as : " + user;
            mLoginStatus.setText(longText);
        } else {
            longText = "Not logged into RSMS";
            mLoginStatus.setText(longText);
        }
    }

    public void initViews() {

        mLoginStatus = (TextView) findViewById(R.id.login_status);
        mAppName = (TextView) findViewById(R.id.app_name);

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
                startActivity(new Intent(WelcomeActivity.this, SyllabusActivity.class));
            }
        });

        mMarks = (ImageView) findViewById(R.id.drawable_marks);
        mMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MarksActivity.class));
            }
        });

        mSeating = (ImageView) findViewById(R.id.drawable_seating_plan);
        mSeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeatingDialogAlert.show();
            }
        });

        mSeatingDialogAlert = new AlertDialog.Builder(this);
        mSeatingDialogAlert.setTitle("Attention");
        mSeatingDialogAlert.setMessage("If you receive a message that the 'PDF cannot be opened', then it implies that the seating plan has not been uploaded." +
                " This is caused by the improper implementation used by RSMS to display the file");
        mSeatingDialogAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = User.getLoginInfo(WelcomeActivity.this);

                String completeUrl = Constants.seatPlanURL + user.getUserName().substring(1, 3) + user.getDept().toUpperCase() +
                        user.getUserName().substring(5) + ".pdf";

                //Launch chrome custom tab
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent intent = builder.build();
                builder.setToolbarColor(getResources().getColor(R.color.md_red_700));
                builder.setStartAnimations(WelcomeActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(WelcomeActivity.this, R.anim.slide_in_left, R.anim.slide_out_right);
                intent.launchUrl(WelcomeActivity.this, Uri.parse(completeUrl));
            }
        });
    }

}