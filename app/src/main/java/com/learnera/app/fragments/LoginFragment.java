package com.learnera.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    TextInputLayout mUserInput;
    TextInputLayout mPassInput;
    Button mLogin;
    ProgressDialog mProgressDialog;
    InputMethodManager inputMethodManager;

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
        setHasOptionsMenu(true);

        initView();

        initProgressDialog();

        getActivity().setTitle("RSMS Login");

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String userName = mUserName.getText().toString();
            String password = mPassword.getText().toString();

            if(TextUtils.isEmpty(userName)) {
                //set error on username field
                mUserInput.setError("Username cannot be empty");

                //request focus for username field
                mUserInput.requestFocus();

                //show keyboard on emtpy username entered
                inputMethodManager.showSoftInput(mUserName, InputMethodManager.SHOW_IMPLICIT);
            }
            else if(TextUtils.isEmpty(password)) {
                mPassInput.setError("Password cannot be empty");
                mPassInput.requestFocus();
                inputMethodManager.showSoftInput(mPassword, InputMethodManager.SHOW_IMPLICIT);
            }
            else {
                if(Utils.isNetworkAvailable(getActivity())) {

                    JSoupLoginTask jSoupLoginTask = new JSoupLoginTask();
                    jSoupLoginTask.execute();

                    Handler handler = new Handler();
                    Utils.testInternetConnectivity(jSoupLoginTask, handler);

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

    private boolean isLoginInfoCorrect() {
        //Check if URL redirects to home page. Login redirects to home page only when entered username & password are correct
        if(res.url().toString().equals(Constants.homeURL))
            return true;
        else {
            Toast.makeText(view.getContext(), "Incorrect username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initView() {
        mLogin = (Button) view.findViewById(R.id.button_login);
        mUserName = (EditText) view.findViewById(R.id.et_uid);
        mPassword = (EditText) view.findViewById(R.id.et_password);
        mUserInput = (TextInputLayout) view.findViewById(R.id.text_input_username_field);
        mPassInput = (TextInputLayout) view.findViewById(R.id.text_input_password_field);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(view.getContext());
        mProgressDialog.setMessage("Checking login information...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
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

        startActivity(new Intent(getActivity(), WelcomeActivity.class));
    }

    private class JSoupLoginTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if(mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
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
