package com.learnera.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
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

    private String mUser;
    private String mEmail;
    private int mStatus;
    private SharedPreferences preferences;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        user = new User();

        sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_FILE, Context.MODE_PRIVATE);
        Toast.makeText(this, sharedPreferences.getString("username", "def"), Toast.LENGTH_SHORT).show();

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
        preferences = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        mStatus = preferences.getInt("STATUS", 0);
        //LOGIN IF NOT LOGGED IN
        //if (mStatus == 0)
        //    startActivity(new Intent(this, LoginActivity.class));
        //refresh();

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void refresh() {
        
        mLoginStatus.setText("LOGGED IN AS : " + mUser + "\nEmail : " + mEmail);
        mAttendance.setVisibility(View.VISIBLE);
        mAnnouncement.setVisibility(View.VISIBLE);
        mMarks.setVisibility(View.VISIBLE);
        mContacts.setVisibility(View.VISIBLE);

        mLogIn.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.button_login)
    void login() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        refresh();
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
}