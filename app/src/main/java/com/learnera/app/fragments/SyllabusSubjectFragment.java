package com.learnera.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.learnera.app.R;
import com.learnera.app.SyllabusActivity;
import com.learnera.app.data.SyllabusSubjectAdapter;
import com.learnera.app.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusSubjectFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    User user = new User();
    String sem;
    //data
    int id, id2;
    //views
    private View view;
    private ListView mListView;
    private Spinner spinner;
    //adapters
    private SyllabusSubjectAdapter syllabusSubjectAdapter;

    public SyllabusSubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SyllabusActivity) getActivity()).getSupportActionBar().setTitle("Syllabus");
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
        spinner = (Spinner) view.findViewById(R.id.syllabus_semester_spinner);
        user = user.getLoginInfo(getActivity());
        sem = user.getSem();
        setspinner();

        return view;
    }

    private void setspinner() {
        int currentSem = Integer.parseInt(sem.substring(1)) - 1;
        ArrayList<String> semList = new ArrayList<>();
        for (int i = 0; i <= currentSem; i++) {
            semList.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_spinner_item,
                semList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(currentSem);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sem = "s" + String.valueOf(position);
        getlist();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getlist() {
        //Variables to store user and subject info

        String dept = user.getDept();
        String arrnameSubjects;
        String arrnameCodes;

        //Getting subject names to display in list
        if (sem.equals("s1") || sem.equals("s2")) {
            //if the student is currently studying semester 1 or 2 we need to display the common syllabus for first years
            arrnameSubjects = "s1" + "subnames";
            arrnameCodes = "s1" + "sub";
        } else {
            //for other semesters we display the subjects for that semester and the student's department
            arrnameSubjects = dept + sem + "subnames";
            arrnameCodes = dept + sem + "sub";
        }

        //Getting subject names
        id = getResources().getIdentifier(arrnameSubjects, "array", getActivity().getPackageName());
        List<String> subtemp = Arrays.asList(getResources().getStringArray(id));
        ArrayList<String> sub = new ArrayList<String>(subtemp);

        //Getting subject codes
        id2 = getResources().getIdentifier(arrnameCodes, "array", getActivity().getPackageName());
        List<String> subcodestemp = Arrays.asList(getResources().getStringArray(id2));
        final ArrayList<String> subcodes = new ArrayList<>(subcodestemp);

        //Setting list view and adapters
        syllabusSubjectAdapter = new SyllabusSubjectAdapter(getActivity(), sub);
        mListView.setAdapter(syllabusSubjectAdapter);

        //What happens on selecting a subject from the displayed list
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
