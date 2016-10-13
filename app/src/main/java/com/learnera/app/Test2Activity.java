package com.learnera.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.authentication.*;

public class Test2Activity extends AppCompatActivity {
    TextView textView;
    Button b1,b2;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        textView=(TextView)findViewById(R.id.statusTextView);
        b1=(Button)findViewById(R.id.sunnyButton);
        b2=(Button)findViewById(R.id.foggyButton);
        mRef=new Firebase(Constants.FIREBASE_URL+"/condition");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text=dataSnapshot.getValue(String.class);
                textView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
