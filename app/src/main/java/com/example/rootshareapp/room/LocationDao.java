package com.example.rootshareapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from location_table WHERE route_id = :route_id ORDER BY _id DESC")
    LiveData<List<Local_LocationData>> getLatestLocations(int route_id);

    @Query("SELECT * from location_table WHERE _id = :location_id")
    LiveData<Local_LocationData> getSelectedLocation(int location_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Local_LocationData.class)
    long insertLocation(Local_LocationData local_locationData);

    @Update
    void updateLocation(Local_LocationData local_locationData);

    @Delete
    void deleteLocation(Local_LocationData local_locationData);

    @Query("DELETE FROM location_table")
    void deleteAllLocations();

}
