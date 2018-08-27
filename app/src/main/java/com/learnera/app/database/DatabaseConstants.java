package com.learnera.app.database;

// Database Constants
public class DatabaseConstants {

    // Database Version
    public final static int DATABASE_VERSION = 2;

    // Database Name
    public final static String DATABASE_NAME = "learnera_database";

    public static class SubjectsTable {
        // Contacts Table Name
        public final static String TABLE_NAME = "subjects";
        // Contacts Table Columns Names
        public final static String SUBJECT_CODE = "subject_code";
        public final static String SUBJECT_NAME = "subject_name";
        public final static String URL = "url";
        public final static String DOWNLOADED_STATUS = "downloaded_status";
        public final static String SEMESTER = "semester";
        public final static String BRANCH = "branch";
        public final static String TAG = "Database.SubjectDetail";
        // Contacts Table Select Query
        public final static String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE ";
        public final static String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
        public final static String DELETE_ALL_QUERY = "DELETE FROM " + TABLE_NAME;

    }

    public class UsersTable {
        // Contacts Table Name
        public final static String TABLE_NAME = "users";
        // Contacts Table Columns Names
        public final static String USER_ID = "uid";
        public final static String USER_NAME = "name";
        public final static String PASSWORD = "password";
        public final static String DEPARTMENT = "department";
        public final static String SEMESTER = "semester";
        public final static String TAG = "Database.Users";
        // Select Queries
        public final static String SELECT_ALL_USERS = "SELECT * FROM " + TABLE_NAME;
    }
}
