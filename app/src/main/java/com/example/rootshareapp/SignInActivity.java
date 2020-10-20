package com.example.rootshareapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rootshareapp.fragment.SignInFragment;

public class SignInActivity extends AppCompatActivity  {

    public static final int REQUEST_ADD_PHOTO = 1;
    private final int REQUEST_PICK_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.signInfragment, SignInFragment.newInstance());
            fragmentTransaction.commit();
        }
    }
}
