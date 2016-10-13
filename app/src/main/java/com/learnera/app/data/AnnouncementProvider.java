package com.learnera.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Shankar on 12-10-2016.
 */

public class AnnouncementProvider extends ContentProvider {

    public static final int ANNOUNCEMENTS=100;
    public static final int ANNOUNCEMENTS_ID=101;
    private static final UriMatcher sUriMatcher= new UriMatcher((UriMatcher.NO_MATCH));

    static {

        sUriMatcher.addURI(StudentContract.CONTENT_AUTHORITY, StudentContract.AnnouncementsGeneral.PATH_ANNOUNCEMENTS,ANNOUNCEMENTS);
        sUriMatcher.addURI(StudentContract.CONTENT_AUTHORITY, StudentContract.AnnouncementsGeneral.PATH_ANNOUNCEMENTS+"/#",ANNOUNCEMENTS);
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = AnnouncementProvider.class.getSimpleName();
    private AnnouncementsDB announcementsDB;
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        announcementsDB=new AnnouncementsDB(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db=announcementsDB.getReadableDatabase();
        Cursor cursor=null;
        int match=sUriMatcher.match(uri);
        switch (match){
            case ANNOUNCEMENTS:
                //Returns whole table
                cursor=db.query(StudentContract.AnnouncementsGeneral.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case ANNOUNCEMENTS_ID:
                //Returns particular row with a particular ID only
                selection= StudentContract.AnnouncementsGeneral._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(StudentContract.AnnouncementsGeneral.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query arguement URI "+uri);
        }
        //set notification URI on the cursor,
        //so we know what content URI the cursor was created for.
        //If the data at this URI changes, then we know we need to update the cursor
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match=sUriMatcher.match(uri);
        switch (match){
            case ANNOUNCEMENTS:
                return insertAnnouncement(uri,contentValues);
            default:
                throw new IllegalArgumentException("Cannot query arguement URI "+uri);
        }
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANNOUNCEMENTS:
                return updateAnnouncement(uri, contentValues, selection, selectionArgs);
            case ANNOUNCEMENTS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = StudentContract.AnnouncementsGeneral._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateAnnouncement(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db=announcementsDB.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ANNOUNCEMENTS:
                getContext().getContentResolver().notifyChange(uri,null);
                return db.delete(StudentContract.AnnouncementsGeneral.TABLE_NAME,selection,selectionArgs);
            case ANNOUNCEMENTS_ID:
                selection= StudentContract.AnnouncementsGeneral._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri,null);
                return db.delete(StudentContract.AnnouncementsGeneral.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException(("Deletion is not supported for "+uri));
        }
        }


    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }

    private Uri insertAnnouncement(Uri uri,ContentValues values){
        announcementsDB=new AnnouncementsDB(getContext());
        SQLiteDatabase db=announcementsDB.getWritableDatabase();
        long newRowID=db.insert(StudentContract.AnnouncementsGeneral.TABLE_NAME,null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,newRowID);
    }
    private int updateAnnouncement(Uri uri,ContentValues values,String selection, String[] selectionArgs)
    {
        announcementsDB=new AnnouncementsDB(getContext());
        SQLiteDatabase db=announcementsDB.getWritableDatabase();
        db.update(StudentContract.AnnouncementsGeneral.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }
}

