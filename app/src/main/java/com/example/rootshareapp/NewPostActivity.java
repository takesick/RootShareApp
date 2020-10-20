package com.example.rootshareapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.rootshareapp.fragment.NewPostFragment;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;

public class NewPostActivity extends AppCompatActivity {

    private LocationDataViewModel mLocationDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new NewPostFragment());
            fragmentTransaction.commit();
        }
    }
}