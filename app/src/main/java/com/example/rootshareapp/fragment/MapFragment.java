package com.example.rootshareapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.rootshareapp.R;
import com.example.rootshareapp.sqlite.LocationContract;
import com.example.rootshareapp.sqlite.LocationOpenHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.rootshareapp.RouteDetailActivity.KEY_ROOT_ID;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {


    private static final int LOCATION_CODE = 100;
    private static final String[] LOCATION_PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE
    };

    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static LocationManager mLocationManager;
    private static GoogleMap mMap;
    private MapView mMapView;
    private View mView;
    private String mProvider;

    private Marker mMarker;
    private List<LatLng> mRoot = new ArrayList<LatLng>();
    private ArrayList<Marker> mMarkerList = new ArrayList<>();

    private SQLiteDatabase db;

     // Might be null if Google Play services APK is not available.
//
//    public static MapFragment newInstance() {
//        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {
            // 許可を得られたことを確認できた段階で初めてsetContentView()を呼ぶ
            // onMapReady()が走るのはこれ以後になる
            //FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mMapView = mView.findViewById(R.id.map_container);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    //許可されたら位置取得、のところ
    private void setMapFragment() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setMapData();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private Cursor getItems(long root_id) {
        return db.query(
                LocationContract.Locations.TABLE_NAME,
                null,
                LocationContract.Locations.COL_ROOT_ID + " = ?",
                new String[]{ String.valueOf(root_id) },
                null,
                null,
                null
        );
    }

    private void setMapData() {

        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
        db = locationOpenHelper.getWritableDatabase();
        long root_id = getActivity().getIntent().getExtras().getLong(KEY_ROOT_ID);
        Cursor mCursor = null;
        mCursor = getItems(root_id);
        while (mCursor.moveToNext()) {
            Long id = mCursor.getLong(mCursor.getColumnIndex(LocationContract.Locations._ID));;
            double latitude = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_LATITUDE));
            double longitude = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_LONGITUDE));
            double accuracy = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_ACCURACY));

            LatLng mLatLng = new LatLng(latitude, longitude);
            mRoot.add(mLatLng);
            //locationが変わるごとにマークをついか
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(mLatLng)
                    .title("経路" + root_id)
                    .draggable(true));
            //userMark.setTag(0);
            mMarkerList.add(mMarker);
            if(id == 1){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 19));
            }
        }

        mCursor.close();

        //        close db
        db.close();
    }

}
