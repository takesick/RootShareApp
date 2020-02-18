package com.example.rootshareapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.rootshareapp.db.LocationRoomDatabase;
import com.example.rootshareapp.model.local.Local_LocationData;

public class LocationDetailActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "LocationDetail";

    public static final String KEY_LOCATION_ID = "key_location_id";

    private TextView timeView;
    private TextView accuracyView;
    private TextView latitudeView;
    private TextView longitudeView;

    LocationRoomDatabase db = Room.databaseBuilder(getApplicationContext(), LocationRoomDatabase.class, "Location_database").build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        timeView = findViewById(R.id.created_at);
        accuracyView = findViewById(R.id.accuracy);
        latitudeView = findViewById(R.id.lat);
        longitudeView = findViewById(R.id.lng);

        findViewById(R.id.saveFab).setOnClickListener(this);

        // Get restaurant ID from extras
        String locationsId = getIntent().getExtras().getString(KEY_LOCATION_ID);
        if (locationsId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_LOCATION_ID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveFab:
                onSaveFabClicked(v);
                break;
            case R.id.deleteFab:
                onDeleteClicked(v);
                break;
        }
    }

    private void onSaveFabClicked(View v) {
        UpdateLocationData();
    }

    private void onDeleteClicked(View v) {
        RemoveLocationData();
    }

    private void UpdateLocationData() {

    }

    private void RemoveLocationData() {

    }

//    /**
//     * Listener for the location document ({@link #mLocationRef}).
//     **/
//    @Override
//    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
//        if (e != null) {
//            Log.w(TAG, "location:onEvent", e);
//            return;
//        }
//
//        onLocationDataLoaded(snapshot.toObject(Local_LocationData.class));
//    }
//
//    private void onLocationDataLoaded(Local_LocationData locationData) {
//        timeView.setText(locationData.getCreated_at());
//        accuracyView.setText("|精度：" + locationData.getAccuracy());
//        latitudeView.setText("|緯度："+ locationData.getLatitude());
//        longitudeView.setText("|経度："+ locationData.getLongitude());
//
//    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

