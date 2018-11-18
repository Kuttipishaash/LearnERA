package com.learnera.app.models;

import com.learnera.app.adapters.AttendanceTableAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<AttendanceTableAdapter> table = new MutableLiveData<>();

    public void set(AttendanceTableAdapter adapter) {
        table.setValue(adapter);
    }

    public LiveData<AttendanceTableAdapter> getTable() {
        return table;
    }
}
