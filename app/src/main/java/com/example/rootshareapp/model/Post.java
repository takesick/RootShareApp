package com.example.rootshareapp.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

public class Post {

    public String id;
    public String uid;
    public String uname;
    public String body;
    public String created_at;
    public String tag;
    public String route_name;
    public DocumentReference route_ref;
    public int starCount = 0;

    public Post() {
    }

    public Post(FirebaseUser user, String created_at, String body) {
        this.uid = user.getUid();
        this.uname = user.getDisplayName();
        this.created_at = created_at;
        this.body = body;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public void setRoute_ref(DocumentReference route_ref) {
        this.route_ref = route_ref;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getBody() {
        return body;
    }

    public DocumentReference getRef() {
        return route_ref;
    }

    public String getRoute_name() {
        return route_name;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("created_at", created_at);
        result.put("body", body);
        result.put("route_ref", route_ref);
        return result;
    }
}
