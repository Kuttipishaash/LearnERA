package com.learnera.app.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.data.Module;
import com.learnera.app.data.ModuleAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModulesFragment extends Fragment {

    View view;

    ListView mListView;
    ArrayList<Module> modulesList;
    StringBuffer share;
    ArrayList<String> modules;
    private ModuleAdapter moduleAdapter;

    public ModulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modules, container, false);
        mListView = (ListView) view.findViewById(R.id.modulesListView);
        getlist();


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                //Share with WhatsApp option only if module details are long pressed. It won't appear on long press of text books, credits or prerequisites
                if (index > 2) {
                    final AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                    build.setTitle("Share via WhatsApp");
                    build.setMessage("Would you like to share the syllabus of " + modules.get(0) + " via WhatsApp?");
                    build.setPositiveButton("SHARE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            share = new StringBuffer();
                            share.append("*SYLLABUS OF : " + modules.get(0) + "*");
                            for (int i = 5, moduleNumber = 1; i < modules.size(); i += 2, moduleNumber++) {
                                share.append("\n\n*MODULE " + moduleNumber + ":*\n");
                                if (modules.get(i).equals("")) {
                                    share.append(modules.get(i + 1));
                                } else {
                                    share.append(modules.get(i) + "\n\n" + modules.get(i + 1));
                                }
                            }
                            share.append("\n\n_Syllabus shared using LearnERA_");
                            dialog.dismiss();
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.whatsapp");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, share.toString());
                            try {
                                startActivity(whatsappIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast toast = Toast.makeText(getActivity(), "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                    build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    build.show();

                }
                return true;
            }

        });
        return view;
    }

    //populates the list view
    private void getlist() {
        Bundle modulesBundle = getArguments();
        modules = modulesBundle.getStringArrayList("modules");
        getActivity().setTitle(modules.get(1));
        Module textBooks = new Module("Text Books", "", modules.get(2));
        Module credits = new Module("Credits", "", modules.get(3));
        Module prerequisites = new Module("Prerequisites", "", modules.get(4));
        modulesList = new ArrayList<Module>();
        modulesList.add(textBooks);
        modulesList.add(credits);
        modulesList.add(prerequisites);
        for (int i = 5, moduleNumber = 1; i < modules.size(); i += 2, moduleNumber++) {
            modulesList.add(new Module("Module " + moduleNumber, modules.get(i), modules.get(i + 1)));
        }
        moduleAdapter = new ModuleAdapter(getActivity(), modulesList);
        mListView.setAdapter(moduleAdapter);

    }

}
