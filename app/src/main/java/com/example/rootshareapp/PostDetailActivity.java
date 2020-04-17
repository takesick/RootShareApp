package com.example.rootshareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PostDetailActivity extends AppCompatActivity {

    public static final String KEY_SNAPSHOT = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
    }
}
