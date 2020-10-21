package com.example.rootshareapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.model.Post;
import com.example.rootshareapp.model.Public_Location;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;

public class PublicMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final String TAG = "PostMapFragment";
    private View mView;
    private static GoogleMap mMap;
    private MapView mMapView;
    private Marker mMarker;

    private String post_id;
    private String route_title;
    private int zoom;
    private int position = 0;
    private int local_route_id;
    private double publicLat, publicLong, publicAccur;
    private double localLat, localLong, localAccur;
    private String localCreated_at, localComment;
    private double max_lat = 0, min_lat = 0, max_long = 0, min_long = 0;
    private LatLng latLng1,latLng2, center;

    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private DocumentReference mRouteRef;
    private CollectionReference mPostRef;
    private LocationDataViewModel mViewModel;

    private List<Public_Location> mLocations = new ArrayList<>();
    private List<LatLng> mRoute = new ArrayList<>();

    private Button downloadBtn;

    public PublicMapFragment() {
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
        mMapView = mView.findViewById(R.id.public_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        downloadBtn = view.findViewById(R.id.download_btn);
        downloadBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        post_id = getActivity().getIntent().getStringExtra("id");
        route_title = getActivity().getIntent().getStringExtra("route_name");
        mPostRef = mDatabase.collection("posts");
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
                            mRouteRef.collection("locations").orderBy("created_at", Query.Direction.ASCENDING).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    Public_Location public_location = document.toObject(Public_Location.class);
                                                    mLocations.add(public_location);
                                                    setLocation(public_location);
                                                    setCamera();
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
        PolylineOptions polyOptions = new PolylineOptions();
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
        publicLat = public_location.latitude;
        publicLong = public_location.longitude;
        publicAccur = public_location.accuracy;

        float color;
        if (publicAccur <= 30){
            color = HUE_RED;
        } else if (publicAccur > 30){
            color = HUE_AZURE;
        } else {
            color = HUE_VIOLET;
        }

        LatLng mLatLng = new LatLng(publicLat, publicLong);
        mRoute.add(mLatLng);
        addSpotMarker(mLatLng, color);
        setMaxAndMinLatLng(mLatLng);
        position++;
    }

    public void setMaxAndMinLatLng(LatLng latLng){
        if(position==0) {
            max_lat = min_lat = latLng.latitude;
            max_long = min_long = latLng.longitude;
        }
        if(publicLat>max_lat) max_lat = latLng.latitude;
        if(publicLat<min_lat) min_lat = latLng.latitude;
        if(publicLong>max_long) max_long = latLng.longitude;
        if(publicLong<min_long) min_long = latLng.longitude;
    }

    public void setCamera(){
        double distance = getMarkerDistance(max_lat, min_lat, max_long,min_long);
        if(distance>1) {
            zoom = 14;
        } else if(distance> 0.5 && distance<=1) {
            zoom = 15;
        } else if(distance> 0.2 && distance<=0.5) {
            zoom = 17;
        } else {
            zoom = 18;
        }
        latLng1 = new LatLng(max_lat, max_long);
        latLng2 = new LatLng(min_lat, min_long);
        center = LatLngBounds.builder().include(latLng1).include(latLng2).build().getCenter();
        if(position == mRoute.size()){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
        }
    }

    public void addSpotMarker(LatLng latLng, float color) {
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("位置情報" + (position+1))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .draggable(true));
    }

    public double getMarkerDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double dist_km = dist * 60 * 1.1515 * 1.609344 / 100000;
        return dist_km;
    }

    private double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public double deg2rad(double degrees) {
        return degrees * (Math.PI / 180f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_btn:
                try {
                    downloadRoute();
                    toastMake("マイルートにキープしました。");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void downloadRoute() throws ExecutionException, InterruptedException {
        mViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        local_route_id = writeRouteDataToDb(route_title, getNowDate(), getUid()).intValue();
        for(int i = 0; i < mLocations.size(); i++) {
            writeLocationDataToDb(mLocations.get(i), local_route_id);
            Log.e("AAAA", mLocations.toString());
        }
    }

    private void writeLocationDataToDb(Public_Location location, int route_id) {

        localLat = location.getLatitude();
        localLong = location.getLongitude();
        localAccur = location.getAccuracy();
        localCreated_at = location.getCreated_at();
        localComment = location.comment;

        Local_Location local_location = new Local_Location(
                localLat,
                localLong,
                publicAccur,
                localCreated_at,
                getUid(),
                route_id,
                localComment
        );
        mViewModel.insertLocation(local_location);

    }

    private Long writeRouteDataToDb(String title, String created_at, String uid) throws ExecutionException, InterruptedException {
        Local_Route local_route = new Local_Route(title, created_at, uid);
        return mViewModel.insertRoute(local_route);
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void toastMake(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.show();
    }

}
