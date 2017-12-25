package com.learnera.app.data;

import java.util.ArrayList;

/**
 * Created by Shankar on 18-10-2017.
 */

public class AttendanceTableRow {
    String date;
    ArrayList<AttendanceTableCells> cells;

    public AttendanceTableRow() {
        this.cells = new ArrayList<AttendanceTableCells>();
    }

    public AttendanceTableRow(String date, ArrayList<AttendanceTableCells> cells) {
        this.date = date;
        this.cells = cells;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<AttendanceTableCells> getCells() {
        return cells;
    }

    public void setCells(ArrayList<AttendanceTableCells> cells) {
        this.cells = cells;
    }

    public void addCell(AttendanceTableCells cell) {
        this.cells.add(cell);
    }
}
