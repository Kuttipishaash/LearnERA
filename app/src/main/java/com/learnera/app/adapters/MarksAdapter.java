package com.learnera.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learnera.app.R;
import com.learnera.app.models.Marks;

import java.util.List;

/**
 * Created by Shankar on 24-06-2017.
 */

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder>{
    private List<Marks> marksList;

    private Context context;

    public MarksAdapter(List<Marks> marksSet) {
        marksList = marksSet;
    }

    @Override
    public MarksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_marks, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MarksAdapter.ViewHolder holder, int position) {
        Marks marks = marksList.get(position);
        holder.subjectCodeTextView.setText(marks.getmSubCode());
        holder.subjectNameTextView.setText(marks.getmSubName().trim());
        holder.subjectMaxMarksTextView.setText(marks.getmOutOf());
        holder.subjectMarksTextView.setText(marks.getmSubMarks());

        if (!marks.getmSubMarks().equals("NA")) {
            float parsedMarks = Float.parseFloat(marks.getmSubMarks().replaceAll("\\s", ""));
            float parsedMaxMarks = Float.parseFloat(marks.getmOutOf().replaceAll("\\s", ""));

            if (parsedMarks < (0.45 * parsedMaxMarks)) {
                holder.subjectMarksTextView.setTextColor(context.getResources().getColor(R.color.danger_red));
            } else if ( parsedMarks < (0.75 * parsedMaxMarks)) {
                holder.subjectMarksTextView.setTextColor(context.getResources().getColor(R.color.warning_orange));
            } else {
                holder.subjectMarksTextView.setTextColor(context.getResources().getColor(R.color.success_green));
            }
        }

    }

    @Override
    public int getItemCount() {
        return marksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectCodeTextView;
        public TextView subjectNameTextView;
        public TextView subjectMaxMarksTextView;
        public TextView subjectMarksTextView;


        public ViewHolder(View v) {
            super(v);
            subjectCodeTextView = v.findViewById(R.id.subject_code_text_view);
            subjectNameTextView = v.findViewById(R.id.subject_name_text_view);
            subjectMaxMarksTextView = v.findViewById(R.id.max_marks_text_view);
            subjectMarksTextView = v.findViewById(R.id.obtained_marks_text_view);
        }
    }
}

