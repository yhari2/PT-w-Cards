package org.ubicomplab.cardviewtest;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by YasasviHari on 6/21/17.
 */

public class PTDatabase extends SQLiteOpenHelper {
    // dummy database
    private static final String DEBUG_TAG = "PTDatabase";
    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "PT_Data";
    public static final String TABLE = "TABLE";
    public static final String APP_NAME = "APP NAME";
    public static final String JSON = "JSON";
    public static final String DISPLAY = "DISPLAY?";
    //the above will be used frequently throughout the rest of code,
    //so best to break it up
    private static final String DB_SCHEMA = "create table " + TABLE
            + " (" + APP_NAME + " integer primary key autoincrement, " + JSON
            + " text not null, " + DISPLAY + " text not null);";// string representation of the db


    public PTDatabase() {
        super(context, DB_NAME, null, DB_VERSION);//still not sure what to do with this context parameter
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_SCHEMA);//creates the necessary database
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        //existing content will be lost, but replaced by next version of content
        onCreate(db);
        // i suspect that this will be the most important since we don't really have a means to implement this
        // in the PT Provider
    }
}