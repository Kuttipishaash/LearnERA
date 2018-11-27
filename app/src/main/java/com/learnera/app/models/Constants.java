package com.learnera.app.models;

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
    public final static String seatPlanURL = "https://www.rajagiritech.ac.in/stud/ktu/Student/Seating/";

    //SharedPreferences file to store login details
    public static final String PREFERENCE_FILE = "user_preferences";
    public static final String TEMP_USERNAME = "temp_username";
    public static final String TEMP_PASSWORD = "temp_password";
    public static final String TEMP_DIVISION = "temp_division";
    public static final String DIVISION_CODE = "temp_branch_code";
    public static final String MARKS_FETCH_STATUS = "marks_fetch_status";

    //Shared preference file to store date information about offline entry in Attendance
    public static final String DATE_UPDATE_ATTENDANCE = "date_update_attendance";

    public class Firebase {
        public static final String REMOTE_CONFIG_SYLLABUS_VERSION = "syllabus_version";
        public static final int REMOTE_CONFIG_DEFAULT_SYLLABUS_VERSION = 0;
    }


}
