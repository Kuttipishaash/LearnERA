package com.learnera.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Prejith on 7/20/2016.
 */

public class AttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //For Recycler
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private List<String> mSubjectList;
    private List<String> mPercentageList;

    //For Spinner
    private ArrayList<String> mSemesterList;
    protected ArrayAdapter<String> mSpinnerAdapter;
    protected Spinner spinner;

    private ProgressDialog mProgressDialog;

    private int count;
    protected String code;

    protected Document doc;
    protected Connection.Response res;

    protected final static String loginURL = "https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login";
    protected final static String attendanceURL = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Leave.asp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        spinner =(Spinner) findViewById(R.id.spinner_attendance);
        spinner.setOnItemSelectedListener(this);

        initProgressDialog();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_attendance);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSemesterList = new ArrayList<>();
        mSubjectList = new ArrayList<>();
        mPercentageList = new ArrayList<>();

        new JSoupSpinnerTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_attendance, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_attendance_help):
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Extract semester code from spinner selection
        code = spinner.getSelectedItem().toString();

        //Start populating recycler view
        new JSoupAttendanceTask().execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //For populating spinner
    private class JSoupSpinnerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setDefaultCountValue();
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mSpinnerAdapter = new ArrayAdapter<>(AttendanceActivity.this, android.R.layout.simple_spinner_item, mSemesterList);
            mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mSpinnerAdapter);
            spinner.setSelection(count);

            mProgressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                res = Jsoup.connect(loginURL)
                        .data("user", "U1504046")
                        .data("pass", "15180")
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

            //Clear both lists before populating recycler view by continuous spinner selections
            mPercentageList.clear();
            mSubjectList.clear();

            mRecyclerAdapter = new AttendanceAdapter(mSubjectList, mPercentageList);
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mProgressDialog.show();

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

    private void showHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AttendanceActivity.this);
        alert.setTitle(R.string.action_announcement_help)
                .setMessage(R.string.help_attendance_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void extractAttendanceData() {
        Elements tables = doc.select("table [width=96%]");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements tds = rows.select("td");
                for (Element td : tds) {
                    String data = td.getElementsByTag("b").text();
                    if(data != "" && count > 1) {
                        mSubjectList.add(data);
                        mRecyclerAdapter.notifyItemInserted(mSubjectList.size());
                    }
                    count++;
                }
                for (Element td : tds) {
                    String data = td.select(":containsOwn(%)").text();
                    if(data != "") {
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
        Elements elements = doc.select("form");
        for(Element element: elements.select("option")) {
            mSemesterList.add(element.text());
            count++;
        }
        //Decrement count by 1 as extra empty input is being taken on scraping. This fixes the index 'count'
        count--;
    }

    private void setDefaultCountValue() {
        count = 0;
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading data from RSMS...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
    }
}