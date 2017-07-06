package com.learnera.app.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.learnera.app.NetworkUtils;
import com.learnera.app.R;
import com.learnera.app.data.Constants;
import com.learnera.app.data.Marks;
import com.learnera.app.data.MarksAdapter;
import com.learnera.app.data.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarksFragment extends Fragment implements AdapterView.OnItemSelectedListener {


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
    ArrayList<Marks> marksList = new ArrayList<>();
    Document doc;
    Elements list;
    Connection.Response res;
    String finalFetchURL;
    ProgressDialog mLoading;
    private RecyclerView mRecyclerView;
    private MarksAdapter marksAdapter;

    private View v;
    private User user;
    private SharedPreferences sharedPreferences;

    public MarksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
        user = user.getLoginInfo(getActivity());

        MarksFragment.JsoupAsyncTask jsoupAsyncTask = new MarksFragment.JsoupAsyncTask();
        jsoupAsyncTask.execute();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_marks, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_marks);
        spinner1 = (Spinner) v.findViewById(R.id.spinner_marks_semesters);
        spinner2 = (Spinner) v.findViewById(R.id.spinner_marks_category);
        return v;
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
                finalFetchURL = Constants.markURL + "?code=" + semListCode.get(spinner1.getSelectedItemPosition()) + "&E_ID=" + examValues.get(spinner2.getSelectedItemPosition());
                MarkAsyncTask marksAsyncTask = new MarkAsyncTask();
                marksAsyncTask.execute();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void dynamicSemList() {
        semList = new ArrayList<>();
        for (int i = 0; i < countSemesters; i++) {
            semList.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
        //SPINNER 1
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                v.getContext(),
                android.R.layout.simple_spinner_item,
                semList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(countSemesters - 1);
        spinner1.setOnItemSelectedListener(this);
    }

    private void dynamicExamList() {
        //SPINNER 2
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                v.getContext(),
                android.R.layout.simple_spinner_item,
                examList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(MarksFragment.this);
    }


    private void createList() {

        marksAdapter = new MarksAdapter(marksList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(marksAdapter);
    }

    //FOR SPINNER 1 DYNAMIC
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mLoading = new ProgressDialog(getActivity());
            mLoading.setMessage("Loading Data...");
            mLoading.show();
            super.onPreExecute();
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
                doc = Jsoup.connect(Constants.markURL)
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
            semListCode = new ArrayList<>();
            MarksFragment.countSemesters = 0;
            for (Element ls : list) {
                for (Element opt : ls.select("option")) {
                    semListCode.add(opt.text());
                    MarksFragment.countSemesters += 1;
                }
            }
            dynamicSemList();
            mLoading.dismiss();
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
            mLoading = new ProgressDialog(getActivity());
            mLoading.setMessage("Loading Data...");
            mLoading.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                doc = Jsoup.connect(Constants.markURL)
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
            examValues = new ArrayList<>();
            examList = new ArrayList<>();
            for (Element ls : list) {
                for (Element opt : ls.select("option")) {
                    examValues.add(opt.val());
                    examList.add(opt.text());
                }
            }
            dynamicExamList();
            mLoading.dismiss();
        }
    }

    //FOR MARK FETCH
    private class MarkAsyncTask extends AsyncTask<Void, Void, Void> {

        Elements tables;

        @Override
        protected void onPreExecute() {
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                mLoading = new ProgressDialog(getActivity());
                mLoading.setMessage("Loading Data...");
                mLoading.show();
            } else {
                NetworkUtils.doWhenNoNetwork(getActivity());
            }

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
            String subjectHeader[] = new String[3];
            int rownum = 0, colnum;
            tables = doc.select("table[width = 100%][align=Left][cellpadding=1]");
            subjectCodes = new ArrayList<>();
            subjectLetters = new ArrayList<>();
            subjectMarksOutOf = new ArrayList<>();
            subjectMarks = new ArrayList<>();
            subjectNames = new ArrayList<>();
            marksList = new ArrayList<>();
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
                                subjectHeader = cell.text().split(" ");
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
            mLoading.dismiss();
        }
    }


}
