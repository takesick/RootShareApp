package com.example.rootshareapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "guide_table")
public class Guide {
    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo(name = "created_at")
    @NonNull
    public String created_at;

    @ColumnInfo(name = "spot_name")
    @NonNull
    public String spot_name;

    @ColumnInfo(name = "route_id")
    @NonNull
    public int route_id;

    @ColumnInfo(name = "body")
    @NonNull
    public String body;

    @Ignore
    public Guide() {
    }

    public Guide(String created_at, String spot_name, int route_id, String body) {
        this.created_at = created_at;
        this.spot_name = spot_name;
        this.route_id = route_id;
        this.body = body;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setCreated_at(@NonNull String created_at) {
        this.created_at = created_at;
    }

    public void setSpot_name(@NonNull String spot_name) {
        this.spot_name = spot_name;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setBody(@NonNull String body) {
        this.body = body;
    }

    public int get_id() {
        return _id;
    }

    @NonNull
    public String getCreated_at() {
        return created_at;
    }

    @NonNull
    public String getSpot_name() {
        return spot_name;
    }

    public int getRoute_id() {
        return route_id;
    }

    @NonNull
    public String getBody() {
        return body;
    }
}
