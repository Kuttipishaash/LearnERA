package com.learnera.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        holder.syllabusSubjectCodeTextView.setText(currentSubject.getSubjectCode().toUpperCase());
        holder.syllabusSubjectsTextView.setText(currentSubject.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return mSubjectDetailsList.size();
    }

    class SyllabusSubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        LinearLayout syllabusSubjectLinearLayout;
        TextView syllabusSubjectsTextView;
        TextView syllabusSubjectCodeTextView;
        ImageView syllabusSubjectsShareImageView;

        SyllabusSubjectViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            syllabusSubjectLinearLayout = itemView.findViewById(R.id.syllabus_subject_linear_layout);
            syllabusSubjectsTextView = itemView.findViewById(R.id.syllabusSubjectsTextView);
            syllabusSubjectCodeTextView = itemView.findViewById(R.id.syllabusSubjectCodeTextView);
            syllabusSubjectsShareImageView = itemView.findViewById(R.id.syllabusSubjectsShareImageView);
            syllabusSubjectLinearLayout.setOnClickListener(this);
            syllabusSubjectsShareImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.syllabus_subject_linear_layout) {
                int itemPosition = getAdapterPosition();
                SubjectDetail clickedSubjectDetails = mSubjectDetailsList.get(itemPosition);
                launchChromeCustomTab(clickedSubjectDetails.getSubjectDownloadURL(), itemView.getContext());
            } else if (v.getId() == R.id.syllabusSubjectsShareImageView) {
                int itemPosition = getAdapterPosition();
                SubjectDetail clickedSubjectDetails = mSubjectDetailsList.get(itemPosition);

                // Sharing syllabus link
                StringBuilder shareText = new StringBuilder("View the syllabus of ");
                shareText.append(clickedSubjectDetails.getSubjectName());
                shareText.append(" from the link below :\n\n");
                shareText.append(clickedSubjectDetails.getSubjectDownloadURL());
                shareText.append("\n\nShared using LearnERA");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText.toString());
                itemView.getContext().startActivity(sharingIntent);
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



