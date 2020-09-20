package com.example.rootshareapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rootshareapp.model.Local_Route;

import java.util.List;

@Dao
public interface RouteDao {

    @Query(" SELECT * from route_table ORDER BY _id DESC ")
    LiveData<List<Local_Route>> getLatestRoutes();

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Local_Route.class)
    long insertRoute(Local_Route local_route);

    @Update
    void updateRoute(Local_Route local_route);

    @Delete
    void deleteRoute(Local_Route local_route);

    @Query("DELETE FROM route_table WHERE _id = :route_id")
    void deleteRoute(int route_id);

    @Query("DELETE FROM route_table")
    void deleteAllRoutes();
}
