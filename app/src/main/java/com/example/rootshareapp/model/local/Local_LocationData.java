package com.example.rootshareapp.model.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class Local_LocationData {
    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo(name = "latitude")
    @NonNull
    public double latitude;

    @ColumnInfo(name = "longitude")
    @NonNull
    public double longitude;

    @ColumnInfo(name = "accuracy")
    @NonNull
    public double accuracy;

    @ColumnInfo(name = "created_at")
    @NonNull
    public String created_at;

    @ColumnInfo(name = "uid")
    @NonNull
    public String uid;

    @ColumnInfo(name = "root_id")
    @NonNull
    public String root_id;

    public Local_LocationData(double latitude, double longitude, double accuracy, String created_at, String uid, String root_id) {
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int get_id() {
        return _id;
    }

    @NonNull
    public int getId() {
        return _id;
    }

    @NonNull
    public double getLatitude() {
        return latitude;
    }

    @NonNull
    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public double getAccuracy() {
        return accuracy;
    }

    @NonNull
    public String getCreated_at() {
        return created_at;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    @NonNull
    public String getRoot_id() {
        return root_id;
    }
}
