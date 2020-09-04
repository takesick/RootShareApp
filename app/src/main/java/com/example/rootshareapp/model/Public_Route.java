package com.example.rootshareapp.model;

public class Public_Route {
    public int _id;
    public String title;
    public String created_at;
    public String uid;
    public String ref;

//    @ColumnInfo(name = "spots")
//    @NonNull
//    public String spots;

    public Public_Route(String title, String created_at, String uid) {
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

//    public void setSpots(@NonNull String spots) {
//        this.spots = spots;
//    }

    public int get_id() {
        return _id;
    }

//    public String getSpots() {
//        return spots;
//    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
