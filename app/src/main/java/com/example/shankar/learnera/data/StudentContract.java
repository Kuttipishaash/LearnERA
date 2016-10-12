package com.example.shankar.learnera.data;

import android.provider.BaseColumns;

/**
 * Created by Shankar on 10-10-2016.
 */

public final class StudentContract {

    private StudentContract(){}
    public static final class AnnouncementsGeneral implements BaseColumns{

        public static String TABLE_NAME="announcements";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_ANNOUNCEMENT="announcement";

    }

    public static final class AnnouncementsPersonal implements BaseColumns{

        public static String TABLE_NAME="announcementspersonal";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_ANNOUNCEMENT="announcement";

    }
    public static final class SemesterOne implements BaseColumns{

        public static String TABLE_NAME="semesterOne";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterTwo implements BaseColumns{

        public static String TABLE_NAME="semesterTwo";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterThree implements BaseColumns{

        public static String TABLE_NAME="semesterThree";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterFour implements BaseColumns{

        public static String TABLE_NAME="semesterFour";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterFive implements BaseColumns{

        public static String TABLE_NAME="semesterFive";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterSix implements BaseColumns{

        public static String TABLE_NAME="semesterSix";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterSeven implements BaseColumns{

        public static String TABLE_NAME="semesterSeven";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class SemesterEight implements BaseColumns{

        public static String TABLE_NAME="semesterEight";

        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_SUBJECT="subject";
        public final static String COLUMN_FIRST_INTERNALS="firstInternals";
        public final static String COLUMN_SECOND_INTERNALS="secondInternals";
        public final static String ASSIGNMENTS="assignment";
        public final static String UNIVERSITY_EXAM="universityExam";
        public final static String ATTENDANCE="attendance";
        public final static String TOTAL_CLASSES="totalClasses";
        public final static String CLASSES_ATTENDED="classesAttended";

    }
    public static final class Contacts implements BaseColumns{

        public static String TABLE_NAME="semesterOne";

        public final static String _ID=BaseColumns._ID;
        public final static String FACULTY_NAME="facultyName";
        public final static String DESIGNATION="designation";
        public final static String DEPARTMENT="department";
        public final static String PHONE="phone";
        public final static String EMAIL="email";

    }
    public static final class ActivityPoints implements BaseColumns{

        public static String TABLE_NAME="activityPoints";

        public final static String _ID=BaseColumns._ID;
        public final static String ACTIVITY="activity";
        public final static String CATEGORY="category";
        public final static String SEMESTER="semester";
        public final static String POINTS="points";

    }
}
