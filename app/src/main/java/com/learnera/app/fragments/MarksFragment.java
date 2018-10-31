package com.learnera.app.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.adapters.MarksAdapter;
import com.learnera.app.anim.MyBounceInterpolator;
import com.learnera.app.models.Constants;
import com.learnera.app.models.Marks;
import com.learnera.app.models.User;
import com.learnera.app.utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarksFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static int countSemesters;
    protected ArrayList<String> semList;
    protected FloatingActionButton fab;

    //protected FloatingActionButton fab;

    private Spinner semesterSpinner;
    private Spinner testCategorySpinner;
    private ArrayList<String> semListCode;
    private ArrayList<String> subjectNames;
    private ArrayList<String> subjectCodes;
    private ArrayList<String> subjectMarks;
    private ArrayList<String> subjectMarksOutOf;
    private ArrayList<String> examValues;
    private ArrayList<String> examList;
    private ArrayList<Marks> marksList = new ArrayList<>();
    private Document doc;
    private Elements list;
    private Connection.Response res;
    private String finalFetchURL;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private MarksAdapter marksAdapter;
    private View view;
    private User user;
    boolean isFabHide = false;

    public MarksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
        user = User.getLoginInfo(getActivity());

        initProgressDialog();

        getActivity().setTitle("Marks");

        JSoupSemesterTask jSoupSemesterTask = new JSoupSemesterTask();
        jSoupSemesterTask.execute();

        Handler handler = new Handler();
        Utils.testInternetConnectivity(jSoupSemesterTask, handler);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_marks, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view_marks);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        semesterSpinner = view.findViewById(R.id.spinner_marks_semesters);
        testCategorySpinner = view.findViewById(R.id.spinner_marks_category);
        //setupFAB();
        initToolbar();

        //For attendance details
        fab = view.findViewById(R.id.marks_fab);
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        fab.startAnimation(myAnim);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TODO: Implement Total Marks Feature
//            }
//        });
        initComponent();
        return view;
    }
    private void initToolbar() {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Marks");

    }
    private void initComponent() {
        NestedScrollView nested_content = view.findViewById(R.id.nested_scroll_view_marks);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateFab(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateFab(true);
                }
            }
        });
    }
    private void animateFab(final boolean hide) {
        FloatingActionButton fab_add = view.findViewById(R.id.marks_fab);
        if (isFabHide && hide || !isFabHide && !hide) return;
        isFabHide = hide;
        int moveY = hide ? (2 * fab_add.getHeight()) : 0;
        fab_add.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }


    //SPINNER SELECTION HANDLING
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.spinner_marks_semesters:
                //execute asynctask for 2nd spinner
                JSoupSpinnerCategoryTask JSoupSpinnerCategoryTask = new JSoupSpinnerCategoryTask(semListCode.get(semesterSpinner.getSelectedItemPosition()));
                JSoupSpinnerCategoryTask.execute();

                //check for internet connectivity
                Handler handler = new Handler();
                Utils.testInternetConnectivity(JSoupSpinnerCategoryTask, handler);
                break;
            case R.id.spinner_marks_category:
                //execute spinner for fetching marks_custom data
                finalFetchURL = Constants.markURL + "?code=" + semListCode.get(semesterSpinner.getSelectedItemPosition()) + "&E_ID=" + examValues.get(testCategorySpinner.getSelectedItemPosition());
                MarkAsyncTask marksAsyncTask = new MarkAsyncTask();
                marksAsyncTask.execute();

                Handler handler1 = new Handler();
                Utils.testInternetConnectivity(marksAsyncTask, handler1);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void dynamicSemList() {
        semList = new ArrayList<>();
        for (int i = 0; i < countSemesters; i++) {
            semList.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
        //SPINNER 1
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_spinner_item,
                semList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(adapter1);
        semesterSpinner.setSelection(countSemesters - 1);
        semesterSpinner.setOnItemSelectedListener(this);
    }

    private void dynamicExamList() {
        //SPINNER 2
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_spinner_item,
                examList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testCategorySpinner.setAdapter(adapter1);
        testCategorySpinner.setOnItemSelectedListener(MarksFragment.this);

        if (testCategorySpinner.getCount() == 0) {
            testCategorySpinner.setEnabled(false);
            Toast.makeText(getActivity(), "No data to display!", Toast.LENGTH_SHORT).show();
            mRecyclerView.setAdapter(null);
        } else {
            testCategorySpinner.setEnabled(true);
        }
    }

    private void createList() {

        marksAdapter = new MarksAdapter(marksList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(marksAdapter);

    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity(),R.style.ProgressDialogCustom);
        mProgressDialog.setMessage("Loading Data...");
        mProgressDialog.setCancelable(false);
    }

    //FOR SPINNER 1 DYNAMIC
    private class JSoupSemesterTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (Utils.isNetworkAvailable(getActivity())) {
                mProgressDialog.show();
            } else {
                Utils.doWhenNoNetwork(getActivity());
            }
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                Utils.doWhenNoNetwork(getActivity());
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
            mProgressDialog.dismiss();
        }
    }

    //FOR SPINNER 2 DYNAMIC
    private class JSoupSpinnerCategoryTask extends AsyncTask<Void, Void, Void> {

        String codes;

        public JSoupSpinnerCategoryTask(String code) {
            this.codes = code;
        }

        @Override
        protected void onPreExecute() {
            if (Utils.isNetworkAvailable(getActivity())) {
                mProgressDialog.show();
            } else {
                Utils.doWhenNoNetwork(getActivity());
            }
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
        protected void onCancelled() {
            super.onCancelled();

            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
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
            mProgressDialog.dismiss();
        }
    }

    //FOR MARK FETCH
    private class MarkAsyncTask extends AsyncTask<Void, Void, Void> {

        Elements tables;

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
            if (Utils.isNetworkAvailable(getActivity())) {
                mProgressDialog.show();
            } else {
                Utils.doWhenNoNetwork(getActivity());
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
                marks.setmSubCode(subjectCodes.get(i));
                marks.setmSubName(subjectNames.get(i));
                marks.setmOutOf(subjectMarksOutOf.get(i));
                marks.setmSubMarks(subjectMarks.get(i));
                marksList.add(marks);
            }
            createList();
            subjectCodes.clear();
            subjectNames.clear();
            subjectMarksOutOf.clear();
            subjectMarks.clear();
            mProgressDialog.dismiss();

        }

    }


}
