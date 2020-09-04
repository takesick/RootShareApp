package com.example.rootshareapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rootshareapp.model.Guide;

import java.util.List;

@Dao
public interface GuideDao {

    @Query("SELECT * from guide_table WHERE route_id = :route_id ORDER BY _id DESC")
    LiveData<List<Guide>> getLatestGuides(int route_id);

    @Query("SELECT * from guide_table WHERE _id = :id")
    LiveData<Guide> getSelectedGuide(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Guide.class)
    long insertGuide(Guide guide);

    @Update
    void updateGuide(Guide guide);

    @Delete
    void deleteGuide(Guide guide);

    @Query("DELETE FROM guide_table")
    void deleteAllGuides();
}
