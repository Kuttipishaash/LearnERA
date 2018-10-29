package com.learnera.app.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.learnera.app.database.dao.SubjectDetailDAO;
import com.learnera.app.database.dao.UserDAO;
import com.learnera.app.models.SubjectDetail;
import com.learnera.app.models.User;

@Database(entities = {User.class, SubjectDetail.class}, version = DatabaseConstants.DATABASE_VERSION)
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
}
