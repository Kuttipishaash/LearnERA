package com.learnera.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.learnera.app.R;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkCalculateFragment extends Fragment {

    private View view;
    private LinearLayout LinearLayoutCheckboxes;
    private CheckBox checkBox;

    public MarkCalculateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mark_calculate, container, false);
        LinearLayoutCheckboxes = (LinearLayout) view.findViewById(R.id.linear_layout_marks_checkbox_group);
        createCheckboxes();
        return view;
    }

    private void createCheckboxes() {
        LinkedHashMap<String, String> checkBoxHashMap = new LinkedHashMap<String, String>();
        checkBoxHashMap.put("1", "Apple");
        checkBoxHashMap.put("2", "Boy");
        checkBoxHashMap.put("3", "Cat");
        checkBoxHashMap.put("4", "Dog");
        checkBoxHashMap.put("5", "Eet");
        checkBoxHashMap.put("6", "Fat");
        checkBoxHashMap.put("7", "Goat");
        checkBoxHashMap.put("8", "Hen");
        checkBoxHashMap.put("9", "I am");
        checkBoxHashMap.put("10", "Jug");
        Set<?> set = checkBoxHashMap.entrySet();
        // Get an iterator
        Iterator<?> i = set.iterator();
        while (i.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) i.next();
            checkBox = new CheckBox(getContext());
            checkBox.setId(Integer.parseInt(mapEntry.getKey().toString()));
            checkBox.setText(mapEntry.getValue().toString());
            LinearLayoutCheckboxes.addView(checkBox);
            /*
            checkBox.setOnClickListener(View.OnClickListener(){

            });


            View.OnClickListener getOnClickDoSomething() {
                return new View.OnClickListener() {
                    public void onClick(View v) {
                        long x = 5;
                    }
                };
*/


        }


    }

}
