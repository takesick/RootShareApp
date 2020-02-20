package com.example.rootshareapp.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.rootshareapp.model.local.Local_LocationData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Local_LocationData.class}, version = 1, exportSchema = false)
public abstract class LocationRoomDatabase extends RoomDatabase {

    public abstract LocationDataDao locationDataDao();

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
                            LocationRoomDatabase.class, "Location_database")
                            .addMigrations(FROM_1_TO_2)
                            .addCallback(sRoomDatabaseCallback)
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
                    LocationDataDao dao = INSTANCE.locationDataDao();
                    dao.deleteAll();
                }
            });
        }
    };

    static final Migration FROM_1_TO_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `location_table` " +
                    "(`id` INTEGER, " +
                    "`created_at` TEXT , `uid` TEXT , `root_id` TEXT ," +
                    " PRIMARY KEY(`id`))");
        }
    };
}