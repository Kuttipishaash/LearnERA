package com.learnera.app.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.models.SubjectDetail;

import java.util.List;


/**
 * Created by Shankar on 07-10-2017.
 */

public class SyllabusSubjectAdapter extends RecyclerView.Adapter<SyllabusSubjectAdapter.SyllabusSubjectViewHolder> {

    private List<SubjectDetail> mSubjectDetailsList;

    public void setmSubjectDetailsList(List<SubjectDetail> mSubjectDetailsList) {
        this.mSubjectDetailsList = mSubjectDetailsList;
    }


    @NonNull
    @Override
    public SyllabusSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_syllabus_subject, parent, false);
        return new SyllabusSubjectViewHolder(recyclerItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SyllabusSubjectViewHolder holder, int position) {
        SubjectDetail currentSubject = mSubjectDetailsList.get(position);
        holder.syllabusSubjectsTextView.setText(currentSubject.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return mSubjectDetailsList.size();
    }

    class SyllabusSubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        TextView syllabusSubjectsTextView;
        ImageView syllabusSubjectsShareImageView;

        SyllabusSubjectViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            syllabusSubjectsTextView = itemView.findViewById(R.id.syllabusSubjectsTextView);
            syllabusSubjectsShareImageView = itemView.findViewById(R.id.syllabusSubjectsShareImageView);
            syllabusSubjectsTextView.setOnClickListener(this);
            syllabusSubjectsShareImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.syllabusSubjectsTextView) {
                int itemPosition = getAdapterPosition();
                SubjectDetail clickedSubjectDetails = mSubjectDetailsList.get(itemPosition);
                launchChromeCustomTab(clickedSubjectDetails.getSubjectDownloadURL(), itemView.getContext());
            } else if (v.getId() == R.id.syllabusSubjectsShareImageView) {
                int itemPosition = getAdapterPosition();
                SubjectDetail clickedSubjectDetails = mSubjectDetailsList.get(itemPosition);
                //TODO: Implement the sharing code here
                Toast.makeText(itemView.getContext(), "Share clicked", Toast.LENGTH_SHORT).show();
            }
        }

        private void launchChromeCustomTab(String completeUrl, Context context) {
            //Launch chrome custom tab
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent intent = builder.build();
            builder.setToolbarColor(context.getResources().getColor(R.color.md_red_700));
            builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
            intent.launchUrl(context, Uri.parse(completeUrl));
        }
    }
}



