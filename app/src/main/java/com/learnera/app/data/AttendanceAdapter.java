package com.learnera.app.data;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learnera.app.R;

import java.util.List;

/**
 * Created by Prejith on 6/22/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<String> mSubjectList;
    private List<String> mPercentageList;
    private List<String> mSubjectCodeList;
    private List<String> mMissedList;
    private List<String> mTotalList;
    private List<String> mDutyAttendanceList;
    static boolean isDutyEnabled;

    //The cutoff percentage of attendance
    private int cutoffPercentage;

    public AttendanceAdapter(List<String> mSubjectList, List<String> mPercentageList, List<String> mSubjectCodeList, List<String> mTotalList, List<String> mMissedList, int cutoffPercentage, List<String> mDutyAttendanceList, boolean isDutyEnabled) {
        this.mSubjectList = mSubjectList;
        this.mPercentageList = mPercentageList;
        this.mSubjectCodeList = mSubjectCodeList;
        this.mMissedList = mMissedList;
        this.mTotalList = mTotalList;
        this.cutoffPercentage = cutoffPercentage;
        this.mDutyAttendanceList = mDutyAttendanceList;
        this.isDutyEnabled = isDutyEnabled;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance, parent, false);
        return new ViewHolder(v);
    }

    //percentagefield, bunkfield
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int missed, total, onduty;
        missed = Integer.parseInt(mMissedList.get(position));
        total = Integer.parseInt(mTotalList.get(position));
        onduty = Integer.parseInt(mDutyAttendanceList.get(position));

        //common fields
        holder.mSubjectField.setText(mSubjectList.get(position));
        holder.mSubjectCodeField.setText(mSubjectCodeList.get(position));
        holder.mAttendedClasses.setText("Classes missed : " + missed + "/" + total);

        if (!isDutyEnabled) {
            //to set number of classes bunkable
            String classCut = bunkCalculate(missed, total);

            holder.mBunkField.setText(classCut);
            holder.mPercentageField.setText(mPercentageList.get(position));

//            Float percent=Float.valueOf(mPercentageList.get(position));

            if (Float.valueOf(mPercentageList.get(position).substring(0, mPercentageList.get(position).indexOf("%"))) <= 75.0) {
                holder.mPercentageField.setTextColor(Color.parseColor("#C62828"));
            } else if (Float.valueOf(mPercentageList.get(position).substring(0, mPercentageList.get(position).indexOf("%"))) <= 80.0) {
                holder.mPercentageField.setTextColor(Color.parseColor("#F9A825"));
            } else {
                holder.mPercentageField.setTextColor(Color.parseColor("#006600"));
            }

        } else {
            //to set attendence percentage incl duty attendance
            String percentInclDuty = calcAttendanceInclDuty(missed, onduty, total);
            String classCutInclDuty = bunkCalculate((missed - onduty), total);

            holder.mPercentageField.setText(percentInclDuty + "%");

            if (Float.valueOf(percentInclDuty) <= 75.0) {
                holder.mPercentageField.setTextColor(Color.parseColor("#C62828"));
            } else if (Float.valueOf(percentInclDuty) <= 80.0) {
                holder.mPercentageField.setTextColor(Color.parseColor("#F9A825"));
            } else {
                holder.mPercentageField.setTextColor(Color.parseColor("#006600"));
            }


            holder.mBunkField.setText(classCutInclDuty);
            holder.mOnDutyClasses.setText("On Duty : " + onduty + " classes");
        }


    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    //Duty attendance percent calculator
    public String calcAttendanceInclDuty(int missed, int duty, int total) {
        int attended = total - missed;
        double newPercent = (double) (attended + duty) / total * 100.0;

        return String.format("%.2f", newPercent);
    }

    //to calculate classes bunkable
    private String bunkCalculate(int missedClasses, int totalClasses) {
        double percent;
        int bunkOrAttend;
        String predictedStatus;
        int attendedClasses = totalClasses - missedClasses;
        percent = (double) attendedClasses / totalClasses * 100.0;
        if (percent > cutoffPercentage) {
            bunkOrAttend = canBunk(percent, attendedClasses, totalClasses);
            predictedStatus = "You can bunk : ";
        } else if (percent < cutoffPercentage) {
            bunkOrAttend = toAttend(percent, attendedClasses, totalClasses);
            predictedStatus = "You should attend : ";
        } else {
            bunkOrAttend = 0;
            predictedStatus = "You can bunk : ";
        }
        return predictedStatus + String.valueOf(bunkOrAttend) + " classes";
    }

    private int canBunk(double attendencePercent, int attendedClasses, int totalClasses) {
        int count = 0;
        while (attendencePercent >= cutoffPercentage) {
            totalClasses++;
            attendencePercent = (double) attendedClasses / totalClasses * 100.0;
            count++;
        }
        count--;
        return count;
    }

    private int toAttend(double attendencePercent, int attendedClasses, int totalClasses) {
        int count = 0;
        while (attendencePercent < cutoffPercentage) {
            totalClasses++;
            attendedClasses++;
            attendencePercent = (double) attendedClasses / totalClasses * 100.0;
            count++;
        }
        count++;
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSubjectField;
        public TextView mPercentageField;
        public TextView mSubjectCodeField;
        public TextView mBunkField;
        public TextView mAttendedClasses;
        public TextView mOnDutyClasses;

        public ViewHolder(View v) {
            super(v);
            mSubjectField = (TextView) v.findViewById(R.id.list_attendance_subject);
            mPercentageField = (TextView) v.findViewById(R.id.list_attendance_percentage);
            mSubjectCodeField = (TextView) v.findViewById(R.id.list_attendance_subject_code);
            mBunkField = (TextView) v.findViewById(R.id.list_attendance_class_cut);
            mAttendedClasses = (TextView) v.findViewById(R.id.list_attendance_attended_classes);
            mOnDutyClasses = (TextView) v.findViewById(R.id.list_attendance_duty_attendence);

            if (!isDutyEnabled) {
                mOnDutyClasses.setVisibility(View.GONE);
            }
        }
    }

}
