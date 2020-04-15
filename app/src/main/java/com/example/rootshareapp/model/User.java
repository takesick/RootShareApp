package com.example.rootshareapp.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String user_name;
    public String display_name;
    public String email;
    public String user_icon;

    public User() {
        //empty constructor needed
    }

    public User(String user_name, String email) {
        this.user_name = user_name;
        this.email = email;
    }

    public User (String user_name, String display_name, String email, String user_icon) {
        this.user_name = user_name;
        this.display_name = display_name;
        this.email = email;
        this.user_icon = user_icon;
    }

}