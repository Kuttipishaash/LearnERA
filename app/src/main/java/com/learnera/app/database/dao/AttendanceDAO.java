package com.learnera.app.database.dao;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.models.AttendanceDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
