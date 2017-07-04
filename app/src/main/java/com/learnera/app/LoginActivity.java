package com.learnera.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learnera.app.data.User;
import com.learnera.app.fragments.AttendanceFragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**  Use show password toggle on password field. Insert fragment to login in intro screen. Logout option in
 all screens. Go back to last activity on pressing login and login successful
 */
public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCE_FILE="user_preferences";

    EditText mUserName;
    EditText mPassword;
    Button mLogin;
    ProgressDialog mProgressDialog;

    User user;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Connection.Response res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = (Button) findViewById(R.id.button_login);
        mUserName = (EditText) findViewById(R.id.et_uid);
        mPassword = (EditText) findViewById(R.id.et_password);

        user = new User();

        initProgressDialog();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = mUserName.getText().toString();
                String password = mPassword.getText().toString();

                if(TextUtils.isEmpty(userName)) {
                    mUserName.setError("Username cannot be empty");
                }
                else if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password cannot be empty");
                }
                else {
                    new JSoupLoginTask().execute();
                    user.setUserName(userName);
                    user.setPassword(Integer.parseInt(password));
                }
            }
        });
    }

    boolean isLoginSuccessful() {
        if(res.url().toString().equals("https://www.rajagiritech.ac.in/stud/KTU/Parent/Home.asp"))
            return true;
        else {
            Toast.makeText(this, "Incorrect username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Checking login information...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
    }

    private class JSoupLoginTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mProgressDialog.dismiss();

            if (isLoginSuccessful()) {
                sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("username",
                        user.getUserName());
                editor.putInt("password",
                        user.getPassword());
                editor.apply();

                Toast.makeText(LoginActivity.this, "Logged in with: " + user.getUserName(), Toast.LENGTH_SHORT)
                        .show();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                res = Jsoup.connect("https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login")
                        .data("user", user.getUserName())
                        .data("pass", String.valueOf(user.getPassword()))
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                Log.e("LOGIN_ACTIVITY", "Error checking login info");
            }
            return null;
        }
    }
}
