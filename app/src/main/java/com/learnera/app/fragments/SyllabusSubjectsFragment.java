package com.learnera.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.learnera.app.R;
import com.learnera.app.activities.SyllabusActivity;
import com.learnera.app.adapters.SyllabusSubjectAdapter;
import com.learnera.app.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.security.auth.Subject;

public class SyllabusSubjectsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Constants
    private static final String TAG = "SyllabusSubjectsFrag";

    private User mCurrentUser = new User();
    private int mCurrentSemester;

    // Views
    private View mParentView;
    private RecyclerView mSubjectsRecyclerView;
    private Spinner mSemesterSelectSpinner;

    // Firebase
    private FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public SyllabusSubjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((SyllabusActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Syllabus");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSyllabusUpdates();
    }

    private void checkSyllabusUpdates() {
        updateSubjects();

        /*TODO: Uncomment the block
        // Firebase RemoteConfig setup
        mRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("syllabus_version", 0);
        mRemoteConfig.setDefaults(defaults);

        //TODO: Remove toast
        Toast.makeText(getActivity(), mRemoteConfig.getLong("syllabus_version") + "", Toast.LENGTH_SHORT).show();

        final Task<Void> fetch = mRemoteConfig.fetch();
        fetch.addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //TODO: Remove debug messages
                    mRemoteConfig.activateFetched();
                    Log.d(TAG, "RemoteConfig fetch successful");
                    Log.d(TAG, "New value" + mRemoteConfig.getLong("syllabus_version"));
                    updateSubjects();
                }
                else {
                    Log.e(TAG, "RemoteConfig fetch failed");

                }
            }
        });
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_syllabus_subjects, container, false);

        //Initializing views
        mSubjectsRecyclerView = mParentView.findViewById(R.id.subjects_rec_view_frg_syl);
        mSemesterSelectSpinner = mParentView.findViewById(R.id.spin_semester_frg_syl);

        // Getting current user info
        mCurrentUser = User.getLoginInfo(getActivity());
        mCurrentSemester = mCurrentUser.getSem();

        setspinner();

        return mParentView;
    }

    //TODO: Fetch new syllabus data from realtime db and update it in local db.
    private void updateSubjects() {
        // Fetching the list of branches
        ArrayList<String> branchCodesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.branches_code_array)));
        ArrayList<String> branchNamesList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.branches_name_array)));

        final DatabaseReference databaseReference = mFirebaseDatabase.getReference("branches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Hello");
                Log.d(TAG, "onDataChange: Hi");
                for (DataSnapshot branch : dataSnapshot.getChildren()) {
                    for (DataSnapshot semester : branch.getChildren()) {
                        for (DataSnapshot subjectDetails : semester.getChildren()) {
                            Subject subject = subjectDetails.getValue(Subject.class);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Function to setup spinner to select semester
     */
    private void setspinner() {
        int currentSem = mCurrentSemester - 1;
        ArrayList<String> semList = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            semList.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mParentView.getContext(),
                android.R.layout.simple_spinner_item,
                semList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSemesterSelectSpinner.setAdapter(adapter);
        mSemesterSelectSpinner.setSelection(currentSem);
        mSemesterSelectSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCurrentSemester = position + 1;
        getlist();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getlist() {
        //Variables to store user and subject info
        String dept = mCurrentUser.getDept();

        //Getting subject names to display in list
        if (mCurrentSemester == 1 || mCurrentSemester == 2) {
            String currentBranch = "fy";    //First year : as all the branches share a common syllabus
            //if the student is currently studying semester 1 or 2 we need to display the common syllabus for first years
            //TODO: handle syllabus for first years
        } else {
            //for other semesters we display the subjects for that semester and the student's department
            //TODO: handle syllabus for NON first years
        }

        //Getting subject names
        //TODO: Create subject names array list here
        ArrayList<String> subjectsArrayList = new ArrayList<String>();

        //Getting subject codes
        //TODO: Create subject codes array list here
        ArrayList<String> subcodes = new ArrayList<>();

        //Setting list view and adapters
        SyllabusSubjectAdapter syllabusSubjectAdapter = new SyllabusSubjectAdapter();
        syllabusSubjectAdapter.setmSubjectList(subjectsArrayList);
        mSubjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSubjectsRecyclerView.setAdapter(syllabusSubjectAdapter);

        //What happens on selecting a subject from the displayed list

    }


}
