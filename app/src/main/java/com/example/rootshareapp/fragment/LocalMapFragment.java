package com.example.rootshareapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.rootshareapp.RouteDetailActivity.KEY_ROUTE_ID;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;

public class LocalMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static final String[] LOCATION_PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE
    };

    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static GoogleMap mMap;
    private MapView mMapView;
    private PolylineOptions polyOptions;
    private View mView;

    private Marker mMarker;
    private List<LatLng> mRoot = new ArrayList<>();
    private ArrayList<Marker> mMarkerList = new ArrayList<>();

    private LocationDataViewModel mLocationDataViewModel;

     // Might be null if Google Play services APK is not available.
//
//    public static MapFragment newInstance() {
//        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public LocalMapFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_map, container, false);
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
            mMapView = mView.findViewById(R.id.local_map);
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

//    データベース情報からマーカーを設置
    private void setMapData() {
        int route_id = getActivity().getIntent().getExtras().getInt(KEY_ROUTE_ID);
        mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        mLocationDataViewModel.getLatestLocations(route_id).observe(this, new Observer<List<Local_Location>>() {
            @Override
            public void onChanged(@Nullable final List<Local_Location> local_locationList) {
                // Update the cached copy of the words in the adapter.
                int n = local_locationList.size();
                for (int i=0; i<n; i++) {
                    Local_Location mLocal_Location = local_locationList.get(i);
                    int position;
                    double latitude, longitude, accuracy;
                    position = i;
                    latitude = mLocal_Location.latitude;
                    longitude = mLocal_Location.longitude;
                    accuracy = mLocal_Location.accuracy;

                    LatLng mLatLng = new LatLng(latitude, longitude);
                    mRoot.add(mLatLng);
                    drawTrace();

                    float color;
                    if (accuracy <= 30){
                        color = HUE_RED;
                    } else if (accuracy > 30){
                        color = HUE_AZURE;
                    } else {
                        color = HUE_VIOLET;
                    }
                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(mLatLng)
                            .title("位置情報" + (position+1))
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                            .draggable(true));
                    mMarkerList.add(mMarker);

                    if(position == 0){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 19));
                    }
                }
            }
        });
    }

    private void drawTrace() {
        // Set a listener for marker click.
        //mMap.setOnMarkerClickListener(this);
        polyOptions = new PolylineOptions();
        //mRunListの要素である緯度経度つまりLatLngをポリラインの要素として登録
        for (LatLng polyLatLng : mRoot) {
            polyOptions.add(polyLatLng);
        }
        polyOptions.color(Color.BLUE);
        polyOptions.width(15);
        polyOptions.geodesic(false);
        mMap.addPolyline(polyOptions);
    }

    public void onMarkerSelected(int position) {
        onMarkerClick(mMarkerList.get(position));
        mMarkerList.get(position).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 20));
    }

//    public void addSpotMarker() {
//        mMarker = mMap.addMarker(new MarkerOptions()
//                .position(mLatLng)
//                .title("位置情報" + (position+1))
//                .icon(BitmapDescriptorFactory.defaultMarker(color))
//                .draggable(true));
//    }
}
