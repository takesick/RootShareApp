package com.example.rootshareapp.fragment;

import android.graphics.Color;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;

public class PublicMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener  {

    private static final String TAG = "PostMapFragment";
    private static GoogleMap mMap;
    private MapView mMapView;
    private Marker mMarker;
    private PolylineOptions polyOptions;
    private View mView;

    private int position = 0;
    private String post_id;

    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private DocumentReference mRouteRef;
    private CollectionReference mPostRef;

    private List<Public_Location> mLocations = new ArrayList<>();
    private List<LatLng> mRoute = new ArrayList<>();

    public PublicMapFragment() {
        // Required empty public constructor
    }

    public static PublicMapFragment newInstance() {
        PublicMapFragment fragment = new PublicMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_public_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("aaaa", "こんにちは");
        mMapView = mView.findViewById(R.id.public_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        post_id = getActivity().getIntent().getStringExtra("id");
        mPostRef = mDatabase.collection("posts");
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
        mPostRef.document(post_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        double latitude, longitude, accuracy;
        latitude = public_location.latitude;
        longitude = public_location.longitude;
        accuracy = public_location.accuracy;

        float color;
        if (accuracy <= 30){
            color = HUE_RED;
        } else if (accuracy > 30){
            color = HUE_AZURE;
        } else {
            color = HUE_VIOLET;
        }

        LatLng mLatLng = new LatLng(latitude, longitude);
        mRoute.add(mLatLng);
        addSpotMarker(mLatLng, color);
        setCamera(mLatLng);

        position++;
    }

    public void setCamera(LatLng latLng){
        int zoom;
        double latitude, longitude, max_lat = 0, min_lat = 0, max_long = 0, min_long = 0;
        for(int i=0; i<mLocations.size(); i++) {
            latitude = mLocations.get(i).latitude;
            longitude = mLocations.get(i).longitude;

            if(i==0) {
                max_lat = min_lat = latitude;
                max_long = min_long = longitude;
            }
            if(latitude>max_lat) max_lat = latitude;
            if(latitude<min_lat) min_lat = latitude;
            if(longitude>max_long) max_long = longitude;
            if(longitude<min_long) min_long = longitude;

        }

        double distance = getDistance(max_lat, min_lat, max_long,min_long);
        if(distance>1) {
            zoom = 14;
        } else if(distance> 0.5 && distance<=1) {
            zoom = 15;
        } else if(distance> 0.2 && distance<=0.5) {
            zoom = 17;
        } else {
            zoom = 18;
        }

        if(position == 0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }
    }

    public void addSpotMarker(LatLng latLng, float color) {
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("位置情報" + (position+1))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .draggable(true));
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double dist_km = dist * 60 * 1.1515 * 1.609344;
        return dist_km;
    }

    private double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public double deg2rad(double degrees) {
        return degrees * (Math.PI / 180f);
    }

}
