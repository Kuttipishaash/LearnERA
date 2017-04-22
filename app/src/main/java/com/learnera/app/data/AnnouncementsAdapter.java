package com.learnera.app.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learnera.app.R;

import java.util.List;

/**
 * Created by Shankar on 23-04-2017.
 */

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {

    private List<Announcement> announcementList;

    public AnnouncementsAdapter(List<Announcement> announcementSet) {
        announcementList = announcementSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcements_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);
        holder.announcementTextView.setText(announcement.getmAnnouncement());
        holder.announcementAuthorTextView.setText(announcement.getmAuthor());
        holder.announcementDateTextView.setText(announcement.getmDate());
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView announcementTextView;
        public TextView announcementAuthorTextView;
        public TextView announcementDateTextView;

        public ViewHolder(View v) {
            super(v);
            announcementTextView = (TextView) v.findViewById(R.id.card_announcement_text);
            announcementAuthorTextView = (TextView) v.findViewById(R.id.card_announcement_author);
            announcementDateTextView = (TextView) v.findViewById(R.id.card_announcement_date);
        }
    }
}
