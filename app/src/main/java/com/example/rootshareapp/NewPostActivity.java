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

//    private Task<Void> addNewPost(final DocumentReference restaurantRef, final Public_Route public_route) {
//        // TODO(developer): Implement
//        return Tasks.forException(new Exception("not yet implemented"));
//    }
//
//    @Override
//    public void onRating(Public_Route public_route) {
//        // In a transaction, add the new rating and update the aggregate totals
//        addNewPost(mPostRef, public_route)
//                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Post added");
//
//                        // Hide keyboard and scroll to top
//                        hideKeyboard();
//                        mRatingsRecycler.smoothScrollToPosition(0);
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Add Post failed", e);
//
//                        // Show failure message and hide keyboard
//                        hideKeyboard();
//                        Snackbar.make(findViewById(android.R.id.content), "Failed to add rating",
//                                Snackbar.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void hideKeyboard() {
//        View view = getCurrentFocus();
//        if (view != null) {
//            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
}