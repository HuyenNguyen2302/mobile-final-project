package com.wpi.cs4518.werideshare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mrampiah on 11/13/16.
 */

public class WeRideshareDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "starbuzz"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database
    private static SQLiteDatabase database = null;

    public WeRideshareDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(database == null)
            database = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Create tables to be used as cache storage
     */
    private void createTables(){
        //create currentUser table
        String userTableQuery = "CREATE TABLE currentUser ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username VARCHAR2(20) UNIQUE, "
                + "password VARCHAR2(20) NOT NULL"
                + ");";

        database.execSQL(userTableQuery);
    }

    /**
     * Create dummy records in cache for testing
     */
    private void createDummyRecords(){
        //create rider
        ContentValues values = new ContentValues();
        values.put("username", "test@example.com");
        values.put("password", "HelloWorld1");

        database.insert("currentUser", null, values);


        //create driver
        values = new ContentValues();
        values.put("username", "test2@example.com");
        values.put("password", "HelloWorld2");

        database.insert("currentUser", null, values);
    }

}
