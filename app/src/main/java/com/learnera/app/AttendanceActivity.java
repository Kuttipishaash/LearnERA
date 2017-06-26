package com.learnera.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prejith on 7/20/2016.
 */

public class AttendanceActivity extends AppCompatActivity {

    @BindView(R.id.spinner_attendance) Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);

        //TODO: Make a dynamic implementation of the spinner and set default semester to current semester of student

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_semesters,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
/*
        try{
            Connection.Response res = Jsoup.connect("https://www.rajagiritech.ac.in/stud/parent/varify.asp?action=login")
                    .data("user", "U1504046")
                    .data("pass", "15180")
                    .followRedirects(true)
                    .method(Connection.Method.POST)
                    .execute();

            Document doc = Jsoup.connect("https://www.rajagiritech.ac.in/stud/KTU/Parent/Leave.asp?code=2017S4IT")
                    .cookies(res.cookies())
                    .get();

            Elements tables = doc.select("table [width=96%]");
            for(Element table: tables) {
                //		System.out.println("Test");
                Elements rows = table.select("tr");
                for(Element row: rows) {
                    Elements tds = rows.select("td");
                    for(Element td: tds) {
                        System.out.println(td.getElementsByTag("b").text());

                    }
                    for(Element td: tds) {
                        StringBuilder build = new StringBuilder(td.select(":containsOwn(%)").text());
                        String printer = build.delete(0, 2).toString();
                        printer = printer.replaceAll("\\s+", "");
                        System.out.println(printer);
                    }
                    //System.out.println(row.getElementsByTag("b").text());
                    //System.out.println(rows.get(1).select(":containsOwn(%)").text());
                    break;
                }
                break;
            }

            System.out.println("\nDone");
        }
        catch (IOException e)
        {

        }

*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_attendance, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case(R.id.action_attendance_help):
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AttendanceActivity.this);
        alert.setTitle(R.string.action_announcement_help)
                .setMessage(R.string.help_attendance_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}