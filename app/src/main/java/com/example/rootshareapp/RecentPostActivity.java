package com.example.rootshareapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RecentPostActivity extends AppCompatActivity {

    public static final String KEY_SNAPSHOT = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_post);
    }
}
