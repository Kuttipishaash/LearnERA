package com.learnera.app.fragments;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.learnera.app.Utils;
import com.learnera.app.data.AttendanceAdapter;
import com.learnera.app.R;
import com.learnera.app.data.Constants;
import com.learnera.app.data.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Prejith on 6/30/2017.
 */

//// TODO: 7/31/2017 Code to be optimized and minor bugs to be fixed.

public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    protected int pos;
    protected String code;
    protected Document doc;
    protected Connection.Response res;
    private ProgressDialog mProgressDialog;
    private int count;
    private View view;
    private User user;
    protected Pattern codePattern, singlePattern, threePattern;
    final protected String sub = "Total Class";

    //For Recycler
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private List<String> mSubjectList;
    private List<String> mPercentageList;
    private List<String> mSubjectCodeList;
    private List<String> mMissedList;
    private List<String> mTotalList;

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

        user = user.getLoginInfo(getActivity());
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

        mSemesterList = new ArrayList<>(); //Semester list not included as semesters shouldn't be initalised in both the calls of initLists

        initLists();

        //initiate patterns for matching string
        codePattern = Pattern.compile("\\w{2}\\d{3}");
        threePattern = Pattern.compile("[A-Z]{3}");
        singlePattern = Pattern.compile("[A-Z]");

        if(Utils.isNetworkAvailable(getActivity())) {
            new JSoupSpinnerTask().execute();
        }
        else {
            Utils.doWhenNoNetwork(getActivity());
        }

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

    private void initLists() {
        mSubjectList = new ArrayList<>();
        mPercentageList = new ArrayList<>();
        mMissedList = new ArrayList<>();
        mTotalList = new ArrayList<>();
        mSubjectCodeList = new ArrayList<>();
    }

    private void clearLists() {
        mPercentageList.clear();
        mSubjectList.clear();
        mSubjectCodeList.clear();
        mMissedList.clear();
        mTotalList.clear();
    }

    private void extractAttendanceData() {

        Elements tables = doc.select("table [width=96%]");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements tds = rows.select("td");
                for(Element td: tds.select(":containsOwn(Total Class)")) {
                    String data = td.text();

                    mTotalList.add(data.substring(data.toLowerCase().indexOf(sub.toLowerCase())).replaceAll("[^\\d]", ""));

                }
                for (Element td : tds) {
                    String data = td.getElementsByTag("b").text();
                    String htmlData = td.html();

                    Matcher matcher = codePattern.matcher(htmlData);
                    Matcher matcher2 = threePattern.matcher(htmlData);
                    Matcher matcher3 = singlePattern.matcher(htmlData);

                    if(count > 1) {
                        if (matcher.find()) {
                            mSubjectCodeList.add(matcher.group());
                        } else if (matcher2.find()) {
                            mSubjectCodeList.add(matcher2.group());
                        } else if (matcher3.find()) {
                            mSubjectCodeList.add(matcher3.group());
                        }

                        if (data != "") {
                            mSubjectList.add(data);
                            mRecyclerAdapter.notifyItemInserted(mSubjectList.size());
                        }
                    }
                    count++;
                }
                for (Element td : tds) {
                    String data = td.select(":containsOwn(%)").text();
                    String data2 = td.getElementsByTag("strong").text();

                    if (data != "") {
                        //Remove first 2 characters as they are invalid
                        StringBuilder build = new StringBuilder(data);
                        String printer = build.delete(0, 2).toString();
                        printer = printer.replaceAll("\\s+", "");

                        //Add to list
                        mPercentageList.add(printer);
                    }

                    if(data2 != "") {
                        mMissedList.add(data2);
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

    //For populating spinner
    private class JSoupSpinnerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setDefaultCountValue();
            if(Utils.isOnline()) {
                mProgressDialog.show();
            }
            else {
                Utils.doWhenNoNetwork(getActivity());
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
                res = Jsoup.connect(Constants.loginURL)
                        .data("user", user.getUserName())
                        .data("pass", String.valueOf(user.getPassword()))
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();

                doc = Jsoup.connect(Constants.attendanceURL)
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

            if (Utils.isOnline()) {
                mProgressDialog.show();
            } else {
                Utils.doWhenNoNetwork(getActivity());
            }

            initLists();

            //Clear lists before populating recycler view by continuous spinner selections
            clearLists();

            mRecyclerAdapter = new AttendanceAdapter(mSubjectList, mPercentageList, mSubjectCodeList, mTotalList, mMissedList);
            mRecyclerView.setAdapter(mRecyclerAdapter);

            setDefaultCountValue();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            extractAttendanceData();

            mProgressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                doc = Jsoup.connect(Constants.attendanceURL + "?code=" + code)
                        .cookies(res.cookies())
                        .get();

            } catch (IOException e) {
                Log.e("ATTENDANCE_ACTIVITY", "Error retrieving data");
            }

            return null;
        }
    }


}
