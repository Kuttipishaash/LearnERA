package com.learnera.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.models.User;

import java.util.List;


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
