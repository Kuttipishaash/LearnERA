package com.learnera.app.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.learnera.app.R;

import java.util.List;

/**
 * Created by Shankar on 06-08-2017.
 */

public class AnnouncementsAdapter extends ExpandableRecyclerAdapter<AnnouncementsAdapter.MyParentViewHolder, AnnouncementsAdapter.MyChildViewHolder> {
    private LayoutInflater mInflater;

    public AnnouncementsAdapter(Context context, List<ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_parent_announcements, viewGroup, false);
        return new MyParentViewHolder(view);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_child_announcements, viewGroup, false);
        return new MyChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(MyParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        AnnouncementKTUParent subcategoryParentListItem = (AnnouncementKTUParent) parentListItem;
        if (subcategoryParentListItem.getmAnnouncementTitle().equals("")) {
            parentViewHolder.listHeader.setText("CLICK TO VIEW");
        } else {
            parentViewHolder.listHeader.setText(subcategoryParentListItem.getmAnnouncementTitle());
        }

    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder childViewHolder, int position, Object childListItem) {
        AnnouncementKTUChild subcategoryChildListItem = (AnnouncementKTUChild) childListItem;
        if (subcategoryChildListItem.getmAnnouncementDescription().equals("")) {
            childViewHolder.listDescription.setVisibility(View.GONE);
        }
        childViewHolder.listDescription.setText(subcategoryChildListItem.getmAnnouncementDescription());
        childViewHolder.listDate.setText(subcategoryChildListItem.getmAnnouncementDate());

    }

    public class MyParentViewHolder extends ParentViewHolder {

        private TextView listHeader;

        private MyParentViewHolder(View itemView) {
            super(itemView);
            listHeader = (TextView) itemView.findViewById(R.id.announcement_title);
    }
    }


    public class MyChildViewHolder extends ChildViewHolder {

        private TextView listDescription;
        private TextView listDate;

        private MyChildViewHolder(View itemView) {
            super(itemView);

            listDescription = (TextView) itemView.findViewById(R.id.announcement_description);
            listDate = (TextView) itemView.findViewById(R.id.announcement_date);
        }
}
}

