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
public interface RouteDao {

    @Query(" SELECT * from route_table ORDER BY _id DESC ")
    LiveData<List<Local_RouteData>> getLatestRoutes();


    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Local_RouteData.class)
    long insertRoute(Local_RouteData local_routeData);

    @Update
    void updateRoute(Local_RouteData local_routeData);

    @Delete
    void deleteRoute(Local_RouteData local_routeData);

    @Query("DELETE FROM route_table")
    void deleteAllRoutes();
}
