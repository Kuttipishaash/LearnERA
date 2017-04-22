package com.learnera.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.learnera.app.data.Announcement;
import com.learnera.app.data.AnnouncementsAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AnnouncementsActivity extends AppCompatActivity {

    //test variables
    private Date date;

    private List<Announcement> announcementList = new ArrayList<Announcement>();
    private RecyclerView mRecyclerView;
    private AnnouncementsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_announcements);
        mAdapter = new AnnouncementsAdapter(announcementList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        prepareAnnouncements();
    }

    //testing function
    private void prepareAnnouncements() {
        date = Calendar.getInstance().getTime();
        Announcement announcement = new Announcement("NO CLASS TOMORROW", "COLLEGE", date);
        announcementList.add(announcement);
        announcement = new Announcement("NO CLASS YESTERDAY", "COLLEGE", date);
        announcementList.add(announcement);
        announcement = new Announcement("NO CLASS DAY AFTER TOMORROW", "COLLEGE", date);
        announcementList.add(announcement);
        announcement = new Announcement("SUBMIT DB ASSIGNMENT", "PDD TEACHER", date);
        announcementList.add(announcement);
        announcement = new Announcement("RETURN LIBRARY BOOK", "LIBRARY", date);
        announcementList.add(announcement);
        announcement = new Announcement("SUBMIT MATHS ASSIGNMENT", "MATHS TEACHER", date);
        announcementList.add(announcement);
        announcement = new Announcement("BRING REGISTRATION MONEY", "CLASS TEACHER", date);
        announcementList.add(announcement);
        announcement = new Announcement("GIVE NAMES FOR WORKSHOP TODAY ITSELF", "CLASS REPRESENTATIVE", date);
        announcementList.add(announcement);
        announcement = new Announcement("BRING MONEY FOR BDAY CELEBRATIONS :P", "PREJITH", date);
        announcementList.add(announcement);


    }

}
