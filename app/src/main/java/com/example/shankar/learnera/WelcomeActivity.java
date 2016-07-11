package com.example.shankar.learnera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button signUp=(Button)findViewById(R.id.signUpButton);
        signUp.setOnClickListener(this);
        Button logIn=(Button)findViewById(R.id.loginButton);
        logIn.setOnClickListener(this);
        Button announcements=(Button)findViewById(R.id.announcementsButton);
        announcements.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.signUpButton:
                startActivity(new Intent(WelcomeActivity.this,SignUpActivity.class));
                break;

            case R.id.loginButton:

                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                break;

            case R.id.announcementsButton:
                startActivity(new Intent(WelcomeActivity.this,AnnouncementsActivity.class));
                break;

            default:
                break;
        }

    }
}
