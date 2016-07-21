package com.example.shankar.learnera;

import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by Prejith on 7/20/2016.
 */

public class AttendanceActivity extends HelpActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_attendance, menu);
        return true;
    }

}
