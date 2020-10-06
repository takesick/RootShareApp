package com.example.rootshareapp.fragment;

import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Post;
import com.example.rootshareapp.model.Public_Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PublicMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener  {

    private static final String TAG = "PostMapFragment";
    private static LocationManager mLocationManager;
    private static GoogleMap mMap;
    private MapView mMapView;
    private Marker mMarker;
    private PolylineOptions polyOptions;
    private View mView;

    private int position = 0;
    private String post_id;

    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private DocumentReference mPostRef, mRouteRef;

    private List<Public_Location> public_locations = new ArrayList<>();
    private List<LatLng> mRoute = new ArrayList<>();

    // Might be null if Google Play services APK is not available.
//
//    public static MapFragment newInstance() {
//        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public PublicMapFragment() {
        // Required empty public constructor
    }

    public static PublicMapFragment newInstance() {
        PublicMapFragment fragment = new PublicMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_map, container, false);
        post_id = getActivity().getIntent().getExtras().getString("post_id");
        mPostRef = mDatabase.collection("posts").document(post_id);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.map_container);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        mPostRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("user_icon"));
                        Post post = document.toObject(Post.class);
                        mRouteRef = post.route_ref;
                        if (mRouteRef != null) {
                            mRouteRef.collection("locations").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    Public_Location public_location = document.toObject(Public_Location.class);
                                                    setLocation(public_location);
                                                    drawTrace();
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void drawTrace() {
        // Set a listener for marker click.
        //mMap.setOnMarkerClickListener(this);
        polyOptions = new PolylineOptions();
        //mRunListの要素である緯度経度つまりLatLngをポリラインの要素として登録
        for (LatLng polyLatLng : mRoute) {
            polyOptions.add(polyLatLng);
        }
        polyOptions.color(Color.BLUE);
        polyOptions.width(15);
        polyOptions.geodesic(false);
        mMap.addPolyline(polyOptions);
    }

    public void setLocation(Public_Location public_location) {
        double latitude, longitude;
        latitude = public_location.latitude;
        longitude = public_location.longitude;
//        accuracy = public_location.accuracy;

        LatLng mLatLng = new LatLng(latitude, longitude);
        mRoute.add(mLatLng);
        addSpotMarker(mLatLng);

        if(position == 0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));
        }
        position++;
    }

    public void addSpotMarker(LatLng latLng) {
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("位置情報" + (position+1))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .draggable(true));
    }
}
