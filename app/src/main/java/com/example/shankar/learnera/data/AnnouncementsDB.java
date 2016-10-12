package com.example.shankar.learnera.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.shankar.learnera.data.StudentContract;

import static android.R.attr.data;

/**
 * Created by Shankar on 10-10-2016.
 */

public class AnnouncementsDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AnnouncementsGeneral.db";
    public static final String SQL_CREATE_ANNOUNCEMENTS_TABLES = "CREATE TABLE "+ StudentContract.AnnouncementsGeneral.TABLE_NAME+" ("+ StudentContract.AnnouncementsGeneral._ID+" INTEGER PRIMARY KEY,"+ StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT+ " TEXT"+" )";

    public AnnouncementsDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ANNOUNCEMENTS_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
