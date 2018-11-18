package com.learnera.app.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.R;

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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
        }, 4500);
    }

    public void setFonts() {
        //Set font
        Typeface appName = Typeface.createFromAsset(getAssets(), "fonts/Pasajero.otf");
        mAppName.setTypeface(appName);
    }
}
