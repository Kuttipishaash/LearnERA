package com.learnera.app.fragments;


import android.app.ProgressDialog;
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

import com.learnera.app.NetworkUtils;
import com.learnera.app.R;
import com.learnera.app.data.Announcement;
import com.learnera.app.data.AnnouncementsAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsKTUFragment extends Fragment {

    protected String ktuURL;
    private List<Announcement> announcementList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private AnnouncementsAdapter mAdapter;
    private ProgressDialog mLoading;

    public AnnouncementsKTUFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ktuURL = "https://ktu.edu.in/eu/core/announcements.htm";
        //respondToInternetStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements_ktu, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_announcements_ktu);
        setupPage();
        return view;
    }

    private void setupPage() {
        //GENERAL ANNOUNCEMENTS INITIALIZATIONS
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        NetworkUtils networkCheck = new NetworkUtils();
        if(networkCheck.isNetworkAvailable(getActivity()))
        {
            AnnouncementsKTUFragment.JsoupAsyncTask jsoupAsyncTask = new AnnouncementsKTUFragment.JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }
        else
        {

        }


    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        Elements tables;

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


                Connection.Response res = Jsoup.connect(ktuURL)
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();
                Document doc = Jsoup.connect(ktuURL)
                        .cookies(res.cookies())
                        .get();
                tables = doc.select("table[width=100%][class=ktu-news]");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            announcementList = new ArrayList<>();
            Announcement newAnnouncement;
            String date = "";
            int count;

            for (Element table : tables) {
                Elements rows = table.getElementsByTag("tr");
                for (Element row : rows) {
                    Elements cells = row.getElementsByTag("td");
                    count = 0;
                    newAnnouncement = new Announcement();
                    for (Element cell : cells) {
                        if (count == 0) {
                            Elements element = cell.getElementsByTag("b");
                            date = element.text() + " ";
                        } else {
                            Elements element = cell.getElementsByTag("b");
                            newAnnouncement.setmDate(date);
                            newAnnouncement.setAnnouncementHead(element.text());
                            announcementList.add(newAnnouncement);
                            //element=cell.getElementsByTag("label");
                            //date = date + element.text()+" ";
                            //element=cell.getElementsByTag("strong");
                            //date = date + element.text();
                        }
                        count++;

                    }
                }
            }
            mAdapter = new AnnouncementsAdapter(announcementList);
            mRecyclerView.setAdapter(mAdapter);
            mLoading.dismiss();
        }
    }



}
