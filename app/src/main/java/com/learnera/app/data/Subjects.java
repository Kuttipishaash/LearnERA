package com.learnera.app.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.learnera.app.database.DatabaseConstants;

@Entity(tableName = DatabaseConstants.SubjectsTable.TABLE_NAME,
        primaryKeys = {
                DatabaseConstants.SubjectsTable.SUBJECT_CODE,
                DatabaseConstants.SubjectsTable.BRANCH,
                DatabaseConstants.SubjectsTable.SEMESTER
        })
public class Subjects {
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.SUBJECT_CODE)
    private String subjectCode;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.BRANCH)
    private String branch;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.SEMESTER)
    private int semester;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.SUBJECT_NAME)
    private String subjectName;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.URL)
    private String subjectDownloadURL;
    @ColumnInfo(name = DatabaseConstants.SubjectsTable.URL)
    private String downloadedStatus;

    public String getCode() {
        return subjectCode;
    }

    public void setCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getName() {
        return subjectName;
    }

    public void setName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getUrl() {
        return subjectDownloadURL;
    }

    public void setUrl(String subjectDownloadURL) {
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

    public String getDownloadedStatus() {
        return downloadedStatus;
    }

    public void setDownloadedStatus(String downloadedStatus) {
        this.downloadedStatus = downloadedStatus;
    }
}
