package com.learnera.app.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.learnera.app.data.Subjects;

import java.util.ArrayList;

import javax.security.auth.Subject;

@Dao
public interface SubjectsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubject(Subject subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubjects(ArrayList<Subject> subjects);

    @Update
    void updateSubject(Subject subject);

    @Update
    void updateSubjects(ArrayList<Subject> subjects);

    @Delete
    void deleteSubject(Subject subject);

    @Query(DatabaseConstants.SubjectsTable.DELETE_ALL_QUERY)
    void deleteAll();

    @Query(DatabaseConstants.SubjectsTable.SELECT_QUERY + DatabaseConstants.SubjectsTable.SEMESTER + " = :sem AND " + DatabaseConstants.SubjectsTable.BRANCH + " = :branch")
    ArrayList<Subjects> getSubjects(int sem, String branch);

}
