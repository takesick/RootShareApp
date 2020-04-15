package com.example.rootshareapp.viewmodel;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreRepositry {

    public static final String TAG = "FIRESTORE_REPOSITORY";
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private Uri mTemporaryIconUri;
    private String mTemporaryDownloadUrl;

    public FirestoreRepositry() {
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void setTemporaryIcon(Uri uri) {
        mTemporaryIconUri = uri;
    }

    public Uri getmTemporaryIconUri() {
        return mTemporaryIconUri;
    }

    public void setTemporaryDownloadUrl(String downloadUrl) {
        mTemporaryDownloadUrl = downloadUrl;
    }

    public String getTemporaryDownloadUrl() {
        return mTemporaryDownloadUrl;
    }

}
