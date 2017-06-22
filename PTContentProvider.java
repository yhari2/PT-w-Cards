package org.ubicomplab.cardviewtest;

import android.content.ContentProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
// adapted from https://code.tutsplus.com/tutorials/android-fundamentals-working-with-content-providers--mobile-5549

/**
 * Created by YasasviHari on 6/21/17.
 */

public class PTProvider extends ContentProvider {

    //provides access, exposes, and manages PTDatabase

    // TODO: 6/22/17 we need to figure out what the authority of the URI will come from the database itself
    // TODO: 6/22/17 what is the data_type?
    // TODO: Change public and private definitions

    private PTDatabase ptdb;

    private String PREFIX = "content://";
    private String AUTHORITY = "";
    private String DATA_TYPE = "";
    private String ID =""; // will vary based on query

    private String URI = PREFIX + AUTHORITY + DATA_TYPE + ID;// this is incomplete, need to figure out more

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ptdb = new PTDatabase();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(PTDatabase.TABLE);

        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case TUTORIAL_ID: // not sure what this is(?)
                queryBuilder.appendWhere(TutListDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case TUTORIALS: // or this (?)
                // no filter
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
}
