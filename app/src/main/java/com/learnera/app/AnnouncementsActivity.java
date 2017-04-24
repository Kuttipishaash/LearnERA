package com.learnera.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learnera.app.data.Announcement;
import com.learnera.app.data.AnnouncementsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AnnouncementsActivity extends AppCompatActivity {

    //test variables
    private Date date;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mAnnouncementsDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<Announcement> announcementList = new ArrayList<Announcement>();
    private RecyclerView mRecyclerView;
    private AnnouncementsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        //  FirebaseApp.initializeApp(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_announcements);
        mAdapter = new AnnouncementsAdapter(announcementList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAnnouncementsDatabaseReference = mFirebaseDatabase.getReference().child("announcements_general");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Announcement announcement = dataSnapshot.getValue(Announcement.class);
                announcementList.add(announcement);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mAnnouncementsDatabaseReference.addChildEventListener(mChildEventListener);
        mRecyclerView.setAdapter(mAdapter);
        //prepareAnnouncements();



    }


    //testing function
  /*  private void prepareAnnouncements() {
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
        mAdapter.notifyDataSetChanged();

    }*/

}
