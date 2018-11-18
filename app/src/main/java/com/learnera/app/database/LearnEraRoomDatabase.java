package com.learnera.app.database;

import android.content.Context;

import com.learnera.app.database.dao.AttendanceDAO;
import com.learnera.app.database.dao.SubjectDetailDAO;
import com.learnera.app.database.dao.UserDAO;
import com.learnera.app.models.AttendanceDetails;
import com.learnera.app.models.SubjectDetail;
import com.learnera.app.models.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, SubjectDetail.class, AttendanceDetails.class,}, version = DatabaseConstants.DATABASE_VERSION)
public abstract class LearnEraRoomDatabase extends RoomDatabase {
    private static LearnEraRoomDatabase DATABASE_INSTANCE;

    public static LearnEraRoomDatabase getDatabaseInstance(final Context context) {
        if (DATABASE_INSTANCE == null) {
            synchronized (LearnEraRoomDatabase.class) {
                if (DATABASE_INSTANCE == null) {
                    //TODO: Database currently runs on main thread. Move it to another thread if possible.
                    DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LearnEraRoomDatabase.class,
                            DatabaseConstants.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return DATABASE_INSTANCE;
    }

    public abstract SubjectDetailDAO subjectDetailDAO();

    public abstract UserDAO usersDAO();

    public abstract AttendanceDAO attendanceDAO();
}
