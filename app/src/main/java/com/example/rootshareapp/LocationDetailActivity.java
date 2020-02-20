package com.example.rootshareapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rootshareapp.fragment.LocationDetailFragment;

public class LocationDetailActivity extends AppCompatActivity {

    private static final String TAG = "LocationDetail";

    public static final String KEY_LOCATION_ID = "key_location_id";

    private TextView timeView;
    private TextView accuracyView;
    private TextView latitudeView;
    private TextView longitudeView;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        if(savedInstanceState == null){
            // FragmentManagerのインスタンス生成
            FragmentManager fragmentManager = getSupportFragmentManager();

            // FragmentTransactionのインスタンスを取得
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            // インスタンスに対して張り付け方を指定する
            fragmentTransaction.replace(R.id.container2, new LocationDetailFragment());

            // 張り付けを実行
            fragmentTransaction.commit();
        }


        // Get restaurant ID from extras

//        if (locationsId == null) {
//            throw new IllegalArgumentException("Must pass extra " + KEY_LOCATION_ID);
//        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
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

//    public getLocationId(locationId){
//        return locationId;
//    }

}

