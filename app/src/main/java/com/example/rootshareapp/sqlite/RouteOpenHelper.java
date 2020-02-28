package com.example.rootshareapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "route.db";
    public static final int DB_VERSION = 1;
    public static final String CREATE_TABLE =
            "create table " + RouteContract.Routes.TABLE_NAME + "(" +
                    RouteContract.Routes._ID + " integer primary key autoincrement," +
                    RouteContract.Routes.COL_CREATED_AT + " String," +
                    RouteContract.Routes.COL_TITLE + " String," +
                    RouteContract.Routes.COL_UID + " String)";

    public static final String DROP_TABLE =
            "drop table if exists " + RouteContract.Routes.TABLE_NAME;


    public RouteOpenHelper(Context context){
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
