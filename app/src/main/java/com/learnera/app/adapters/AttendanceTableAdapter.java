package com.learnera.app.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.learnera.app.R;
import com.learnera.app.models.AttendanceTableCells;
import com.learnera.app.models.AttendanceTableRow;

import java.util.ArrayList;

/**
 * Created by Shankar on 18-10-2017.
 */

public class AttendanceTableAdapter extends ArrayAdapter<AttendanceTableRow> {
    public AttendanceTableAdapter(Activity context, ArrayList<AttendanceTableRow> rows) {
        super(context, 0, rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_attendance_table, parent, false);
        }
        AttendanceTableRow currentRow = getItem(position);

        TextView absentDateTextView = listItemView.findViewById(R.id.absentDate);
        TextView subject1TextView = listItemView.findViewById(R.id.sub1);
        TextView subject2TextView = listItemView.findViewById(R.id.sub2);
        TextView subject3TextView = listItemView.findViewById(R.id.sub3);
        TextView subject4TextView = listItemView.findViewById(R.id.sub4);
        TextView subject5TextView = listItemView.findViewById(R.id.sub5);
        TextView subject6TextView = listItemView.findViewById(R.id.sub6);
        TextView subject7TextView = listItemView.findViewById(R.id.sub7);

        absentDateTextView.setText(currentRow.getDate());

        ArrayList<AttendanceTableCells> temp = currentRow.getCells();

        subject1TextView.setText((temp.get(0).getmSubject()));
        subject1TextView.setBackgroundColor(Color.parseColor(temp.get(0).getColor()));

        subject2TextView.setText((temp.get(1).getmSubject()));
        subject2TextView.setBackgroundColor(Color.parseColor(temp.get(1).getColor()));

        subject3TextView.setText((temp.get(2).getmSubject()));
        subject3TextView.setBackgroundColor(Color.parseColor(temp.get(2).getColor()));

        subject4TextView.setText((temp.get(3).getmSubject()));
        subject4TextView.setBackgroundColor(Color.parseColor(temp.get(3).getColor()));

        subject5TextView.setText((temp.get(4).getmSubject()));
        subject5TextView.setBackgroundColor(Color.parseColor(temp.get(4).getColor()));

        subject6TextView.setText((temp.get(5).getmSubject()));
        subject6TextView.setBackgroundColor(Color.parseColor(temp.get(5).getColor()));

        subject7TextView.setText((temp.get(6).getmSubject()));
        subject7TextView.setBackgroundColor(Color.parseColor(temp.get(6).getColor()));

        return listItemView;

    }
}
