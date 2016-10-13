package com.learnera.app;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.learnera.app.data.StudentContract;


public class AnnouncementsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ANNOUNCEMENT_LOADER=0;
    AnnouncementsCursorAdapter mAnnouncementsCursorAdapter;

    Button button;
    ListView listView;
    int pos=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        listView=(ListView) findViewById(R.id.announcementsList);
        mAnnouncementsCursorAdapter=new AnnouncementsCursorAdapter(this,null);
        listView.setAdapter(mAnnouncementsCursorAdapter);
        //display();

        getLoaderManager().initLoader(ANNOUNCEMENT_LOADER,null,this);

        button=(Button) findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(AnnouncementsActivity.this, TestActivity.class);
                startActivity(n);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                pos=position;
                final String[] project={StudentContract.AnnouncementsGeneral._ID,
                        StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT};
                Cursor cursor=getContentResolver().query(StudentContract.AnnouncementsGeneral.CONTENT_URI,project,null,null,null);
                cursor.moveToPosition(pos);
                final String _id=Integer.toString(cursor.getInt(0));
                final AlertDialog.Builder build = new AlertDialog.Builder(AnnouncementsActivity.this);
                build.setTitle("DELETE ANNOUNCEMENT");
                build.setMessage("Would you like to remove this announcement from the list ?");
                build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Uri u= Uri.withAppendedPath(StudentContract.AnnouncementsGeneral.CONTENT_URI, _id);
                        getContentResolver().delete(u, StudentContract.AnnouncementsGeneral._ID+"="+_id,null);
                        dialog.dismiss();
                        //display();
                    }
                });
                build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                    }
                });
                build.show();
                cursor.close();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int position,
                                    long id) {
                pos=position;
                final String[] project={StudentContract.AnnouncementsGeneral._ID,
                        StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT};
                Cursor cursor=getContentResolver().query(StudentContract.AnnouncementsGeneral.CONTENT_URI,project,null,null,null);
                cursor.moveToPosition(pos);
                final String _id=Integer.toString(cursor.getInt(0));
                final AlertDialog.Builder build = new AlertDialog.Builder(AnnouncementsActivity.this);
                build.setTitle("UPDATE ANNOUNCEMENT");
                build.setMessage("Would you like to update this announcement from the list ?");

                final EditText input = new EditText(AnnouncementsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                build.setView(input);
                build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String x=input.getText().toString();
                        ContentValues values = new ContentValues();
                        values.put(StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT,x);
                        Uri u= Uri.withAppendedPath(StudentContract.AnnouncementsGeneral.CONTENT_URI, _id);
                        getContentResolver().update(u,values,StudentContract.AnnouncementsGeneral._ID+"="+_id,null);
                        dialog.dismiss();
                        //display();

                    }
                });
                build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                build.show();
                cursor.close();
                return true;
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
        //display();
    }

/*    public void display(){
        //columns to return
        String[] project={StudentContract.AnnouncementsGeneral._ID,
                StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT};

        //query() takes in (table name,
                the columns to return,
                the columns for the WHERE clause,
                the values for the WHERE clause,
                don't group the rows,
                don't filter by row groups,
                the sort order);

        Cursor cursor=getContentResolver().query(StudentContract.AnnouncementsGeneral.CONTENT_URI,project,null,null,null);
        AnnouncementsCursorAdapter adapter=new AnnouncementsCursorAdapter(this,cursor);
        listView.setAdapter(adapter);
        cursor.close();

    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection={StudentContract.AnnouncementsGeneral._ID,
                StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT};
        return new CursorLoader(this, StudentContract.AnnouncementsGeneral.CONTENT_URI, projection, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAnnouncementsCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAnnouncementsCursorAdapter.swapCursor(null);
    }
}
