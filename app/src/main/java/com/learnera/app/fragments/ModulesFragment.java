package com.learnera.app.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.learnera.app.R;
import com.learnera.app.SyllabusActivity;
import com.learnera.app.data.Module;
import com.learnera.app.data.ModuleAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModulesFragment extends Fragment {

    //views
    View view;
    ListView mListView;

    //data handlers
    ArrayList<Module> modulesList;
    StringBuffer share;
    ArrayList<String> modules;
    String mTitle;

    //adapters
    private ModuleAdapter moduleAdapter;

    public ModulesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SyllabusActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTitle = "Syllabus";
        view = inflater.inflate(R.layout.fragment_modules, container, false);
        mListView = (ListView) view.findViewById(R.id.modulesListView);
        getlist();
        setHasOptionsMenu(true);

        //To share just one module the user can long press on that module's details and the share dialog appears
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {
                //Share with WhatsApp option only if module details are long pressed. It won't appear on long press of text books, credits or prerequisites
                final AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                final int tempIndex = index + 1;
                build.setTitle("Share");
                build.setMessage("Would you like to share the syllabus of Module " + tempIndex + "?");
                build.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        share = new StringBuffer();
                        share.append("Syllabus of " + modules.get(0) + "\n\nModule " + tempIndex + "\n\n");
                        share.append(modules.get((tempIndex * 2)) + "\n");
                        share.append(modules.get((tempIndex * 2) + 1));
                        share.append("\n\nSyllabus shared using LearnERA");
                        dialogInterface.dismiss();
                        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, share.toString());
                        startActivity(Intent.createChooser(sendIntent, "Share syllabus with"));
                    }
                });
                build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                build.show();   //displays dialog
                return true;
            }

        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_syllabus, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                //Call the function to share the entire syllabus of that subject
                shareFullSyllabus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Populates the list view
    private void getlist() {
        Bundle modulesBundle = getArguments();
        modules = modulesBundle.getStringArrayList("modules");
        mTitle = modules.get(1);

        modulesList = new ArrayList<Module>();

        //use int i = 5 ater putting text books
        for (int i = 2, moduleNumber = 1; i < modules.size(); i += 2, moduleNumber++) {
            modulesList.add(new Module("Module " + moduleNumber, modules.get(i), modules.get(i + 1)));
        }
        moduleAdapter = new ModuleAdapter(getActivity(), modulesList);
        mListView.setAdapter(moduleAdapter);
    }

    //Function to share the entire syllabus of the subject in text format
    private void shareFullSyllabus() {
        share = new StringBuffer();
        share.append("Syllabus of : " + modules.get(0));

        //use int i = 5 ater putting text books
        for (int i = 2, moduleNumber = 1; i < modules.size(); i += 2, moduleNumber++) {
            share.append("\n\nMODULE " + moduleNumber + ":\n");
            if (modules.get(i).equals("")) {
                share.append(modules.get(i + 1));
            } else {
                share.append(modules.get(i) + "\n\n" + modules.get(i + 1));
            }
        }
        share.append("\n\nSyllabus shared using LearnERA");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, share.toString());
        startActivity(Intent.createChooser(sendIntent, "Share syllabus with"));
    }
}
