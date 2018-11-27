package com.learnera.app.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.learnera.app.R;
import com.learnera.app.models.User;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private TextView mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAppName = findViewById(R.id.splash_app_name);
        setFonts();
        User.eraseUserInfo(SplashActivity.this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                User.eraseUserInfo(SplashActivity.this);
                User.clearOfflineData(SplashActivity.this);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 4500);
    }

    public void setFonts() {
        //Set font
        Typeface appName = Typeface.createFromAsset(getAssets(), "fonts/pasajero.otf");
        mAppName.setTypeface(appName);
    }
}
