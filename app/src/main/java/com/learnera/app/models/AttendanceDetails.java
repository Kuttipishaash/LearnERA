package com.learnera.app.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.learnera.app.database.DatabaseConstants;
import com.learnera.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = DatabaseConstants.AttendanceTable.TABLE_NAME)
public class AttendanceDetails {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.SUBJECT_LIST)
    @TypeConverters(Utils.class)
    private List<String> subjectList;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.PERCENTAGE_LIST)
    @TypeConverters(Utils.class)
    private List<String> percentageList;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.SUBJECT_CODE_LIST)
    @TypeConverters(Utils.class)
    private List<String> subjectCodeList;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.MISSED_LIST)
    @TypeConverters(Utils.class)
    private List<String> missedList;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.TOTAL_LIST)
    @TypeConverters(Utils.class)
    private List<String> totalList;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.DUTY_LIST)
    @TypeConverters(Utils.class)
    private List<String> dutyAttendanceList;

    @ColumnInfo(name = DatabaseConstants.AttendanceTable.TABLE_ROWS_LIST)
    @TypeConverters(Utils.class)
    private ArrayList<AttendanceTableRow> tableRows;

    public AttendanceDetails() {
        id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public List<String> getPercentageList() {
        return percentageList;
    }

    public void setPercentageList(List<String> percentageList) {
        this.percentageList = percentageList;
    }

    public List<String> getSubjectCodeList() {
        return subjectCodeList;
    }

    public void setSubjectCodeList(List<String> subjectCodeList) {
        this.subjectCodeList = subjectCodeList;
    }

    public List<String> getMissedList() {
        return missedList;
    }

    public void setMissedList(List<String> missedList) {
        this.missedList = missedList;
    }

    public List<String> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<String> totalList) {
        this.totalList = totalList;
    }

    public List<String> getDutyAttendanceList() {
        return dutyAttendanceList;
    }

    public void setDutyAttendanceList(List<String> dutyAttendanceList) {
        this.dutyAttendanceList = dutyAttendanceList;
    }

    public ArrayList<AttendanceTableRow> getTableRows() {
        return tableRows;
    }

    public void setTableRows(ArrayList<AttendanceTableRow> tableRows) {
        this.tableRows = tableRows;
    }
}
