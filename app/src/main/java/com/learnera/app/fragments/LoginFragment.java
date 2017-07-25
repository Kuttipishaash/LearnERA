package com.learnera.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learnera.app.AttendanceActivity;
import com.learnera.app.IntroActivity;
import com.learnera.app.LoginActivity;
import com.learnera.app.MarksActivity;
import com.learnera.app.Utils;
import com.learnera.app.R;
import com.learnera.app.WelcomeActivity;
import com.learnera.app.data.Constants;
import com.learnera.app.data.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Prejith on 7/4/2017.
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
    Elements u;
    View view;

    String name;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        getActivity().setTitle("RSMS Login");

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
                if(Utils.isNetworkAvailable(getActivity())) {
                   new JSoupLoginTask().execute();
                   user.setUserName(userName);
                   user.setPassword(Integer.parseInt(password));
                }
                else {
                    Utils.doWhenNoNetwork(getActivity());
                }
            }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    boolean isLoginInfoCorrect() {
        //Check if URL redirects to home page. Login redirects to home page only when entered username & password are correct
        if(res.url().toString().equals(Constants.homeURL))
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

    private void login() {
        //write username and password to sharedpreference file
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("user",
                user.getUser());
        editor.putString("username",
                user.getUserName());
        editor.putInt("password",
                user.getPassword());
        editor.apply();

        Toast.makeText(view.getContext(), "Logged in as: \n" + user.getUser(), Toast.LENGTH_SHORT)
                .show();

        //Go back to previous fragment after login
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (getActivity() instanceof LoginActivity || getActivity() instanceof IntroActivity) {
            Intent i = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(i);
        } else if (getActivity() instanceof MarksActivity) {
            Fragment fragment;
            getActivity().setTitle("Marks");
            fragment = new MarksFragment();
            fragmentTransaction.replace(R.id.marks_fragment, fragment);
            fragmentTransaction.commit();
        } else if (getActivity() instanceof AttendanceActivity) {
            Fragment fragment;
            getActivity().setTitle("Attendance");
            fragment = new AttendanceFragment();
            fragmentTransaction.replace(R.id.fragment_attendance, fragment);
            fragmentTransaction.commit();
        }
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

            name = u.text();
            user.setUser(name);
            mProgressDialog.dismiss();

            if (isLoginInfoCorrect()) {
                login();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                res = Jsoup.connect(Constants.loginURL)
                        .data("user", user.getUserName())
                        .data("pass", String.valueOf(user.getPassword()))
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();
                Document doc = Jsoup.connect(Constants.homeURL)
                        .cookies(res.cookies())
                        .get();
                u = doc.select("strong");
            } catch (IOException e) {
                Log.e("LOGIN_ACTIVITY", "Error checking login info");
            }
            return null;
        }
    }
}
