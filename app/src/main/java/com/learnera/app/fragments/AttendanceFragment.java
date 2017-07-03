package com.learnera.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.learnera.app.data.AttendanceAdapter;
import com.learnera.app.LoginActivity;
import com.learnera.app.R;
import com.learnera.app.data.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prejith on 6/30/2017.
 */

public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    protected final static String loginURL = "https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login";
    protected final static String attendanceURL = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Leave.asp";

    protected int pos;
    protected String code;
    protected Document doc;
    protected Connection.Response res;
    private ProgressDialog mProgressDialog;
    private int count;
    private View view;
    private User user;

    SharedPreferences sharedPreferences;

    //For Recycler
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private List<String> mSubjectList;
    private List<String> mPercentageList;

    //For Spinner
    private ArrayList<String> mSemesters;
    private ArrayList<String> mSemesterList;
    protected ArrayAdapter<String> mSpinnerAdapter;
    protected Spinner spinner;

    public AttendanceFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();

        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREFERENCE_FILE, Context.MODE_PRIVATE);
        user.setUserName(sharedPreferences.getString("username", null));
        user.setPassword(sharedPreferences.getInt("password", 0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attendance, container, false);

        spinner =(Spinner) view.findViewById(R.id.spinner_attendance);
        spinner.setOnItemSelectedListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_attendance);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        initProgressDialog();

        mSemesterList = new ArrayList<>();
        mSubjectList = new ArrayList<>();
        mPercentageList = new ArrayList<>();

        new JSoupSpinnerTask().execute();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Extract semester code from spinner selection
        pos = spinner.getSelectedItemPosition();
        code = mSemesterList.get(pos);
        //Start populating recycler view
        new AttendanceFragment.JSoupAttendanceTask().execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(view.getContext());
        mProgressDialog.setMessage("Loading data from RSMS...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
    }

    private void extractAttendanceData() {
        Elements tables = doc.select("table [width=96%]");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements tds = rows.select("td");
                for (Element td : tds) {
                    String data = td.getElementsByTag("b").text();
                    if (data != "" && count > 1) {
                        mSubjectList.add(data);
                        mRecyclerAdapter.notifyItemInserted(mSubjectList.size());
                    }
                    count++;
                }
                for (Element td : tds) {
                    String data = td.select(":containsOwn(%)").text();
                    if (data != "") {
                        StringBuilder build = new StringBuilder(data);
                        String printer = build.delete(0, 2).toString();
                        printer = printer.replaceAll("\\s+", "");
                        mPercentageList.add(printer);
                    }
                }
                break;
            }
            break;
        }
    }

    private void extractSemesterList() {
        mSemesters = new ArrayList<>();
        Elements elements = doc.select("form");
        for (Element element : elements.select("option")) {
            mSemesterList.add(element.text());
            count++;
        }
        //Decrement count by 1 as extra empty input is being taken on scraping. This fixes the index 'count'
        count--;
        for (int i = 0; i <= count; i++) {
            mSemesters.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
    }

    private void setDefaultCountValue() {
        count = 0;
    }

    //To check internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void doWhenNoNetwork() {
        Fragment fragment = new NetworkNotAvailableFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_attendance, fragment);
        fragmentTransaction.commit();
    }

    //For populating spinner
    private class JSoupSpinnerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setDefaultCountValue();
            if(isNetworkAvailable()) {
                mProgressDialog.show();
            }
            else {
                doWhenNoNetwork();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mSpinnerAdapter = new ArrayAdapter<>(view.getContext(),
                    android.R.layout.simple_spinner_item,
                    mSemesters);
            mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mSpinnerAdapter);
            spinner.setSelection(count);
            mProgressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                res = Jsoup.connect(loginURL)
                        .data("user", user.getUserName())
                        .data("pass", String.valueOf(user.getPassword()))
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();

                doc = Jsoup.connect(attendanceURL)
                        .cookies(res.cookies())
                        .get();

                extractSemesterList();

            } catch (IOException e) {
                Log.e("ACTIVITY_ATTENDANCE", "Error initialising spinner");
            }
            return null;
        }
    }

    //For populating RecyclerView
    private class JSoupAttendanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (isNetworkAvailable()) {
                mProgressDialog.show();
            } else {
                doWhenNoNetwork();
            }
            mSubjectList = new ArrayList<>();
            mPercentageList = new ArrayList<>();

            //Clear both lists before populating recycler view by continuous spinner selections
            mPercentageList.clear();
            mSubjectList.clear();

            mRecyclerAdapter = new AttendanceAdapter(mSubjectList, mPercentageList);
            mRecyclerView.setAdapter(mRecyclerAdapter);

            setDefaultCountValue();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mProgressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                doc = Jsoup.connect(attendanceURL + "?code=" + code)
                        .cookies(res.cookies())
                        .get();

                extractAttendanceData();

            } catch (IOException e) {
                Log.e("ATTENDANCE_ACTIVITY", "Error retrieving data");
            }

            return null;
        }
    }


}
