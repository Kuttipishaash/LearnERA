package com.learnera.app.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.learnera.app.data.User;

import java.util.ArrayList;


@Dao
public interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(ArrayList<User> subjects);

    @Update
    void updateUser(User user);

    @Update
    void updateUsers(ArrayList<User> subjects);

    @Delete
    void deleteUser(User user);

    @Query(DatabaseConstants.UsersTable.SELECT_ALL_USERS)
    ArrayList<User> getUsers();

}
