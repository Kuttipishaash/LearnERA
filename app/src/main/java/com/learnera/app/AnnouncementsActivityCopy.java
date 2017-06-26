package com.learnera.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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


public class AnnouncementsActivityCopy extends AppCompatActivity {



    private List<Announcement> announcementList = new ArrayList<Announcement>();
    private List<Announcement> announcementListPersonal = new ArrayList<Announcement>();
    private RecyclerView mRecyclerView, mRecyclerViewPersonal;
    private AnnouncementsAdapter mAdapter, mAdapterPersonal;
    protected String ktuURL;
    protected String rsetURL;
    static Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcementscopy);
        ktuURL="https://ktu.edu.in/eu/core/announcements.htm";
        respondToInternetStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ktuURL="https://ktu.edu.in/eu/core/announcements.htm";
        respondToInternetStatus();
    }

    private void setupPage(){
        //GENERAL ANNOUNCEMENTS INITIALIZATIONS
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_announcements_ktu);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        //PERSONAL ANNOUNCEMENTS INITIALIZATIONS
        //mRecyclerViewPersonal = (RecyclerView) findViewById(R.id.recycler_view_announcements_rajagiri);
        // mAdapterPersonal = new AnnouncementsAdapter(announcementListPersonal);
        //RecyclerView.LayoutManager mLayoutManagerPersonal = new LinearLayoutManager(getApplicationContext());
        //mRecyclerViewPersonal.setLayoutManager(mLayoutManagerPersonal);
        // mRecyclerViewPersonal.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerViewPersonal.setNestedScrollingEnabled(false); //Disables scrolling of individual recycler views as scroll view is implemented
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        Elements tables;
        @Override
        protected void onPreExecute() {
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
            announcementList = new ArrayList<Announcement>();
            Announcement newAnnouncement;
            String date="";
            int count;

            for (Element table : tables) {
                Elements rows = table.getElementsByTag("tr");
                for (Element row : rows) {
                    Elements cells = row.getElementsByTag("td");
                    count=0;
                    newAnnouncement=new Announcement();
                    for(Element cell: cells)
                    {
                        if(count==0)
                        {
                            Elements element = cell.getElementsByTag("b");
                            date = element.text()+" ";
                        }
                       else
                        {
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
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void respondToInternetStatus()
    {
        if(isNetworkAvailable())
        {
            setupPage();
        }

        else
        {
            i =new Intent(AnnouncementsActivityCopy.this,NetworkNotAvailableActivity.class);
            startActivity(i);
        }
    }
}
