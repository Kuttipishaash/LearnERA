package com.learnera.app.data;

import java.util.ArrayList;

/**
 * Created by Shankar on 18-10-2017.
 */

public class AttendenceTableRow {
    String date;
    ArrayList<AttendenceTableCells> cells;

    public AttendenceTableRow() {
        this.cells = new ArrayList<AttendenceTableCells>();
    }

    public AttendenceTableRow(String date, ArrayList<AttendenceTableCells> cells) {
        this.date = date;
        this.cells = cells;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<AttendenceTableCells> getCells() {
        return cells;
    }

    public void setCells(ArrayList<AttendenceTableCells> cells) {
        this.cells = cells;
    }

    public void addCell(AttendenceTableCells cell) {
        this.cells.add(cell);
    }
}
