package com.learnera.app.data;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSubjectField;
        public TextView mPercentageField;
        public TextView mSubjectCodeField;
        public TextView mBunkField;

        public ViewHolder(View v) {
            super(v);
            mSubjectField = (TextView) v.findViewById(R.id.list_attendance_subject);
            mPercentageField = (TextView) v.findViewById(R.id.list_attendance_percentage);
            mSubjectCodeField = (TextView) v.findViewById(R.id.list_attendance_subject_code);
            mBunkField = (TextView) v.findViewById(R.id.list_attendance_class_cut);
        }
    }

    public AttendanceAdapter(List<String> mSubjectList, List<String> mPercentageList, List<String> mSubjectCodeList, List<String> mTotalList, List<String> mMissedList) {
        this.mSubjectList = mSubjectList;
        this.mPercentageList = mPercentageList;
        this.mSubjectCodeList = mSubjectCodeList;
        this.mMissedList = mMissedList;
        this.mTotalList = mTotalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSubjectField.setText(mSubjectList.get(position));
        holder.mPercentageField.setText(mPercentageList.get(position));
        holder.mSubjectCodeField.setText(mSubjectCodeList.get(position));

        String classCut = bunkCalculate(Integer.parseInt(mMissedList.get(position)), Integer.parseInt(mTotalList.get(position)));
        holder.mBunkField.setText(classCut);
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    //to calculate classes bunkable
    private String bunkCalculate(int missedClasses, int totalClasses) {
        double percent;
        int bunkOrAttend;
        String predictedStatus;
        int attendedClasses = totalClasses - missedClasses;
        percent = (double) attendedClasses / totalClasses * 100.0;
        if (percent > 75) {
            bunkOrAttend = canBunk(percent, attendedClasses, totalClasses);
            predictedStatus = "You can bunk : ";
        } else if (percent < 75) {
            bunkOrAttend = toAttend(percent, attendedClasses, totalClasses);
            predictedStatus = "You should attend : ";
        } else {
            bunkOrAttend = 0;
            predictedStatus = "You can bunk : ";
        }
        return predictedStatus + String.valueOf(bunkOrAttend) + " classes";
/*
        //display bunkable dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attendence_bunk_view);
        TextView dialogButton = (TextView) dialog.findViewById(R.id.bunk_dialog_dismiss);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView bunkSubjectTextView = (TextView) dialog.findViewById(R.id.bunk_subject_text_view);
        TextView bunkSubjectCodeTextView = (TextView) dialog.findViewById(R.id.bunk_subject_code_text_view);
        TextView classCountTextView = (TextView) dialog.findViewById(R.id.num_of_classes_text_view);
        bunkSubjectTextView.setText(mSubjectList.get(itemPositionClicked));
        bunkSubjectCodeTextView.setText(mSubjectCodeList.get(itemPositionClicked));
        classCountTextView.setText();
        dialog.show();
*/    }

    private int canBunk(double attendencePercent, int attendedClasses, int totalClasses) {
        int count = 0;
        while (attendencePercent >= 75) {
            totalClasses++;
            attendencePercent = (double) attendedClasses / totalClasses * 100.0;
            count++;
        }
        count--;
        return count;
    }

    private int toAttend(double attendencePercent, int attendedClasses, int totalClasses) {
        int count = 0;
        while (attendencePercent < 75) {
            totalClasses++;
            attendedClasses++;
            attendencePercent = (double) attendedClasses / totalClasses * 100.0;
            count++;
        }
        count++;
        return count;
    }

}
