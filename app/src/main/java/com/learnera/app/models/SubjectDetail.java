package com.learnera.app.models;

import com.learnera.app.database.DatabaseConstants;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = DatabaseConstants.SubjectsTable.TABLE_NAME,
        primaryKeys = {
                DatabaseConstants.SubjectsTable.SUBJECT_CODE,
                DatabaseConstants.SubjectsTable.BRANCH,
                DatabaseConstants.SubjectsTable.SEMESTER
        })
public class SubjectDetail {
    @NonNull
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.SUBJECT_CODE)
    private String subjectCode;
    @NonNull
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.BRANCH)
    private String branch;
    @NonNull
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.SEMESTER)
    private int semester;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.SUBJECT_NAME)
    private String subjectName;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.URL)
    private String subjectDownloadURL;
    /*@ColumnInfo(name = DatabaseConstants.SubjectsTable.URL)
    private String downloadedStatus;*/

    @NonNull
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(@NonNull String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDownloadURL() {
        return subjectDownloadURL;
    }

    public void setSubjectDownloadURL(String subjectDownloadURL) {
        this.subjectDownloadURL = subjectDownloadURL;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

   /* public String getDownloadedStatus() {
        return downloadedStatus;
    }

    public void setDownloadedStatus(String downloadedStatus) {
        this.downloadedStatus = downloadedStatus;
    }*/
}
