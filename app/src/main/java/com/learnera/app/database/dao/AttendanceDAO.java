package com.learnera.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.models.AttendanceDetails;

import java.util.List;

@Dao
public interface AttendanceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDetails(AttendanceDetails details);

    @Update
    void updateDetails(AttendanceDetails details);

    @Delete
    void deleteDetails(AttendanceDetails details);

    @Query(DatabaseConstants.AttendanceTable.SELECT_ATTENDANCE)
    List<AttendanceDetails> getAttendance();

    @Query(DatabaseConstants.AttendanceTable.DELETE_EXTRA)
    void deleteAll();

    @Query(DatabaseConstants.AttendanceTable.CLEAR_TABLE)
    void clearTable();

//    @Query(DatabaseConstants.AttendanceTable.DELETE_TOP)
//    void deleteTop();
}
