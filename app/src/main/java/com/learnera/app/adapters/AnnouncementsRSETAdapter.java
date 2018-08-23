package com.learnera.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learnera.app.R;
import com.learnera.app.data.AnnouncementRSET;

import java.util.List;

/**
 * Created by Shankar on 06-08-2017.
 */

public class AnnouncementsRSETAdapter extends RecyclerView.Adapter<AnnouncementsRSETAdapter.ViewHolder> {
    private List<AnnouncementRSET> announcementRSET;

    public AnnouncementsRSETAdapter(List<AnnouncementRSET> announcementRSET) {
        this.announcementRSET = announcementRSET;
    }

    @Override
    public AnnouncementsRSETAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_announcements_rset, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnnouncementsRSETAdapter.ViewHolder holder, int position) {
        AnnouncementRSET announcement = announcementRSET.get(position);
        holder.announcementRSETTextView.setText(announcement.getAnnouncement());
        holder.announcementdateRSETTextView.setText(announcement.getDate());
    }

    @Override
    public int getItemCount() {
        return announcementRSET.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView announcementRSETTextView;
        public TextView announcementdateRSETTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            announcementRSETTextView = itemView.findViewById(R.id.announcement_rset);
            announcementdateRSETTextView = itemView.findViewById(R.id.announcement_date_rset);
        }
    }
}
