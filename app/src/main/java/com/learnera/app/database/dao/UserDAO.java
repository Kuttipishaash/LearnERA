package com.learnera.app.database.dao;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.models.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query(DatabaseConstants.UsersTable.SELECT_ALL_USERS)
    List<User> getUsers();

}
