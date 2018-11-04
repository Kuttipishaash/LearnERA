package com.learnera.app.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.learnera.app.BuildConfig;
import com.learnera.app.R;
import com.learnera.app.adapters.SyllabusSubjectAdapter;
import com.learnera.app.database.LearnEraRoomDatabase;
import com.learnera.app.database.dao.SubjectDetailDAO;
import com.learnera.app.models.SubjectDetail;
import com.learnera.app.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.learnera.app.models.Constants.Firebase.REMOTE_CONFIG_SYLLABUS_VERSION;


public class SyllabusSubjectsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Constants
    private static final String TAG = "SyllabusSubjectsFrag";
    private static final long REMOTE_CONFIG_CACHE_EXPIRATION_IN_SEC = 43200L;   // New remote config values will be fetched every 12 hours.

    private User mCurrentUser = new User();

    // Views
    private View view;
    private RecyclerView mSubjectsRecyclerView;
    private Spinner mSemesterSelectSpinner;

    // Firebase
    private FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private SubjectDetailDAO subjectDetailDAO;

    private long localSyllabusVersion;
    private long fetchedSyllabusVersion;

    private ProgressDialog mProgressDialog;

    public SyllabusSubjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus_subjects, container, false);
        subjectDetailDAO = LearnEraRoomDatabase.getDatabaseInstance(getActivity()).subjectDetailDAO();

        initViews();
        initProgressDialog();
        initToolbar();

        // Getting current user info
        mCurrentUser = User.getLoginInfo(getActivity());
        setRecyclerViewContents(mCurrentUser.getSem());
        setSemesterSpinnerContents();
        checkSyllabusUpdates();
        return view;
    }

    private void checkSyllabusUpdates() {
        Log.i(TAG, "Current remote config value : " + mRemoteConfig.getLong(REMOTE_CONFIG_SYLLABUS_VERSION));

        // Firebase RemoteConfig setup
        mRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(REMOTE_CONFIG_SYLLABUS_VERSION, 0);
        mRemoteConfig.setDefaults(defaults);

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
                    Log.i(TAG, "RemoteConfig fetch successful");
                    Log.i(TAG, "New value : " + mRemoteConfig.getLong(REMOTE_CONFIG_SYLLABUS_VERSION));
                } else {
                    Log.e(TAG, "RemoteConfig fetch failed");

                }
            }
        });
    }

    //Initializing views
    private void initViews() {
        mSubjectsRecyclerView = view.findViewById(R.id.subjects_rec_view_frg_syl);
        mSemesterSelectSpinner = view.findViewById(R.id.spin_semester_frg_syl);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(view.getContext(), R.style.ProgressDialogCustom);
        mProgressDialog.setMessage(getString(R.string.msg_loading_syllabus));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
    }

    private void initToolbar() {
        Toolbar toolbar = view.findViewById(R.id.toolbar_syllabus);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Syllabus");
    }

    private void updateSubjects() {
        subjectDetailDAO.deleteAll();

        // Fetching data from Firebase Realtime Database and storing it to the local RoomDatabase
        final DatabaseReference databaseReference = mFirebaseDatabase.getReference(getString(R.string.firebase_syllabus_fetch_path));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange is executing");
                mProgressDialog.show();
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
                mProgressDialog.hide();
                Log.d(TAG, "onDataChange has finished executing");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Function to setup spinner to select semester
    private void setSemesterSpinnerContents() {
        int currentSem = mCurrentUser.getSem() - 1;
        ArrayList<String> semList = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            semList.add(getResources().getStringArray(R.array.array_semesters)[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(),
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
        setRecyclerViewContents(position + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setRecyclerViewContents(int currentSemester) {
        //Getting subject names
        List<SubjectDetail> subjectsList;
        String currentDept = mCurrentUser.getDept();
        if (currentSemester == 1 || currentSemester == 2) {
            subjectsList = subjectDetailDAO.getSubjects(0, currentDept);
        } else {
            subjectsList = subjectDetailDAO.getSubjects(currentSemester - 2, currentDept);
        }
        //Setting list view and adapters
        SyllabusSubjectAdapter syllabusSubjectAdapter = new SyllabusSubjectAdapter();
        syllabusSubjectAdapter.setmSubjectDetailsList(subjectsList);
        mSubjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSubjectsRecyclerView.setAdapter(syllabusSubjectAdapter);
        syllabusSubjectAdapter.notifyDataSetChanged();
    }
}
