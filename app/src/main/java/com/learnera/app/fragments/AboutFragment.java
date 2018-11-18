package com.learnera.app.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.learnera.app.R;
import com.learnera.app.activities.AnnouncementsActivity;
import com.learnera.app.activities.AttendanceActivity;
import com.learnera.app.activities.MarksActivity;
import com.learnera.app.activities.WelcomeActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created by praji on 8/8/2017.
 */

public class AboutFragment extends Fragment {

    private View view;
    private Button mContact;
    private TextView mAppName;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        mContact = view.findViewById(R.id.button_contact_us);


        mAppName = view.findViewById(R.id.text_app_name);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Pasajero.otf");
        mAppName.setTypeface(typeface);
        //To open gmail for sending feedback to the developer
        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"learneraproject@gmail.com"});
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Feedback for LearnEra");
                startActivity(sendIntent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getActivity() instanceof AttendanceActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            getActivity().setTitle("Attendance");
        } else if (getActivity() instanceof MarksActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            getActivity().setTitle("Marks");
        } else if (getActivity() instanceof AnnouncementsActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            getActivity().setTitle("Announcements");
        } else if (getActivity() instanceof WelcomeActivity) {
            startActivity(new Intent(getActivity(), WelcomeActivity.class));
        } else {
            /*
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager.getBackStackEntryCount() == 1) {
                getActivity().setTitle("Syllabus");
            }
            else if(fragmentManager.getBackStackEntryCount() == 2) {
                getActivity().setTitle("kopp");
            }*/
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
    }
}