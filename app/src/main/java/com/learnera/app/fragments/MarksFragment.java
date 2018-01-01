package com.learnera.app.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.Utils;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MarksFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static int countSemesters;
    protected ArrayList<String> semList;

    //protected FloatingActionButton fab;

    Spinner spinner1;
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
    ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private MarksAdapter marksAdapter;
    private View view;
    private User user;

    public MarksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
        user = user.getLoginInfo(getActivity());

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_marks);
        spinner1 = (Spinner) view.findViewById(R.id.spinner_marks_semesters);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_marks_category);
        //setupFAB();
        return view;
    }

    /*
        public void setupFAB(){
            fab  = (FloatingActionButton) view.findViewById(R.id.marks_fab);
            fab.setSize(FloatingActionButton.SIZE_NORMAL);
            final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);

            fab.startAnimation(myAnim);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new MarkCalculateFragment();
                    FragmentActivity fragmentActivity = getActivity();
                    FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.marks_fragment, fragment);
                    fragmentTransaction.commit();
                }
            });
        }
    */
    //SPINNER SELECTION HANDLING
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.spinner_marks_semesters:
                //execute asynctask for 2nd spinner
                JSoupSpinnerCategoryTask JSoupSpinnerCategoryTask = new JSoupSpinnerCategoryTask(semListCode.get(spinner1.getSelectedItemPosition()));
                JSoupSpinnerCategoryTask.execute();

                //check for internet connectivity
                Handler handler = new Handler();
                Utils.testInternetConnectivity(JSoupSpinnerCategoryTask, handler);
                break;
            case R.id.spinner_marks_category:
                //execute spinner for fetching marks_custom data
                finalFetchURL = Constants.markURL + "?code=" + semListCode.get(spinner1.getSelectedItemPosition()) + "&E_ID=" + examValues.get(spinner2.getSelectedItemPosition());
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
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(countSemesters - 1);
        spinner1.setOnItemSelectedListener(this);
    }

    private void dynamicExamList() {
        //SPINNER 2
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_spinner_item,
                examList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(MarksFragment.this);

        if (spinner2.getCount() == 0) {
            spinner2.setEnabled(false);
            Toast.makeText(getActivity(), "No data to display!", Toast.LENGTH_SHORT).show();
        } else {
            spinner2.setEnabled(true);
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
        mProgressDialog = new ProgressDialog(getActivity());
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
            mProgressDialog.dismiss();

        }

    }


}
