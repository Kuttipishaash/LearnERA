package com.learnera.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.learnera.app.data.Marks;
import com.learnera.app.data.MarksAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prejith on 8/8/2016.
 */
public class MarksActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static int countSemesters;
    static Intent i;
    protected ArrayList<String> semList;
    @BindView(R.id.spinner_marks_semesters)
    Spinner spinner1;
    @BindView(R.id.spinner_marks_category)
    Spinner spinner2;
    ArrayList<String> semListCode;
    ArrayList<String> subjectNames;
    ArrayList<String> subjectCodes;
    ArrayList<String> subjectLetters;
    ArrayList<String> subjectMarks;
    ArrayList<String> subjectMarksOutOf;
    ArrayList<String> examValues;
    ArrayList<String> examList;
    ArrayList<Marks> marksList = new ArrayList<Marks>();
    Document doc;
    Elements list;
    Connection.Response res;
    String finalFetchURL;
    private RecyclerView mRecyclerView;
    private MarksAdapter marksAdapter;
    private String markurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        ButterKnife.bind(this);
        respondToInternetStatus();
        if (isNetworkAvailable()) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }

    }

    //SPINNER SELECTION HANDLING
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.spinner_marks_semesters:
                JsoupAsyncTask2 jsoupAsyncTask2 = new JsoupAsyncTask2(semListCode.get(spinner1.getSelectedItemPosition()));
                jsoupAsyncTask2.execute();
                break;
            case R.id.spinner_marks_category:
                respondToInternetStatus();
                finalFetchURL = markurl + "?code=" + semListCode.get(spinner1.getSelectedItemPosition()) + "&E_ID=" + examValues.get(spinner2.getSelectedItemPosition());
                MarkAsyncTask marksAsyncTask = new MarkAsyncTask();
                marksAsyncTask.execute();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_marks, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_marks_help):
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MarksActivity.this);
        alert.setTitle(R.string.action_announcement_help)
                .setMessage(R.string.help_marks_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void dynamicSemList() {
        semList = new ArrayList<String>();
        for (int i = 0; i < countSemesters; i++) {
            semList.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
        //SPINNER 1
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                MarksActivity.this,
                android.R.layout.simple_spinner_item,
                semList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(MarksActivity.this);
    }

    private void dynamicExamList() {
        //SPINNER 2
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                MarksActivity.this,
                android.R.layout.simple_spinner_item,
                examList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(MarksActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        markurl = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Mark.asp";
        if (isNetworkAvailable()) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }
    }

    private void createList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_marks);
        marksAdapter = new MarksAdapter(marksList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(marksAdapter);
    }

    //TO CHECK INTERNET CONNECTION
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //TO RESPOND TO INTERNET CONNECTION PRESENCE
    public void respondToInternetStatus() {
        if (!isNetworkAvailable()) {
            i = new Intent(MarksActivity.this, NetworkNotAvailableActivity.class);
            startActivity(i);
        }
    }

    //FOR SPINNER 1 DYNAMIC
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                markurl = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Mark.asp";

                res = Jsoup.connect("https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login")
                        .data("user", "U1504045")
                        .data("pass", "15253")
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();
                doc = Jsoup.connect(markurl)
                        .cookies(res.cookies())
                        .get();
                list = doc.select("select[name=code]");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            semListCode = new ArrayList<String>();
            MarksActivity.countSemesters = 0;
            for (Element ls : list) {
                for (Element opt : ls.select("option")) {
                    semListCode.add(opt.text().toString());
                    MarksActivity.countSemesters += 1;
                }
            }
            dynamicSemList();
        }
    }

    //FOR SPINNER 2 DYNAMIC
    private class JsoupAsyncTask2 extends AsyncTask<Void, Void, Void> {

        String codes;

        public JsoupAsyncTask2(String code) {
            this.codes = code;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                doc = Jsoup.connect(markurl)
                        .cookies(res.cookies())
                        .data("code", codes)
                        .get();
                list = doc.select("select[name=E_ID]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            examValues = new ArrayList<String>();
            examList = new ArrayList<String>();
            for (Element ls : list) {
                for (Element opt : ls.select("option")) {
                    examValues.add(opt.val().toString());
                    examList.add(opt.text().toString());
                }
            }
            dynamicExamList();
        }
    }

    //FOR MARK FETCH
    private class MarkAsyncTask extends AsyncTask<Void, Void, Void> {

        Elements tables;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(finalFetchURL)
                        .cookies(res.cookies())
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Marks marks;
            String test = "", test2 = "";
            String subjectHeader[] = new String[3];
            int rownum = 0, colnum;
            tables = doc.select("table[width = 100%][align=Left][cellpadding=1]");
            subjectCodes = new ArrayList<String>();
            subjectLetters = new ArrayList<String>();
            subjectMarksOutOf = new ArrayList<String>();
            subjectMarks = new ArrayList<String>();
            subjectNames = new ArrayList<String>();
            marksList = new ArrayList<Marks>();
            for (Element table : tables) {
                Elements rows = table.select("tr");
                for (Element row : rows) {
                    rownum++;
                    Elements cells = row.select("td");
                    colnum = 0;
                    for (Element cell : cells) {
                        colnum++;
                        if (colnum <= 3) {
                            continue;
                        }
                        switch (rownum) {
                            case 1:
                                subjectHeader = cell.text().toString().split(" ");
                                subjectLetters.add(subjectHeader[0]);
                                subjectCodes.add(subjectHeader[1]);
                                subjectMarksOutOf.add(subjectHeader[2].substring(1));
                                break;
                            case 2:

                                if (cell.text().equals("\u00a0")) {
                                    subjectMarks.add("NA");
                                } else {
                                    subjectMarks.add(cell.text());
                                }
                                break;

                        }
                        //test = test + cell.text().toString() + "\n";
                    }
                }
            }
            tables = doc.select("table[width = 50%][align=Left][cellpadding=1]");
            rownum = 0;
            for (Element table : tables) {
                Elements rows = table.select("tr");
                for (Element row : rows) {
                    if (rownum == 0) {
                        rownum++;
                        continue;
                    }
                    Elements cells = row.select("td");
                    colnum = 0;
                    for (Element cell : cells) {
                        if (colnum <= 1) {
                            colnum++;
                            continue;
                        }

                        subjectNames.add(cell.text().replaceAll("^\\s+", ""));
                    }
                    rownum++;
                }
            }
            for (int i = 0; i < rownum - 1; i++) {

                marks = new Marks();
                marks.setmSubLetter(subjectLetters.get(i));
                marks.setmSubCode(subjectCodes.get(i));
                marks.setmSubName(subjectNames.get(i));
                marks.setmOutOf(subjectMarksOutOf.get(i));
                marks.setmSubMarks(subjectMarks.get(i));
                marksList.add(marks);
            }
            createList();
            subjectLetters.clear();
            subjectCodes.clear();
            subjectNames.clear();
            subjectMarksOutOf.clear();
            subjectMarks.clear();
        }
    }


}
