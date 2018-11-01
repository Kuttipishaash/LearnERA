package com.learnera.app.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.learnera.app.adapters.AttendanceTableAdapter;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<AttendanceTableAdapter> table = new MutableLiveData<>();

    public void set(AttendanceTableAdapter adapter) {
        table.setValue(adapter);
    }

    public LiveData<AttendanceTableAdapter> getTable() {
        return table;
    }
}
