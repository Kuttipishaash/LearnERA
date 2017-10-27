package com.learnera.app.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.learnera.app.R;

import java.util.ArrayList;

/**
 * Created by Shankar on 16-10-2017.
 */

public class ModuleAdapter extends ArrayAdapter<Module> {
    public ModuleAdapter(Activity context, ArrayList<Module> modules) {
        super(context, 0, modules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_module, parent, false);
        }
        Module currentModule = getItem(position);
        TextView moduleNumber = (TextView) listItemView.findViewById(R.id.moduleNum);
        moduleNumber.setText(currentModule.getmModNum());
        TextView moduleNameTextView = (TextView) listItemView.findViewById(R.id.modName);
        String s = currentModule.getmModName();
        if (s.equals("")) {
            moduleNameTextView.setVisibility(View.GONE);
        } else {
            moduleNameTextView.setVisibility(View.VISIBLE);
            moduleNameTextView.setText(s);
        }
        TextView moduleDetailsTextView = (TextView) listItemView.findViewById(R.id.modDetail);
        String s2 = currentModule.getmModDetails();
        if (s2.equals("")) {
            moduleDetailsTextView.setVisibility(View.GONE);
        } else {
            moduleDetailsTextView.setVisibility(View.VISIBLE);
            moduleDetailsTextView.setText(s2);
        }

        return listItemView;

    }
}
