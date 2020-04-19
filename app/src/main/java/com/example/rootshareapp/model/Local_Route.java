package com.example.rootshareapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "route_table")
public class Local_Route {
    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "created_at")
    @NonNull
    public String created_at;

    @ColumnInfo(name = "uid")
    @NonNull
    public String uid;

    public Local_Route(String title, String created_at, String uid) {
        this.title = title;
        this.created_at = created_at;
        this.uid = uid;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
