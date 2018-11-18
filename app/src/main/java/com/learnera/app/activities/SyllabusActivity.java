package com.learnera.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.learnera.app.R;
import com.learnera.app.fragments.SyllabusSubjectsFragment;
import com.learnera.app.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


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
        menuInflater.inflate(R.menu.menu_syllabus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.action_report):
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_mail_title));
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_mail_address)});
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.report_mail_subject));
                startActivity(sendIntent);
                return true;
            case (R.id.action_about):
                Utils.showAbout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
