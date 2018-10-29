package com.learnera.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.models.SubjectDetail;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface SubjectDetailDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubject(SubjectDetail subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubjects(ArrayList<SubjectDetail> subjects);

    @Update
    void updateSubject(SubjectDetail subject);

    @Update
    void updateSubjects(ArrayList<SubjectDetail> subjects);

    @Delete
    void deleteSubject(SubjectDetail subject);

    @Query(DatabaseConstants.SubjectsTable.DELETE_ALL_QUERY)
    void deleteAll();

    @Query(DatabaseConstants.SubjectsTable.SELECT_QUERY + DatabaseConstants.SubjectsTable.SEMESTER + " = :sem AND " + DatabaseConstants.SubjectsTable.BRANCH + " = :branch")
    List<SubjectDetail> getSubjects(int sem, String branch);

    @Query(DatabaseConstants.SubjectsTable.SELECT_ALL_QUERY)
    List<SubjectDetail> getAllSubjects();

}
