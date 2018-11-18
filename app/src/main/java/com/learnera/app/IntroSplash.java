package com.learnera.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.learnera.app.activities.WelcomeActivity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class IntroSplash extends AppCompatActivity {
    private TextView mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_splash);
        mAppName = findViewById(R.id.splash_app_name);
        setFonts();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(IntroSplash.this, WelcomeActivity.class));
            }
        }, 10000);
    }

    public void setFonts() {
        //Set font
        Typeface appName = Typeface.createFromAsset(getAssets(), "fonts/Pasajero.otf");
        mAppName.setTypeface(appName);
    }
}
