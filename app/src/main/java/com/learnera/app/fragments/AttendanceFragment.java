package com.learnera.app.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.Utils;
import com.learnera.app.data.AttendanceAdapter;
import com.learnera.app.data.AttendenceTableAdapter;
import com.learnera.app.data.AttendenceTableCells;
import com.learnera.app.data.AttendenceTableRow;
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

    final protected String sub = "Total Class";
    public ArrayList<AttendenceTableRow> tableRows;
    protected int pos;
    protected String code;
    protected Document doc;
    protected Connection.Response res;
    protected Pattern codePattern, singlePattern, threePattern;
    protected ArrayAdapter<String> mSpinnerAdapter;
    protected Spinner spinner;
    //For Attendence Table
    protected FloatingActionButton fab;
    protected View attendenceTable;
    protected AttendenceTableAdapter tableAdapter;
    protected ListView tableList;
    //To remove
    long startTime, stopTime, elapsedTime;
    long startTime2, stopTime2, elapsedTime2;
    private ProgressDialog mProgressDialog;
    private int count;
    private View view;
    private User user;
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

    public AttendanceFragment() {
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

        spinner = (Spinner) view.findViewById(R.id.spinner_attendance);
        spinner.setOnItemSelectedListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_attendance);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        initProgressDialog();

        //Semester list not included as semesters shouldn't be initalised in both the calls of initLists
        mSemesterList = new ArrayList<>();
        initLists();

        //initiate patterns for matching string
        initPatterns();

        final JSoupSpinnerTask jSoupSpinnerTask = new JSoupSpinnerTask();
        jSoupSpinnerTask.execute();

        //check for internet connectivity
        Handler handler = new Handler();
        Utils.testInternetConnectivity(jSoupSpinnerTask, handler);

        //For attendence details
        fab = (FloatingActionButton) view.findViewById(R.id.attendance_fab);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendenceDetails();
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Extract semester code from spinner selection
        pos = spinner.getSelectedItemPosition();
        code = mSemesterList.get(pos);

        //Start populating recycler view
        final JSoupAttendanceTask jSoupAttendanceTask = new JSoupAttendanceTask();
        jSoupAttendanceTask.execute();

        //check for internet connectivity
        Handler handler = new Handler();
        Utils.testInternetConnectivity(jSoupAttendanceTask, handler);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(view.getContext());
        mProgressDialog.setMessage("Loading data from RSMS...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
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

    private void initPatterns() {
        codePattern = Pattern.compile("\\w{2}\\d{3}");
        threePattern = Pattern.compile("[A-Z]{3}");
        singlePattern = Pattern.compile("[A-Z]");
    }

    private void extractAttendanceData() {
        startTime = System.currentTimeMillis();
        Elements tables = doc.select("table [width=96%]");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements tds = rows.select("td");
                for (Element td : tds.select(":containsOwn(Total Class)")) {
                    String data = td.text();

                    mTotalList.add(data.substring(data.toLowerCase().indexOf(sub.toLowerCase())).replaceAll("[^\\d]", ""));

                }
                for (Element td : tds) {
                    String data = td.getElementsByTag("b").text();
                    String htmlData = td.html();

                    Matcher matcher = codePattern.matcher(htmlData);
                    Matcher matcher2 = threePattern.matcher(htmlData);
                    Matcher matcher3 = singlePattern.matcher(htmlData);

                    if (count > 1) {
                        if (matcher.find()) {
                            mSubjectCodeList.add(matcher.group());
                        } else if (matcher2.find()) {
                            mSubjectCodeList.add(matcher2.group());
                        } else if (matcher3.find()) {
                            mSubjectCodeList.add(matcher3.group());
                        }

                        if (!data.equals("")) {
                            mSubjectList.add(data);
                            mRecyclerAdapter.notifyItemInserted(mSubjectList.size());
                        }
                    }
                    count++;
                }
                for (Element td : tds) {
                    String data = td.select(":containsOwn(%)").text();
                    String data2 = td.getElementsByTag("strong").text();

                    if (td.text().equals("-")) {
                        mPercentageList.add("-");
                        mMissedList.add("-");
                    } else {
                        if (!data.equals("")) {
                            //Remove first 2 characters as they are invalid
                            StringBuilder build = new StringBuilder(data);
                            String printer = build.delete(0, 2).toString();
                            printer = printer.replaceAll("\\s+", "");

                            //Add to list
                            mPercentageList.add(printer);
                        }

                        if (!data2.equals("")) {
                            mMissedList.add(data2);
                        }
                    }
                }
                break;
            }
            break;
        }
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        String x = elapsedTime + "";
        Toast.makeText(getActivity(), x, Toast.LENGTH_LONG).show();
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

    private void attendenceDetails() {
        //To show table view of attendence of the days on which the student was absent
        tableRows = new ArrayList<AttendenceTableRow>();

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_attendence_details);
        tableList = (ListView) dialog.findViewById(R.id.list_view_attendence_table);


        TextView dialogButton = (TextView) dialog.findViewById(R.id.attendence_dialog_dismiss);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Elements tables = doc.select("table [width=96%]");
        int count = 0;
        int rowNumber;
        int colNumber;
        for (Element table : tables) {
            if (count == 0) {
                count++;
                continue;
            }
            rowNumber = 0;
            Elements rows = table.select("tr");
            for (Element row : rows) {
                if (rowNumber <= 1) {
                    rowNumber++;
                    continue;
                }
                colNumber = 0;
                AttendenceTableRow attendenceTableRow = new AttendenceTableRow();
                Elements tds = row.select("td");
                for (Element td : tds) {
                    if (colNumber == 0) {
                        attendenceTableRow.setDate(td.text());
                    } else {
                        String subject = td.text();
                        String color = td.attr("bgcolor");
                        attendenceTableRow.addCell(new AttendenceTableCells(subject, color));
                    }
                    colNumber++;
                }
                rowNumber++;
                tableRows.add(attendenceTableRow);
            }
        }
        tableAdapter = new AttendenceTableAdapter(getActivity(), tableRows);
        tableList.setAdapter(tableAdapter);


        dialog.show();
    }

    //For populating spinner
    private class JSoupSpinnerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if (mProgressDialog != null) {
                mProgressDialog.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setDefaultCountValue();
            if (Utils.isNetworkAvailable(getActivity())) {
                mProgressDialog.show();
            } else {
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
        protected void onCancelled() {
            super.onCancelled();

            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (Utils.isNetworkAvailable(getActivity())) {
                mProgressDialog.show();
            } else {
                Utils.doWhenNoNetwork(getActivity());
            }

            initLists();

            //Clear lists before populating recycler view by continuous spinner selections
            clearLists();

            mRecyclerAdapter = new AttendanceAdapter(mSubjectList, mPercentageList, mSubjectCodeList, mTotalList, mMissedList);
            setDefaultCountValue();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            extractAttendanceData();
            mRecyclerView.setAdapter(mRecyclerAdapter);
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
