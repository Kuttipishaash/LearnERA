package com.example.shankar.learnera;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shankar on 02-10-2016.
 */

public class AnnouncementsAdapter extends ArrayAdapter<String>{
    public AnnouncementsAdapter(Activity context, ArrayList<String> announcements)
    {
        super(context,0,announcements);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.announcements_list_item,parent,false);
        }
        String announcementText=getItem(position);
        TextView announcement=(TextView)listItemView.findViewById(R.id.announcementTextView);
        announcement.setText(announcementText);
        return listItemView;

    }
}
