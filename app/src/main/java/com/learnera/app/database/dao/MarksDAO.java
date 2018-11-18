package com.learnera.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.learnera.app.models.Marks;

import java.util.List;

@Dao
public interface MarksDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMarks(Marks marks);

    @Update
    void updateMarks(Marks marks);

    @Delete
    void deleteMarks(Marks marks);

    List<Marks> getMarks();

    void clearTable();
}
