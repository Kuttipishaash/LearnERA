package com.learnera.app.utils;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.learnera.app.R;
import com.learnera.app.adapters.AttendanceTableAdapter;
import com.learnera.app.models.SharedViewModel;

public class AKDialogFragment extends DialogFragment {

    private static final String TAG = "AKDialogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_ak, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Detailed Attendance");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
        setHasOptionsMenu(true);

        final ListView tableList = rootView.findViewById(R.id.list_view_attendance_table);
        final TextView noDataAttendanceTableTextView = rootView.findViewById(R.id.no_data_attendance_text_view);

        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        model.getTable().observe(this, new Observer<AttendanceTableAdapter>() {
            @Override
            public void onChanged(@Nullable AttendanceTableAdapter adapter) {
                if (adapter.isEmpty()) {
                    noDataAttendanceTableTextView.setVisibility(View.VISIBLE);
                    tableList.setVisibility(View.GONE);
                } else {
                    noDataAttendanceTableTextView.setVisibility(View.GONE);
                    tableList.setVisibility(View.VISIBLE);
                    tableList.setAdapter(adapter);
                }
            }
        }
        );


        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}