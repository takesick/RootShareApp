package com.example.rootshareapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.rootshareapp.adapter.LocationFragmentStatePagerAdapter;
import com.example.rootshareapp.fragment.MapFragment;
import com.example.rootshareapp.fragment.RouteDetailFragment;

public class RouteDetailActivity extends AppCompatActivity {

    private static final String TAG = "LocationDetail";

    public static final String KEY_ROOT_ID = "key_root_id";
    public static final String KEY_LOCATION_ID = "key_location_id";

    ViewPager viewPager;

    private TextView timeView;
    private TextView accuracyView;
    private TextView latitudeView;
    private TextView longitudeView;
    private SQLiteDatabase db;
    private LocationFragmentStatePagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        if (savedInstanceState == null) {
            // FragmentManagerのインスタンス生成

            FragmentManager fragmentManager = getSupportFragmentManager();

            // FragmentTransactionのインスタンスを取得
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            // インスタンスに対して張り付け方を指定する
            fragmentTransaction.add(R.id.location_container, new RouteDetailFragment());

            // 張り付けを実行
            fragmentTransaction.commit();

            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
            MapFragment mapFragment = new MapFragment();
            fragmentTransaction2.add(R.id.map_container, mapFragment);
            fragmentTransaction2.addToBackStack(null);
            fragmentTransaction2.commit();
        }

//        viewPager = (ViewPager) findViewById(R.id.location_container);
//        viewPager.setAdapter(new LocationFragmentStatePagerAdapter( getSupportFragmentManager(),mAdapter));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode != REQUEST_MAIN_ACTIVITY)
//            return;
//        if (resultCode != RESULT_OK)
//            return;


    }
}

