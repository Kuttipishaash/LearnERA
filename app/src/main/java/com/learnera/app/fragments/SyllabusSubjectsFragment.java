package com.learnera.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.learnera.app.adapters.SyllabusSubjectAdapter;
import com.learnera.app.models.SubjectDetail;
import com.learnera.app.models.User;

import java.util.ArrayList;
import java.util.List;


public class SyllabusSubjectsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Constants
    private static final String TAG = "SyllabusSubjectsFrag";
    private static final long REMOTE_CONFIG_CACHE_EXPIRATION_IN_SEC = 43200L;   // New remote config values will be fetched every 12 hours.

    private User mCurrentUser = new User();

    // Views
    private View mParentView;
    private RecyclerView mSubjectsRecyclerView;
    private Spinner mSemesterSelectSpinner;
    private SyllabusSubjectAdapter syllabusSubjectAdapter;

    //Data
    private ArrayList<SubjectDetail> subjectDetailArrayList;

    // Firebase
    private FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
//    private SubjectDetailDAO subjectDetailDAO;

//    private long localSyllabusVersion;
//    private long fetchedSyllabusVersion;

    public SyllabusSubjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: Set app bar title here
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Function to fetch the subjects from the Firebase RealtimeDatabase
    //TODO: To be removed when implementing offline functionality
    /**
     * Semesters are represented in the firebase database as follows:
     * First year that is semesters 1 and 2 are represented as 0
     * Third semester as 1, Fourth semester as 2 and so on.
     * nth semester is represented as n-2 in the database.
     */
    private void getSubjects(int currentSemester) {
        subjectDetailArrayList.clear();
        syllabusSubjectAdapter.notifyDataSetChanged();
        if (currentSemester == 1 || currentSemester == 2) {
            currentSemester = 0;

        } else {
            currentSemester = currentSemester - 2;
        }
        String semester = String.valueOf(currentSemester);
        String department = mCurrentUser.getDept();
        databaseReference = mFirebaseDatabase.getReference("branches").child(department).child(semester);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot subjectDetailsSnapshot : dataSnapshot.getChildren()) {
                    SubjectDetail subject = subjectDetailsSnapshot.getValue(SubjectDetail.class);
                    assert subject != null;
                    subject.setBranch(mCurrentUser.getDept());
                    subject.setSemester(Integer.parseInt(databaseReference.getKey()));
                    subjectDetailArrayList.add(subject);
                    syllabusSubjectAdapter.notifyDataSetChanged();

                }
                setRecyclerViewContents(mCurrentUser.getSem());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void checkSyllabusUpdates() {
        updateSubjects();

        //TODO: Uncomment the block
        // Firebase RemoteConfig setup
        mRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(REMOTE_CONFIG_SYLLABUS_VERSION, 0);
        mRemoteConfig.setDefaults(defaults);

        //TODO: Remove toast
        localSyllabusVersion = mRemoteConfig.getLong(REMOTE_CONFIG_SYLLABUS_VERSION);
        fetchedSyllabusVersion = localSyllabusVersion;
        final Task<Void> fetch = mRemoteConfig.fetch(5);
        fetch.addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //TODO: Remove debug messages
                    mRemoteConfig.activateFetched();
                    fetchedSyllabusVersion = mRemoteConfig.getLong(REMOTE_CONFIG_SYLLABUS_VERSION);
                    if (fetchedSyllabusVersion > localSyllabusVersion) {
                        updateSubjects();
                    }
                    Log.e(TAG, "RemoteConfig fetch successful");
                    Log.e(TAG, "New value : " + mRemoteConfig.getLong(REMOTE_CONFIG_SYLLABUS_VERSION));
                } else {
                    Log.e(TAG, "RemoteConfig fetch failed");

                }
            }
        });
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_syllabus_subjects, container, false);
        //TODO: Uncomment for offline version
//        subjectDetailDAO = LearnEraRoomDatabase.getDatabaseInstance(getActivity()).subjectDetailDAO();

        initObjects();

        // Getting current user info
        mCurrentUser = User.getLoginInfo(getActivity());

        setSemesterSpinnerContents();

        return mParentView;
    }

    //Initializing views
    private void initObjects() {
        mSubjectsRecyclerView = mParentView.findViewById(R.id.subjects_rec_view_frg_syl);
        mSemesterSelectSpinner = mParentView.findViewById(R.id.spin_semester_frg_syl);

        subjectDetailArrayList = new ArrayList<SubjectDetail>();
        syllabusSubjectAdapter = new SyllabusSubjectAdapter();
        syllabusSubjectAdapter.setmSubjectDetailsList(subjectDetailArrayList);
    }

    //TODO: Fetch new syllabus data from realtime db and update it in local db.
   /* private void updateSubjects() {
        subjectDetailDAO.deleteAll();

        // Fetching data from FirebaseRealtime Database and storing it to the local RoomDatabase
        final DatabaseReference databaseReference = mFirebaseDatabase.getReference("branches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Hello");
                Log.d(TAG, "onDataChange: Hi");
                for (DataSnapshot branchSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot semesterSnapshot : branchSnapshot.getChildren()) {
                        for (DataSnapshot subjectDetailsSnapshot : semesterSnapshot.getChildren()) {
                            SubjectDetail subject = subjectDetailsSnapshot.getValue(SubjectDetail.class);
                            assert subject != null;
                            subject.setBranch(branchSnapshot.getKey());
                            subject.setSemester(Integer.parseInt(semesterSnapshot.getKey()));
                            subjectDetailDAO.insertSubject(subject);
                        }
                    }
                }
                setRecyclerViewContents(mCurrentUser.getSem());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    /**
     * Function to setup spinner to select semester
     */
    private void setSemesterSpinnerContents() {
        int currentSem = mCurrentUser.getSem() - 1;
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
        //Getting subject names to display in list
        getSubjects(position + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setRecyclerViewContents(int currentSemester) {
//        //Getting subject names
//        List<SubjectDetail> subjectsList;
//        String currentDept = mCurrentUser.getDept();
//        if (currentSemester == 1 || currentSemester == 2) {
//            //TODO: Uncomment for offline version
//            List<SubjectDetail> commonFirstYearSubjectsList = subjectDetailDAO.getSubjects(0, currentDept);
//            subjectsList = subjectDetailDAO.getSubjects(currentSemester, currentDept);
//            subjectsList.addAll(commonFirstYearSubjectsList);
//
//        } else {
//
//            subjectsList = subjectDetailDAO.getSubjects(currentSemester, currentDept);
//
//        }
        //Setting list view and adapters
        mSubjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSubjectsRecyclerView.setAdapter(syllabusSubjectAdapter);
    }


}
