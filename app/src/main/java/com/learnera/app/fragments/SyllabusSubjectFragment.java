package com.learnera.app.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.learnera.app.R;
import com.learnera.app.data.SyllabusSubjectAdapter;
import com.learnera.app.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusSubjectFragment extends Fragment {
    int id, id2;
    View view;
    ListView mListView;
    private SyllabusSubjectAdapter syllabusSubjectAdapter;


    public SyllabusSubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus_subject, container, false);
        mListView = (ListView) view.findViewById(R.id.syllabusSubjectsListView);
        getlist();
        getActivity().setTitle("Syllabus");
        return view;
    }

    private void getlist() {
        User user = new User();
        user = user.getLoginInfo(getActivity());
        String sem = user.getSem();
        String dept = user.getDept();
        String arrnameSubjects;
        String arrnameCodes;
        //Getting subject names to display in list
        if (sem.equals("s1") || sem.equals("s2")) {
            arrnameSubjects = "s1" + "subnames";
            arrnameCodes = "s1" + "sub";
        } else {
            arrnameSubjects = dept + sem + "subnames";
            arrnameCodes = dept + sem + "sub";
        }

        id = getResources().getIdentifier(arrnameSubjects, "array", getActivity().getPackageName());
        List<String> subtemp = Arrays.asList(getResources().getStringArray(id));
        ArrayList<String> sub = new ArrayList<String>(subtemp);

        //Getting subject codes

        id2 = getResources().getIdentifier(arrnameCodes, "array", getActivity().getPackageName());
        List<String> subcodestemp = Arrays.asList(getResources().getStringArray(id2));
        final ArrayList<String> subcodes = new ArrayList<>(subcodestemp);

        syllabusSubjectAdapter = new SyllabusSubjectAdapter(getActivity(), sub);
        mListView.setAdapter(syllabusSubjectAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String subjectName = (String) mListView.getItemAtPosition(position);
                int subjectID = getResources().getIdentifier(subcodes.get(position), "array", getActivity().getPackageName());
                List<String> modulestemp = Arrays.asList(getResources().getStringArray(subjectID));
                ArrayList<String> moduleList = new ArrayList<>();
                moduleList.add(subjectName);
                moduleList.addAll(modulestemp);
                Bundle modules = new Bundle();
                modules.putStringArrayList("modules", moduleList);
                Fragment modulesFragment = new ModulesFragment();
                modulesFragment.setArguments(modules);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_syllabus, modulesFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }


}
