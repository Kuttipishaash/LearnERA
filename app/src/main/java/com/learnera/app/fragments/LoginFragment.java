package com.learnera.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.data.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import static com.learnera.app.LoginActivity.PREFERENCE_FILE;

/**
 * Created by praji on 7/4/2017.
 */

public class LoginFragment extends Fragment {

    EditText mUserName;
    EditText mPassword;
    Button mLogin;
    ProgressDialog mProgressDialog;

    User user;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Connection.Response res;
    View view;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);

        mLogin = (Button) view.findViewById(R.id.button_login);
        mUserName = (EditText) view.findViewById(R.id.et_uid);
        mPassword = (EditText) view.findViewById(R.id.et_password);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    boolean isLoginInfoCorrect() {
        if(res.url().toString().equals("https://www.rajagiritech.ac.in/stud/KTU/Parent/Home.asp"))
            return true;
        else {
            Toast.makeText(view.getContext(), "Incorrect username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(view.getContext());
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

            if (isLoginInfoCorrect()) {
                login();
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

    private void login() {
        sharedPreferences = getActivity().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("username",
                user.getUserName());
        editor.putInt("password",
                user.getPassword());
        editor.apply();

        Toast.makeText(view.getContext(), "Logged in with: " + user.getUserName(), Toast.LENGTH_SHORT)
                .show();
    }
}
