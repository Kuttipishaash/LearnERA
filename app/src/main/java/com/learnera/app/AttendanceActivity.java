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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<String> mSubjectList;
    private List<String> mPercentageList;

    private ProgressDialog mProgressDialog;

    private int count = 1;

    protected Document doc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_attendance);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSubjectList = new ArrayList<>();
        mPercentageList = new ArrayList<>();

        mAdapter = new AttendanceAdapter(this, mSubjectList, mPercentageList);
        mRecyclerView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading data from RSMS...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        new JSoupAttendanceTask().execute();
        //extractDataForList();
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

    private class JSoupAttendanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("NETWORK", "Pre-execution");

            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("NETWORK", "Post-execution");

            mProgressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response res = Jsoup.connect("https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login")
                        .data("user", "U1504046")
                        .data("pass", "15180")
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();

                doc = Jsoup.connect("https://www.rajagiritech.ac.in/stud/KTU/Parent/Leave.asp?code=2017S4IT")
                        .cookies(res.cookies())
                        .get();

                Elements tables = doc.select("table [width=96%]");
                for (Element table : tables) {
                    Elements rows = table.select("tr");
                    for (Element row : rows) {
                        Elements tds = rows.select("td");
                        for (Element td : tds) {
                            String data = td.getElementsByTag("b").text();
                            if(data != "" && count > 2) {
                                mSubjectList.add(data);
                                mAdapter.notifyItemInserted(mSubjectList.size());
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
}