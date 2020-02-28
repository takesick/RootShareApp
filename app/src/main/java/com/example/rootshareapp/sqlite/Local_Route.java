package com.example.rootshareapp.sqlite;

public class Local_Route {

    public static final String FIELD_DESTINATION = "destination";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_CREATE_AT = "created_at";
    public static final String FIELD_UID = "uid";

    public int id;
    public String title;
    public String created_at;
    public String uid;

    public Local_Route() {}

    public Local_Route(String title, String created_at, String uid) {
        this.title = title;
        this.created_at = created_at;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
