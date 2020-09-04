package com.example.rootshareapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.rootshareapp.fragment.LocationFragment;
import com.example.rootshareapp.fragment.LocalMapFragment;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class RouteDetailActivity extends AppCompatActivity {

    public static final String KEY_ROUTE_ID = "key_route_id";
    public static final String KEY_LOCATION_ID = "key_location_id";

    private LocationDataViewModel mLocationDataViewModel;
    private EditText routeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        routeTitle =findViewById(R.id.setTitle);

        mLocationDataViewModel = ViewModelProviders.of(this).get(LocationDataViewModel.class);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(getApplicationContext());

        if (savedInstanceState == null) {

            // FragmentManagerのインスタンス生成
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.add(R.id.map_container, new LocalMapFragment());
            fragmentTransaction1.commit();
            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
            fragmentTransaction2.add(R.id.location_container, new LocationFragment());
            fragmentTransaction2.commit();

        }
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
//    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}

