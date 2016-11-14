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

    private void createTables(){
        //create user table
        String userTableQuery = "CREATE TABLE user ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username VARCHAR2(20) UNIQUE, "
                + "password VARCHAR2(20) NOT NULL, "
                + "first_name VARCHAR2(20) NOT NULL,"
                + "last_name VARCHAR2(25),"
                + "email VARCHAR2(30) UNIQUE,"
                + "phone VARCHAR2(20)"
                + "user_type VARCHAR2(10) NOT NULL,"
                + "date_registered TIMESTAMP NOT NULL,"
                + "CONSTRAINT user_type_check check (user_type in ('driver', 'rider')"
                + ");";

        database.execSQL(userTableQuery);

        //create vehicle table
        String vehicleTableQuery = "CREATE TABLE vehicle ("
                + "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "registration_id VARCHAR2(30) UNIQUE, "
                + "capacity INTEGER NOT NULL"
                + ");";

        database.execSQL(vehicleTableQuery);
    }

    private void createDummyRecords(){
        //create rider
        ContentValues values = new ContentValues();
        values.put("username", "test@example.com");
        values.put("password", "HelloWorld1");
        values.put("first_name", "Test");
        values.put("last_name", "User");
        values.put("email", "test@example.com");
        values.put("phone", "123-456-7890");
        values.put("user_type", "rider");
        values.put("date_registered", System.currentTimeMillis());

        database.insert("user", null, values);


        //create driver
        values = new ContentValues();
        values.put("username", "test2@example.com");
        values.put("password", "HelloWorld2");
        values.put("first_name", "Test2");
        values.put("last_name", "User2");
        values.put("email", "test2@example.com");
        values.put("phone", "223-456-7890");
        values.put("user_type", "driver");
        values.put("date_registered", System.currentTimeMillis());

        database.insert("user", null, values);
    }


}
