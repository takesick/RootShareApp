package com.example.rootshareapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rootshareapp.model.Local_Location;

import java.util.List;

@Dao
public interface LocationDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from location_table WHERE route_id = :route_id ORDER BY _id DESC")
    LiveData<List<Local_Location>> getLatestLocations(int route_id);

    @Query("SELECT * from location_table WHERE _id = :location_id")
    LiveData<Local_Location> getSelectedLocation(int location_id);

    @Query("SELECT * from location_table WHERE route_id = :route_id ORDER BY _id")
    List<Local_Location> getLocationsWithinRoute(Integer[] route_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Local_Location.class)
    long insertLocation(Local_Location local_location);

    @Update
    void updateLocation(Local_Location local_location);

    @Delete
    void deleteLocation(Local_Location local_location);

    @Query("DELETE FROM location_table")
    void deleteAllLocations();

}
