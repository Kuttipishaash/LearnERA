package com.learnera.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.learnera.app.data.AnnouncementsDB;
import com.learnera.app.data.StudentContract;

/**
 * Created by Shankar on 13-10-2016.
 */

public class AnnouncementsCursorAdapter extends CursorAdapter{

    /**
     * Constructs a new {@link AnnouncementsCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public AnnouncementsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.announcements_list_item,parent,false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView=(TextView) view.findViewById(R.id.announcementTextView);
        int announcementColumnIndex=cursor.getColumnIndex(StudentContract.AnnouncementsGeneral.COLUMN_ANNOUNCEMENT);
        String announcement=cursor.getString(announcementColumnIndex);
        textView.setText(announcement);
    }
}
