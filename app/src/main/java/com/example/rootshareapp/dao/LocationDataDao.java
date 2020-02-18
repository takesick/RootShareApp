package com.example.rootshareapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rootshareapp.model.local.Local_LocationData;

import java.util.List;

@Dao
public interface LocationDataDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query(" SELECT * from location_table WHERE _id = 1 ORDER BY _id DESC ")
    LiveData<List<Local_LocationData>> getLatestRoot();



    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Local_LocationData.class)
    void insert(Local_LocationData local_locationData);

    @Update
    void update(Local_LocationData local_locationData);

    @Delete
    void delete(Local_LocationData local_locationData);

    @Query("DELETE FROM location_table")
    void deleteAll();
}
