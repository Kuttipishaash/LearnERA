package com.learnera.app.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.learnera.app.AnnouncementsActivity;
import com.learnera.app.AttendanceActivity;
import com.learnera.app.MarksActivity;
import com.learnera.app.R;

/**
 * Created by praji on 8/8/2017.
 */

public class AboutFragment extends Fragment {

    private View view;
    private Button mContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);

        mContact = (Button) view.findViewById(R.id.button_contact_us);

        getActivity().setTitle("About Us");

        //open gmail
        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"});
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Feedback for LearnEra");
                startActivity(sendIntent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(getActivity() instanceof AttendanceActivity) {
            getActivity().setTitle("Attendance");
        }
        else if(getActivity() instanceof MarksActivity) {
            getActivity().setTitle("Marks");
        }
        else if(getActivity() instanceof AnnouncementsActivity) {
            getActivity().setTitle("Announcements");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
    }
}