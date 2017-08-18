package com.learnera.app.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnera.app.AnnouncementsActivity;
import com.learnera.app.R;
import com.learnera.app.Utils;
import com.learnera.app.data.AnnouncementRSET;
import com.learnera.app.data.AnnouncementsRSETAdapter;
import com.learnera.app.data.Constants;
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
//// TODO: 8/13/2017 Implement internet check and about us for announcements
public class AnnouncementsRSETFragment extends Fragment {

    public static String announcementRSETURL;
    Document doc;
    Elements list;
    Connection.Response res;
    ProgressDialog mLoading;
    private ArrayList<AnnouncementRSET> rsetArrayList;
    private RecyclerView mRecyclerView;
    private AnnouncementsRSETAdapter announcementAdapter;
    private User user;

    public AnnouncementsRSETFragment() {
        announcementRSETURL = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Notice.asp";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User();
        user = user.getLoginInfo(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        setupPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements__rset, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_announcements_rset);
        setupPage();
        return view;
    }

    private void setupPage() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //divider for recycler view
        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(divider);

        if (Utils.isNetworkAvailable(getActivity())) {
            AnnouncementsActivity.network.setVisibility(View.GONE);
            AnnouncementsActivity.mViewPager.setVisibility(View.VISIBLE);

            AnnouncementsRSETFragment.JsoupAsyncTask jsoupAsyncTask = new AnnouncementsRSETFragment.JsoupAsyncTask();
            jsoupAsyncTask.execute();

//            Handler handler = new Handler();
//            Utils.testInternetConnectivity(jsoupAsyncTask, handler);
        } else {
            Utils.doWhenNoNetwork(getActivity());
        }
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        Elements tables;
        private ProgressDialog mLoading;

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if(mLoading.isShowing()) {
                mLoading.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
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
                res = Jsoup.connect(Constants.loginURL)
                        .data("user", user.getUserName())
                        .data("pass", String.valueOf(user.getPassword()))
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();
                doc = Jsoup.connect(AnnouncementsRSETFragment.announcementRSETURL)
                        .cookies(res.cookies())
                        .get();
                list = doc.select("table[bordercolor=#CCCCCC]");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            rsetArrayList = new ArrayList<>();
            String date = "";
            String announcement;
            int count;
            for (Element table : list) {
                Elements rows = table.getElementsByTag("tr");
                for (Element row : rows) {
                    Elements cells = row.getElementsByTag("td");
                    count = 0;
                    for (Element cell : cells) {
                        if (count == 1) {
                            Elements announce = cell.getElementsByTag("a");
                            date = announce.text();
                        } else if (count > 0) {
                            Elements announce = cell.getElementsByTag("a");
                            announcement = announce.text();
                            AnnouncementRSET announcementRSET = new AnnouncementRSET(announcement, date);
                            rsetArrayList.add(announcementRSET);
                        }
                        count++;
                    }
                }
            }
            mLoading.dismiss();
            rsetArrayList.remove(0);
            announcementAdapter = new AnnouncementsRSETAdapter(rsetArrayList);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(announcementAdapter);
        }
    }
}
