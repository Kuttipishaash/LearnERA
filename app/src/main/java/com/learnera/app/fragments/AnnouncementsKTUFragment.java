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

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.learnera.app.AnnouncementsActivity;
import com.learnera.app.R;
import com.learnera.app.Utils;
import com.learnera.app.data.AnnouncementKTUChild;
import com.learnera.app.data.AnnouncementKTUParent;
import com.learnera.app.data.AnnouncementsAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsKTUFragment extends Fragment {

    protected String ktuURL;

    private RecyclerView mRecyclerView;
    private boolean isLoaded = false, isVisibleToUser = false;

    public AnnouncementsKTUFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ktuURL = "https://ktu.edu.in/eu/core/announcements.htm";
        //respondToInternetStatus();
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_attendance, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements_ktu, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_announcements_ktu);
        setupPage();
        setHasOptionsMenu(true);
        return view;
    }


    private void setupPage() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        if (Utils.isNetworkAvailable(getActivity())) {
            AnnouncementsActivity.network.setVisibility(View.GONE);
            AnnouncementsActivity.mViewPager.setVisibility(View.VISIBLE);

            AnnouncementsKTUFragment.JsoupAsyncTask jsoupAsyncTask = new AnnouncementsKTUFragment.JsoupAsyncTask();
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

            if (mLoading.isShowing()) {
                mLoading.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
        }

        @Override
        protected void onPreExecute() {
            mLoading = new ProgressDialog(getActivity(), R.style.ProgressDialogCustom);
            mLoading.setMessage("Loading KTU Data...");
            mLoading.setCancelable(false);
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
            mLoading.dismiss();
            AnnouncementKTUParent newAnnouncements;
            AnnouncementKTUChild newAnnouncementKTUChild;
            String date = "";
            String title;
            String desc;
            //String ext = "";
            int count;
            List<ParentListItem> parentListItems = new ArrayList<>();


            for (Element table : tables) {
                Elements rows = table.getElementsByTag("tr");
                for (Element row : rows) {
                    Elements cells = row.getElementsByTag("td");
                    newAnnouncements = new AnnouncementKTUParent();
                    newAnnouncementKTUChild = new AnnouncementKTUChild();
                    count = 0;
                    for (Element cell : cells) {
                        if (count == 0) {
                            Elements element = cell.getElementsByTag("b");
                            date = element.text().substring(0, 10);

                        } else {
                            Elements elementdesc = cell.getElementsByTag("li");
                            for (Element element : elementdesc) {
                                Elements head = element.getElementsByTag("b");
                                //Elements head2 = element.getElementsByTag("a");

                                title = head.eq(0).text();
                                //ext = head2.eq(0).text();
                                desc = element.ownText();


                                newAnnouncementKTUChild.setmAnnouncementDate(date);
                                newAnnouncements.setmAnnouncementTitle(title);
                                newAnnouncementKTUChild.setmAnnouncementDescription(desc);
                                List<AnnouncementKTUChild> childList = new ArrayList<>();
                                childList.add(newAnnouncementKTUChild);
                                newAnnouncements.setChildList(childList);
                                parentListItems.add(newAnnouncements);

                            }
                        }
                        count++;
                    }
                }
                mRecyclerView.setAdapter(new AnnouncementsAdapter(getContext(), parentListItems));
            }
        }

    }
}
