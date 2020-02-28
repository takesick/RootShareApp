package com.example.rootshareapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "location.db";
    public static final int DB_VERSION = 1;
    public static final String CREATE_TABLE =
            "create table " + LocationContract.Locations.TABLE_NAME + "(" +
                    LocationContract.Locations._ID + " integer primary key autoincrement," +
                    LocationContract.Locations.COL_LATITUDE + " double," +
                    LocationContract.Locations.COL_LONGITUDE + " double," +
                    LocationContract.Locations.COL_ACCURACY + " double," +
                    LocationContract.Locations.COL_CREATED_AT + " String," +
                    LocationContract.Locations.COL_COMMENT + " String," +
                    LocationContract.Locations.COL_UID + " String ," +
                    LocationContract.Locations.COL_ROOT_ID + " integer)";

    public static final String DROP_TABLE =
            "drop table if exists " + LocationContract.Locations.TABLE_NAME;


    public LocationOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        create table
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        drop table
        db.execSQL(DROP_TABLE);
//        onCreate
        onCreate(db);

    }

}
