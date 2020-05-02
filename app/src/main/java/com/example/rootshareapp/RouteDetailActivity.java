package com.example.rootshareapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.rootshareapp.fragment.LocationFragment;
import com.example.rootshareapp.fragment.MapFragment;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;

import java.util.Objects;

public class RouteDetailActivity extends AppCompatActivity {

    private static final String TAG = "LocationDetail";
    public static final String KEY_ROUTE_ID = "key_route_id";
    public static final String KEY_LOCATION_ID = "key_location_id";

    private LocationDataViewModel mLocationDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        mLocationDataViewModel = ViewModelProviders.of(this).get(LocationDataViewModel.class);
        EditText edit =findViewById(R.id.setTitle);
        if (savedInstanceState == null) {

            // FragmentManagerのインスタンス生成
            FragmentManager fragmentManager = getSupportFragmentManager();
            // FragmentTransactionのインスタンスを取得
            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            MapFragment mapFragment = new MapFragment();
            // インスタンスに対して張り付け方を指定する
            fragmentTransaction1.add(R.id.map_container, mapFragment);
            // 張り付けを実行
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode != REQUEST_MAIN_ACTIVITY)
//            return;
//        if (resultCode != RESULT_OK)
//            return;

    }
}

