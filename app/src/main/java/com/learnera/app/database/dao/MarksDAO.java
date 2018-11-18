package com.learnera.app.database.dao;

import com.learnera.app.models.Marks;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

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
