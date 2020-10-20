package com.example.rootshareapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.rootshareapp.fragment.NewPostFragment;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.firebase.firestore.CollectionReference;

public class NewPostActivity extends AppCompatActivity
//        implements NewPostFragment
{

    private LocationDataViewModel mLocationDataViewModel;
    private CollectionReference mPostRef;

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

//    private void hideKeyboard() {
//        View view = getCurrentFocus();
//        if (view != null) {
//            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
}