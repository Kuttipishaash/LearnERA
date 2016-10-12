package com.example.shankar.learnera;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.shankar.learnera.data.StudentContract;
import com.example.shankar.learnera.data.AnnouncementsDB;

import java.util.ArrayList;

public class AnnouncementsActivity extends AppCompatActivity {

    //ArrayList<AnnouncementsList> announcements=new ArrayList<AnnouncementsList>();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        display();

        button=(Button) findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(AnnouncementsActivity.this, TestActivity.class);
                startActivity(n);
            }
        });


       // Firebase ref1=new Firebase(Constants.FIREBASE_URL).child("announcements");
        //Firebase ref2=new Firebase(Constants.FIREBASE_URL).child("u1504045").child("announcements");
        /*ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    AnnouncementsList announcementsList= dataSnapshot.getValue(AnnouncementsList.class);
                    announcements.add(announcementsList);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
       // AnnouncementsAdapter adapter=new AnnouncementsAdapter(AnnouncementsActivity.this,announcements);
        //ListView listView=(ListView) findViewById(R.id.announcementsList);
        //listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        display();
    }

    public void display(){
        AnnouncementsDB announcementsDB=new AnnouncementsDB(AnnouncementsActivity.this);
        SQLiteDatabase db= announcementsDB.getWritableDatabase();

        //columns to return
        String[] project={StudentContract.AnnouncementsGeneral._ID,
                StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT};

        //query() takes in (table name,
        // the columns to return,
        //the columns for the WHERE clause,
        //the values for the WHERE clause,
        //don't group the rows,
        //don't filter by row groups,
        //the sort order);
        Cursor cursor=db.query(StudentContract.AnnouncementsGeneral.TABLE_NAME,project,null,null,null,null,null);
        int announcementColumnIndex=cursor.getColumnIndex(StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT);
        ArrayList<String> announcementsArray=new ArrayList<String>();

        cursor.moveToLast();
        int lastIndex=cursor.getPosition();

        cursor.moveToFirst();
        for(int i=0;i<=lastIndex;i++){
            String announcement=cursor.getString(announcementColumnIndex);
            announcementsArray.add(announcement);
            cursor.moveToNext();
        }
        cursor.close();
        AnnouncementsAdapter arrayAdapter=new AnnouncementsAdapter(this,announcementsArray);
        ListView listView=(ListView) findViewById(R.id.announcementsList);
        listView.setAdapter(arrayAdapter);
    }
}
