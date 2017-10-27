package com.learnera.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.learnera.app.fragments.SyllabusSubjectFragment;


public class SyllabusActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        fragment = new SyllabusSubjectFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_syllabus, fragment);
        fragmentTransaction.commit();
    }
}
