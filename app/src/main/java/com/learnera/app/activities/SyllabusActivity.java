package com.learnera.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.learnera.app.R;
import com.learnera.app.fragments.SyllabusSubjectsFragment;
import com.learnera.app.models.User;
import com.learnera.app.utils.Utils;


public class SyllabusActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        setTitle(getString(R.string.title_activity_syllabus));
        fragment = new SyllabusSubjectsFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_syllabus, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.action_about):
                Utils.showAbout(this);
                return true;
            case (R.id.action_logout):
                User.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
