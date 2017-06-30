package com.learnera.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prejith on 6/22/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<String> mSubjectList;
    private List<String> mPercentageList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSubjectField;
        public TextView mPercentageField;

        public ViewHolder(View v) {
            super(v);
            mSubjectField = (TextView) v.findViewById(R.id.card_attendance_subject);
            mPercentageField = (TextView) v.findViewById(R.id.card_attendance_percentage);
        }
    }

    public AttendanceAdapter(List<String> mSubjectList, List<String> mPercentageList) {
        this.mSubjectList = mSubjectList;
        this.mPercentageList = mPercentageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_attendance_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSubjectField.setText(mSubjectList.get(position));
        holder.mPercentageField.setText(mPercentageList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }
}