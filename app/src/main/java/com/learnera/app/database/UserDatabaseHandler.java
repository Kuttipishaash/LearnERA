package com.learnera.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.learnera.app.data.User;

import java.util.ArrayList;

/**
 * Created by shankar on 27/12/17.
 */

public class UserDatabaseHandler extends SQLiteOpenHelper {


    public UserDatabaseHandler(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DatabaseConstants.UsersTable.TABLE_NAME + "( "
                + DatabaseConstants.UsersTable.USER_ID + " VARCHAR(8) PRIMARY KEY , "
                + DatabaseConstants.UsersTable.USER_NAME + " VARCHAR(30),"
                + DatabaseConstants.UsersTable.PASSWORD + " INTEGER(6),"
                + DatabaseConstants.UsersTable.DEPARTMENT + " VARCHAR(2));";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.UsersTable.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.UsersTable.USER_ID, user.getUserName()); // User ID
        values.put(DatabaseConstants.UsersTable.PASSWORD, user.getPassword()); // Password
        values.put(DatabaseConstants.UsersTable.USER_NAME, user.getUser()); // User's name
        values.put(DatabaseConstants.UsersTable.DEPARTMENT, user.getDept()); // Department of user

        // Inserting Row
        db.insert(DatabaseConstants.UsersTable.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single user
    public User getUser(String id) {
        User user;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DatabaseConstants.UsersTable.TABLE_NAME, new String[]{DatabaseConstants.UsersTable.USER_ID,
                        DatabaseConstants.UsersTable.PASSWORD, DatabaseConstants.UsersTable.USER_NAME, DatabaseConstants.UsersTable.DEPARTMENT}, DatabaseConstants.UsersTable.USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        user = new User();
        user.setUserName(cursor.getString(0));
        user.setPassword(Integer.parseInt(cursor.getString(1)));
        user.setUser(cursor.getString(2));
        user.setDept(cursor.getString(3));
        return user;
    }

    // Getting All Users
    public ArrayList<User> getAllUsers() {
        ArrayList<User> usersList = new ArrayList<User>();
        int x = usersList.size();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseConstants.UsersTable.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try {
                    user = new User();
                    user.setUserName(cursor.getString(0));
                    user.setPassword(Integer.parseInt(cursor.getString(2)));
                    user.setUser(cursor.getString(1));
                    user.setDept(cursor.getString(3));
                    // Adding contact to list
                    usersList.add(user);
                } catch (Exception e) {
                    Log.e("catch", "database error " + e.getMessage()); //Just a log message
                }
            } while (cursor.moveToNext());
        }


        // return user list
        Log.d(DatabaseConstants.UsersTable.TAG, "userlist size " + usersList.size());   //Just a log message
        return usersList;
    }

    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + DatabaseConstants.UsersTable.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.UsersTable.USER_NAME, user.getUser());
        values.put(DatabaseConstants.UsersTable.PASSWORD, user.getPassword());
        values.put(DatabaseConstants.UsersTable.DEPARTMENT, user.getDept());

        // updating row
        return db.update(DatabaseConstants.UsersTable.TABLE_NAME, values, DatabaseConstants.UsersTable.USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserName())});
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseConstants.UsersTable.TABLE_NAME, DatabaseConstants.UsersTable.USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserName())});
        db.close();
    }
}
