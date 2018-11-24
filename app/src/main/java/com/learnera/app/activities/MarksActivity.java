package com.learnera.app.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.learnera.app.R;
import com.learnera.app.fragments.MarksFragment;
import com.learnera.app.models.User;
import com.learnera.app.utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Prejith on 8/8/2016.
 */
public class MarksActivity extends AppCompatActivity {

    private static FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        if (Utils.isNetworkAvailable(this)) {
            if (User.isLoggedIn(this)) {
                doWhenNetworkPresent();
            } else {
                Utils.doWhenNotLoggedIn(this);
                Utils.doWhenNotLoggedIn(this);
            }
        } else {
            Utils.doWhenNoNetwork(this);
        }
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

    public void doWhenNetworkPresent() {
        fragment = new MarksFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.marks_fragment, fragment);
        fragmentTransaction.commit();
    }

}