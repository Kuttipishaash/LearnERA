package com.learnera.app.database.dao;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.models.SubjectDetail;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


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
