package com.learnera.app.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.learnera.app.Animations.MyBounceInterpolator;
import com.learnera.app.R;
import com.learnera.app.Utils;
import com.learnera.app.data.AttendanceAdapter;
import com.learnera.app.data.AttendanceTableAdapter;
import com.learnera.app.data.AttendanceTableCells;
import com.learnera.app.data.AttendanceTableRow;
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
    public ArrayList<AttendanceTableRow> tableRows;
    protected int pos;
    protected String code;
    protected Document doc;
    protected Connection.Response res;
    protected Pattern codePattern, singlePattern, threePattern;
    protected ArrayAdapter<String> mSpinnerAdapter;
    protected Spinner spinner;
    //For attendance Table
    protected FloatingActionButton fab;
    protected AttendanceTableAdapter tableAdapter;
    protected ListView tableList;
    protected TextView noDataAttendanceTableTextView;
    JSoupAttendanceTask jSoupAttendanceTask;
    JSoupSpinnerTask jSoupSpinnerTask;
    Dialog dialog;

    //Animations
    Animation fadeInAnimation;
    Animation fadeOutAnimation;
    //To remove
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

    private List<String> mDutyAttendenceList;   //For duty attendence count for each subject

    //For Spinner
    private ArrayList<String> mSemesters;
    private ArrayList<String> mSemesterList;
    //For setting cutoff percentage
    private RadioGroup attendancePercentSelector;
    //For enabling/disabling on duty
    private RadioGroup dutyEnablerSelector;

    public AttendanceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getLoginInfo(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attendance, container, false);

        spinner = view.findViewById(R.id.spinner_attendance);
        spinner.setOnItemSelectedListener(this);

        fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        mRecyclerView = view.findViewById(R.id.recycler_view_attendance);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        initProgressDialog();

        //Semester list not included as semesters shouldn't be initalised in both the calls of initLists
        mSemesterList = new ArrayList<>();
        initLists();

        //initiate patterns for matching string
        initPatterns();

        jSoupSpinnerTask = new JSoupSpinnerTask();
        jSoupSpinnerTask.execute();

        //check for internet connectivity
        Handler handler = new Handler();
        Utils.testInternetConnectivity(jSoupSpinnerTask, handler);

        //For attendance details
        fab = view.findViewById(R.id.attendance_fab);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        fab.startAnimation(myAnim);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTable();
            }
        });

        setRadioButtons();
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Extract semester code from spinner selection
        pos = spinner.getSelectedItemPosition();
        code = mSemesterList.get(pos);

        //Start populating recycler view
        jSoupAttendanceTask = new JSoupAttendanceTask();
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
        mDutyAttendenceList = new ArrayList<>();
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
                        int index = mPercentageList.size();
                        mTotalList.remove(index);
                        mSubjectCodeList.remove(index);
                        mSubjectList.remove(index);
                        continue;

                        //mPercentageList.add("-");
                        // mMissedList.add("-");
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
    }

    private void setRadioButtons() {
        int attendanceCutoff = user.getattendanceCutoff(getActivity());

        attendancePercentSelector = view.findViewById(R.id.attendance_cutoff_selector);

        //set default value as disabled
        dutyEnablerSelector = view.findViewById(R.id.attendance_duty_selector);
        dutyEnablerSelector.check(R.id.attendance_duty_disable);

        //set value of cutoff from sharedpreferences which was loaded into attendanceCutOff
        switch (attendanceCutoff) {
            case 75:
                attendancePercentSelector.check(R.id.attendance_cutoff_75);
                break;
            case 80:
                attendancePercentSelector.check(R.id.attendance_cutoff_80);
                break;
        }


        //listener for cutoff radio group
        attendancePercentSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.attendance_cutoff_75:
                        user.setAttendenceCutoff(getActivity(), 75);
                        if(dutyEnablerSelector.getCheckedRadioButtonId() == R.id.attendance_duty_disable) {
                            populateList(false);
                        } else {
                            populateList(true);
                        }
                        break;
                    case R.id.attendance_cutoff_80:
                        user.setAttendenceCutoff(getActivity(), 80);
                        if(dutyEnablerSelector.getCheckedRadioButtonId() == R.id.attendance_duty_disable) {
                            populateList(false);
                        } else {
                            populateList(true);
                        }
                        break;
                }
            }
        });

        //listener for duty attendance enabling and disabling radio group
        dutyEnablerSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.attendance_duty_disable:
                        populateList(false);
                        break;
                    case R.id.attendance_duty_enable:
                        populateList(true);
                        break;
                }
            }
        });
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

    private void attendanceDetails() {


        //To show table view of attendance of the days on which the student was absent
        tableRows = new ArrayList<AttendanceTableRow>();


        //Making the mDutyAttendenceList values to 0 for each subject initially
        //Subjects whose subject codes doesnt end with a number i.e, V,SEP,LIB etc are removed as it will cause issues for duty attendance
        int loopvar = mSubjectList.size();
        for (int i = 0; i < loopvar; i++) {
            String subCode = mSubjectCodeList.get(i);
            subCode = subCode.substring(subCode.length() - 1); //taking last character of subject code
            boolean testResult = true;
            try {
                Integer.parseInt(subCode);  //trying to convert it to a number
            } catch (NumberFormatException e) {    //if it couldn't be converted to a number it is a subject to be removed so remove it from all other lists
                testResult = false;
                mSubjectList.remove(i);
                mPercentageList.remove(i);
                mMissedList.remove(i);
                mTotalList.remove(i);
                mSubjectCodeList.remove(i);
                loopvar--;
                i--;
            }
            if (testResult)  //if it is an acceptable subjects i.e, it is not V,SEP,LIB etc, a 0 entry is made for the subject in the mDutyAttendanceList
                mDutyAttendenceList.add(Integer.toString(0));
        }


        //DEBUG SECTION 1:
        for (int i = 0; i < mSubjectList.size(); i++) {
            String message = mSubjectList.get(i) + " percent : " + mPercentageList.get(i) + " missed : " + mMissedList.get(i) + " out of " + mTotalList.get(i) + "classes.";
            Log.d("Subject" + i, message);
        }
        Log.d("subcode size", mSubjectCodeList.size() + "");
        Log.d("sub size", mSubjectList.size() + "");
        Log.d("percent size", mPercentageList.size() + "");
        Log.d("missed size", mMissedList.size() + "");
        Log.d("total size", mTotalList.size() + "");

        mSubjectCodeList.subList(mSubjectList.size(), mSubjectCodeList.size()).clear();
        Log.d("cleared", "Sub code list unnecessary removed");
        Log.d("subcode size", mSubjectCodeList.size() + "");
        Log.d("sub size", mSubjectList.size() + "");
        Log.d("percent size", mPercentageList.size() + "");
        Log.d("missed size", mMissedList.size() + "");
        Log.d("total size", mTotalList.size() + "");

        for (int i = 0; i < mSubjectCodeList.size(); i++) {
            Log.d("SubCode" + i, mSubjectCodeList.get(i));
        }
        //DEBUG SECTION 1 ENDS

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
                AttendanceTableRow AttendanceTableRow = new AttendanceTableRow();
                Elements tds = row.select("td");
                for (Element td : tds) {
                    if (colNumber == 0) {
                        AttendanceTableRow.setDate(td.text());
                    } else {
                        String subject = td.text();
                        String color = td.attr("bgcolor");
                        AttendanceTableRow.addCell(new AttendanceTableCells(subject, color));

                        //Duty Attendence Counter
                        //To increment the duty attendence count for a subject if a yellow color found in table
                        if (color.equalsIgnoreCase("#ff9900") || color.equalsIgnoreCase("#cccc00")) {
                            if (!(subject.equals("SEP") | subject.equals("V") | subject.equals("MENT") | subject.equals("LIB") | subject.equals("U"))) {

                                for (int i = 0; i < mSubjectCodeList.size(); i++) {
                                    if (subject.contains(mSubjectCodeList.get(i))) {
                                        int n = i;
                                        mDutyAttendenceList.set(n, Integer.toString(Integer.parseInt(mDutyAttendenceList.get(n)) + 1));
                                        break;
                                    }
                                }

                            }
                        }
                    }
                    colNumber++;
                }
                rowNumber++;
                tableRows.add(AttendanceTableRow);
            }
        }

    }

    //Attendance details table
    public void showTable() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attendance_details);

        tableList = dialog.findViewById(R.id.list_view_attendance_table);
        noDataAttendanceTableTextView = dialog.findViewById(R.id.no_data_attendance_text_view);
        TextView dialogButton = dialog.findViewById(R.id.attendance_dialog_dismiss);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tableAdapter = new AttendanceTableAdapter(getActivity(), tableRows);
        if (tableAdapter.isEmpty()) {
            noDataAttendanceTableTextView.setVisibility(View.VISIBLE);
            tableList.setVisibility(View.GONE);
        } else {
            noDataAttendanceTableTextView.setVisibility(View.GONE);
            tableList.setVisibility(View.VISIBLE);
            tableList.setAdapter(tableAdapter);
        }

        dialog.show();
    }


    private void populateList(boolean shouldEnableDuty) {
        mRecyclerView.startAnimation(fadeOutAnimation);
        mRecyclerAdapter = new AttendanceAdapter(mSubjectList, mPercentageList, mSubjectCodeList, mTotalList, mMissedList, user.getattendanceCutoff(getActivity()), mDutyAttendenceList, shouldEnableDuty);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.startAnimation(fadeInAnimation);

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

            //to save users current semesters in case of any changes in the RSMS
            String x = "s" + (count + 1);
            user.setSem(x);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("sem",
                    user.getSem());
            editor.commit();

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
            mRecyclerAdapter = new AttendanceAdapter(mSubjectList, mPercentageList, mSubjectCodeList, mTotalList, mMissedList, user.getattendanceCutoff(getActivity()), mDutyAttendenceList, false);
            setDefaultCountValue();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            extractAttendanceData();
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mProgressDialog.dismiss();
            attendanceDetails();
            dutyEnablerSelector.check(R.id.attendance_duty_disable);    //to check DISABLE radio button when semester is changed in spinner
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
