package com.example.rootshareapp.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.rootshareapp.model.Guide;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Local_Route.class, Local_Location.class, Guide.class}, version = 1, exportSchema = false)
public abstract class LocationRoomDatabase extends RoomDatabase {

    public abstract RouteDao routeDao();
    public abstract LocationDao locationDao();
    public abstract GuideDao guideDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile LocationRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LocationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationRoomDatabase.class, "local_database")
//                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(FROM_0_TO_1)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // Populate the database in the background.
                    // If you want to start with more words, just add them.
//                    RouteDao routedao = INSTANCE.routeDao();
//                    LocationDao locationdao = INSTANCE.locationDao();
//
//                    routedao.deleteAllRoutes();
//                    locationdao.deleteAllLocations();
                }
            });
        }
    };

    static final Migration FROM_0_TO_1 = new Migration(0, 1) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
//            database.execSQL("drop table route_table");
//            database.execSQL("drop table location_table");
            database.execSQL("create table " + "route_table" + "(" +
                    RouteContract.Routes._ID + " integer primary key autoincrement," +
                    RouteContract.Routes.COL_CREATED_AT + " String," +
                    RouteContract.Routes.COL_TITLE + " String," +
                    RouteContract.Routes.COL_UID + " String)"
//                    RouteContract.Routes.COL_SPOTS + " Sting)"
            );
            database.execSQL("create table " + "location_table" + "(" +
                    LocationContract.Locations._ID + " integer primary key autoincrement," +
                    LocationContract.Locations.COL_LATITUDE + " double," +
                    LocationContract.Locations.COL_LONGITUDE + " double," +
                    LocationContract.Locations.COL_ACCURACY + " double," +
                    LocationContract.Locations.COL_CREATED_AT + " String," +
                    LocationContract.Locations.COL_COMMENT + " String," +
                    LocationContract.Locations.COL_UID + " String ," +
                    LocationContract.Locations.COL_ROUTE_ID + " integer)"
            );
//            database.execSQL("create table " + "guide_table" + "(" +
//                            GuideContract.Guide._ID + " integer primary key autoincrement," +
//                            GuideContract.Guide.COL_CREATED_AT + " String," +
//                            GuideContract.Guide.COL_SPOT_NAME + " String," +
//                            GuideContract.Guide.COL_BODY + " String," +
//                            GuideContract.Guide.COL_ROUTE_ID + " integer)"
//            );
        }
    };
}