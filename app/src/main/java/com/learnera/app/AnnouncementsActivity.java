package com.learnera.app;

import android.content.SharedPreferences;
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
import java.util.List;


public class AnnouncementsActivity extends AppCompatActivity {

    //test variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mAnnouncementsDatabaseReference, mPersonalAnnouncementsDatabaseReference;
    private ChildEventListener mChildEventListener, mPersonalChildEventListner;

    private List<Announcement> announcementList = new ArrayList<Announcement>();
    private List<Announcement> announcementListPersonal = new ArrayList<Announcement>();
    private RecyclerView mRecyclerView, mRecyclerViewPersonal;
    private AnnouncementsAdapter mAdapter, mAdapterPersonal;
    private SharedPreferences preferences;
    private String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        preferences = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
        mUID = preferences.getString("UID", "");

        //GENERAL ANNOUNCEMENTS INITIALIZATIONS
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_announcements);
        mAdapter = new AnnouncementsAdapter(announcementList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //PERSONAL ANNOUNCEMENTS INITIALIZATIONS
        mRecyclerViewPersonal = (RecyclerView) findViewById(R.id.recycler_view_announcements_personal);
        mAdapterPersonal = new AnnouncementsAdapter(announcementListPersonal);
        RecyclerView.LayoutManager mLayoutManagerPersonal = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewPersonal.setLayoutManager(mLayoutManagerPersonal);
        mRecyclerViewPersonal.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //GENERAL ANNOUNCENETS
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

        //PERSONAL ANNOUNCEMENTS
        mPersonalAnnouncementsDatabaseReference = mFirebaseDatabase.getReference().child(mUID).child("announcements");
        mPersonalChildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Announcement announcement = dataSnapshot.getValue(Announcement.class);
                announcementListPersonal.add(announcement);
                mAdapterPersonal.notifyDataSetChanged();
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
        mPersonalAnnouncementsDatabaseReference.addChildEventListener(mPersonalChildEventListner);
        mRecyclerViewPersonal.setAdapter(mAdapterPersonal);
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
