package com.learnera.app.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.learnera.app.AnnouncementsActivity;
import com.learnera.app.AttendanceActivity;
import com.learnera.app.MarksActivity;
import com.learnera.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkNotAvailableFragment extends Fragment implements View.OnClickListener {

    Button retryButton;

    public NetworkNotAvailableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_not_available, container, false);
        retryButton = (Button) view.findViewById(R.id.button_retry_connection);
        retryButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_retry_connection) {
            if (isNetworkAvailable()) {
                Fragment fragment;
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (getActivity() instanceof MarksActivity) {
                    fragment = new MarksFragment();
                    fragmentTransaction.replace(R.id.marks_fragment, fragment);
                } else if (getActivity() instanceof AnnouncementsActivity) {
                    //TODO: HANDLING IF FRAGMENT IS INFLATED ANNOUNCEMENT ACTIVITY
                } else if (getActivity() instanceof AttendanceActivity) {
                    fragment = new AttendanceFragment();
                    fragmentTransaction.replace(R.id.fragment_attendance, fragment);
                }
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "NO INTERNET CONNECTION FOUND", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
