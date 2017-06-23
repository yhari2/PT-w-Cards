package org.ubicomplab.cardviewtest;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by YasasviHari on 6/23/17.
 */


// TODO: Need to fix the scope issues with most of the variables- which variable goes where?

public class PTProvider extends ContentProvider {
    /**
     * Authority of this content provider
     */
    public static final String AUTHORITY = "com.aware.provider.plugin.provider.ProactiveTasks";
    private static UriMatcher sUriMatcher = null; // used to find URI paths in content provider
    private static HashMap<String, String> tableMap = null; //contains database table columns
    private static DatabaseHelper databaseHelper = null; // not sure why the database helper isn't taking...
    private static SQLiteDatabase database = null; // internal reference to the db file itself

    //ContentProvider query indexes
    private static final int EXAMPLE = 1;
    private static final int EXAMPLE_ID = 2;

    //////////////////////


    // Below is a class that is a representation  of the database
    public static final class Example_Data implements BaseColumns {

        private Example_Data(){}


        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/plugin_PT");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aware.plugin.provider.ProactiveTasks";

        /**
         * How each row is identified individually internally in Android (vnd.android.cursor.item). <br/>
         * It needs to be /vnd.aware.plugin.XXX where XXX is your plugin name (no spaces!).
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aware.plugin.provider.ProactiveTasks";

        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICE_ID = "device_id";
        public static final String APP_NAME = "App Name";
        public static final String toBeDisplayed = "Display?";
        //... Add here more columns you might need

        public static final int DATABASE_VERSION=1;// to be updated with each modification of database structure
        public static final String DATABASE_NAME= "plugin_PT.db";
        public static final String[] DATABASE_TABLES = {"plugin_PT"};



        public static final String[] TABLES_FIELDS = {
                Example_Data._ID + " integer primary key autoincrement," +
                        Example_Data.TIMESTAMP + " real default 0," +
                        Example_Data.DEVICE_ID + " text default ''," +
                        "UNIQUE (" + Example_Data.TIMESTAMP + "," + Example_Data.DEVICE_ID + ")"
        };


        /**
         * Initialise the ContentProvider
         */
        private boolean initializeDB() {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
            }
            if( databaseHelper != null && ( database == null || ! database.isOpen()) ) {
                database = databaseHelper.getWritableDatabase();
            }
            return( database != null && databaseHelper != null);
        }
        /**
         * Allow resetting the ContentProvider when updating/reinstalling AWARE
         */
        public static void resetDB(Context c ) {
            Log.d("AWARE", "Resetting " + DATABASE_NAME + "...");

            File db = new File(DATABASE_NAME);
            db.delete();
            databaseHelper = new DatabaseHelper( c, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS);
            if( databaseHelper != null ) {
                database = databaseHelper.getWritableDatabase();
            }
        }

    }

    //////////////////////////// CONTENT PROVIDER FROM HERE DOWN ///////////////////////////

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (!initializeDB()) {
            Log.w(AUTHORITY, "Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case EXAMPLE:
                count = database.delete(DATABASE_TABLES[0], selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case EXAMPLE:
                return Example_Data.CONTENT_TYPE;
            case EXAMPLE_ID:
                return Example_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues new_values) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        ContentValues values = (new_values != null) ? new ContentValues(new_values) : new ContentValues();

        switch (sUriMatcher.match(uri)) {
            case EXAMPLE:
                long _id = database.insert(DATABASE_TABLES[0],Example_Data.DEVICE_ID, values);
                if (_id > 0) {
                    Uri dataUri = ContentUris.withAppendedId(Example_Data.CONTENT_URI, _id);
                    getContext().getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        AUTHORITY = getContext().getPackageName() + ".provider.plugin.example"; //make AUTHORITY dynamic
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DATABASE_TABLES[0], EXAMPLE); //URI for all records
        sUriMatcher.addURI(AUTHORITY, DATABASE_TABLES[0]+"/#", EXAMPLE_ID); //URI for a single record

        tableMap = new HashMap<String, String>();
        tableMap.put(Example_Data._ID, Example_Data._ID);
        tableMap.put(Example_Data.TIMESTAMP, Example_Data.TIMESTAMP);
        tableMap.put(Example_Data.DEVICE_ID, Example_Data.DEVICE_ID);

        return true; //let Android know that the database is ready to be used.
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case EXAMPLE:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(tableMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {
            if (Aware.DEBUG) Log.e(Aware.TAG, e.getMessage());//not sure what to do with this issue
            return null;
        }
    }

    //* there are a ton of issues with this code, mainly with the scope of these variables. Not all of them are in the right places
}
