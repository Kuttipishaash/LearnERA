package com.learnera.app.data;

/**
 * Created by Prejith on 7/4/2017.
 */

public class Constants {

    //URLs
    public final static String loginURL = "https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login";
    public final static String attendanceURL = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Leave.asp";
    public final static String homeURL = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Home.asp";
    public final static String markURL = "https://www.rajagiritech.ac.in/stud/KTU/Parent/Mark.asp";
    public final static String noticesURL = "https://www.rajagiritech.ac.in/Home/notice/notice.asp";
    public final static String seatPlanURL = "https://www.rajagiritech.ac.in/stud/ktu/Student/Seating/RET";

    //SharedPreferences file to store login details
    public static final String PREFERENCE_FILE="user_preferences";

    // Database constants
    // Database Version
    public final static int DATABASE_VERSION = 1;

    // Database Name
    public final static String DATABASE_NAME = "userManager";

    // Contacts table name
    public final static String TABLE_NAME = "users";

    // Contacts Table Columns names
    public final static String KEY_USER_ID = "uID";
    public final static String KEY_USER_NAME = "uName";
    public final static String KEY_PASSWORD = "password";
    public final static String KEY_DEPARTMENT = "department";
    public final static String TAG = "UserDatabaseHandler";
}
