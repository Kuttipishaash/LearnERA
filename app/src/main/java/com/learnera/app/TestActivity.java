package com.learnera.app;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.learnera.app.data.StudentContract;

public class TestActivity extends AppCompatActivity {

    EditText et;
    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        et=(EditText)findViewById(R.id.announcements);
        b1=(Button)findViewById(R.id.buttonAdd);
        b2=(Button)findViewById(R.id.buttonUpdate);




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*String announcement=et.getText().toString().trim();
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
                }*/
                String announcement=et.getText().toString().trim();
                ContentValues values = new ContentValues();
                values.put(StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT,announcement);

                getContentResolver().insert(StudentContract.AnnouncementsGeneral.CONTENT_URI,values);
                finish();



            }
        });

    }

}
