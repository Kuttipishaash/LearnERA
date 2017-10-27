package com.learnera.app.data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.learnera.app.R;

import java.util.ArrayList;


/**
 * Created by Shankar on 07-10-2017.
 */

public class SyllabusSubjectAdapter extends ArrayAdapter<String> {

    private ArrayList<String> mSubjectList;


    public SyllabusSubjectAdapter(Activity context, ArrayList<String> subjects) {

        super(context, 0, subjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_syllabus_subject, parent, false);
        }
        String currentSubject = getItem(position);
        TextView subjectName = (TextView) listItemView.findViewById(R.id.syllabusSubjectsTextView);
        subjectName.setText(currentSubject);
        return listItemView;
    }
}



