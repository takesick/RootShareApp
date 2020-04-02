package com.example.rootshareapp.model;

import android.net.Uri;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String display_name;
    public String email;
    public Uri icon_path;
    public String introduction;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User (String username, String display_name, String email, Uri icon_path, String introduction) {
        this.username = username;
        this.display_name = display_name;
        this.email = email;
        this.icon_path = icon_path;
        this.introduction = introduction;
    }

}