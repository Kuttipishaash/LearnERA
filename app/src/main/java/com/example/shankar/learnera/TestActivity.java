package com.example.shankar.learnera;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shankar.learnera.data.StudentContract;
import com.example.shankar.learnera.data.AnnouncementsDB;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.authentication.*;
import com.google.firebase.auth.api.model.StringList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {

    EditText et;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        et=(EditText)findViewById(R.id.announcements);
        b=(Button)findViewById(R.id.buttonSubmitDetails);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String announcement=et.getText().toString().trim();
                AnnouncementsDB announcementsDB=new AnnouncementsDB(TestActivity.this);
                SQLiteDatabase db= announcementsDB.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT,announcement);

                long newRowID=db.insert(StudentContract.AnnouncementsGeneral.TABLE_NAME,null,values);

                if (newRowID==-1)
                {
                    Toast.makeText(TestActivity.this,"ERROR WITH SAVING ANNOUNCEMENT",Toast.LENGTH_SHORT).show();
                    et.setText("");
                }
                else
                {
                    Toast.makeText(TestActivity.this,"Announcement saved with ID "+newRowID,Toast.LENGTH_SHORT).show();
                }
                finish();

            }
        });
    }

}
