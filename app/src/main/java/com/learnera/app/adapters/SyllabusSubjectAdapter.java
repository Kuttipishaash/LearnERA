package com.learnera.app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learnera.app.R;

import java.util.ArrayList;


/**
 * Created by Shankar on 07-10-2017.
 */

public class SyllabusSubjectAdapter extends RecyclerView.Adapter<SyllabusSubjectAdapter.SyllabusSubjectViewHolder> {

    private ArrayList<String> mSubjectList;

    public void setmSubjectList(ArrayList<String> mSubjectList) {
        this.mSubjectList = mSubjectList;
    }


    @NonNull
    @Override
    public SyllabusSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_syllabus_subject, parent, false);
        return new SyllabusSubjectViewHolder(recyclerItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SyllabusSubjectViewHolder holder, int position) {
        String currentSubject = mSubjectList.get(position);
        holder.syllabusSubjectsTextView.setText(currentSubject);
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    class SyllabusSubjectViewHolder extends RecyclerView.ViewHolder {
        TextView syllabusSubjectsTextView;

        SyllabusSubjectViewHolder(View itemView) {
            super(itemView);
            syllabusSubjectsTextView = itemView.findViewById(R.id.syllabusSubjectsTextView);
        }
    }
}



