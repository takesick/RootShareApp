package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.room.Local_LocationData;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.example.rootshareapp.RouteDetailActivity.KEY_LOCATION_ID;
import static com.example.rootshareapp.RouteDetailActivity.KEY_ROUTE_ID;

public class LocationDetailFragment extends Fragment {

    public static final String ARG_ID = "Selected Location_ID";
    public static final String ARG_CRESTED_AT = "Selected Location_CRESTED_AT";
    public static final String ARG_ACCURACY = "Selected Location_ACCURACY";
    public static final String ARG_LATITUDE = "Selected Location_LATITUDE";
    public static final String ARG_LONGITUDE = "Selected Location_LONGITUDE";
    public static final String ARG_UID = "Selected Location_UID ";
    public static final String ARG_ROUTE_ID = "Selected Location_ROUTE_ID";
    public static final String ARG_COMMENT = "Selected Location_COMMENT ";

    private View view;
    private TextView timeView;
    private TextView accuracyView;
    private TextView latitudeView;
    private TextView longitudeView;
    private EditText editCommentView;
    private FloatingActionButton saveFab, deleteFab;
    private Local_LocationData mLocal_Location;
//
//    int id = this.getArguments().getInt(KEY_LOCATION_ID);

    public static LocationDetailFragment newInstance(Local_LocationData local_locationData) {

        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, local_locationData._id);
        args.putString(ARG_CRESTED_AT, local_locationData.created_at);
        args.putDouble(ARG_ACCURACY, local_locationData.accuracy);
        args.putDouble(ARG_LATITUDE, local_locationData.latitude);
        args.putDouble(ARG_LONGITUDE, local_locationData.longitude);
        args.putString(ARG_UID, local_locationData.uid);
        args.putInt(ARG_ROUTE_ID, local_locationData.route_id);
        args.putString(ARG_COMMENT, local_locationData.comment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_location_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeView = view.findViewById(R.id.created_at);
        accuracyView = view.findViewById(R.id.accuracy);
        latitudeView = view.findViewById(R.id.lat);
        longitudeView = view.findViewById(R.id.lng);
        editCommentView = view.findViewById(R.id.editComent);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        timeView.setText("計測日時：" + args.getString(ARG_CRESTED_AT));
        accuracyView.setText("|精度：" + args.getDouble(ARG_ACCURACY));
        latitudeView.setText("|緯度：" + args.getDouble(ARG_LATITUDE));
        longitudeView.setText("|経度：" + args.getDouble(ARG_LONGITUDE));
        editCommentView.setText(args.getString(ARG_COMMENT));
        setLocation(args);

        saveFab = getActivity().findViewById(R.id.saveFab);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveFabClicked(v);
            }
        });

        deleteFab  = getActivity().findViewById(R.id.deleteFab);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClicked(v);
            }
        });

    }

    private void onSaveFabClicked(View v) {
        UpdateLocationData();
    }

    private void onDeleteClicked(View v) {
        RemoveLocationData();
    }

    private void UpdateLocationData() {
        String comment;
        LocationDataViewModel mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        Local_LocationData mLocal_LocationData = getLocation();
        comment = editCommentView.getText().toString();
        mLocal_LocationData.setComment(comment);

        mLocationDataViewModel.updateLocation(mLocal_LocationData);

//        ContentValues newComment = new ContentValues();
//        newComment.put(LocationContract.Locations.COL_COMMENT, comment);
//        long updateCount = db.update(
//                LocationContract.Locations.TABLE_NAME,
//                newComment,
//                LocationContract.Locations._ID + " = ?",
//                new String[]{ String.valueOf(location_id) }
//        );
//        db.close();

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.location_container, new LocationFragment());
        fragmentTransaction.commit();

    }

    private void RemoveLocationData() {
//        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
//        db = locationOpenHelper.getWritableDatabase();
//
//        Bundle args = getArguments();
//        long location_id = args.getLong(KEY_LOCATION_ID);
//
//        long deleted = db.delete(
//                LocationContract.Locations.TABLE_NAME,
//                LocationContract.Locations._ID + " = ?",
//                new String[]{ String.valueOf(location_id) }
//        );
//        db.close();

        Intent intent = new Intent(getContext(), RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.KEY_ROUTE_ID, getActivity().getIntent().getExtras().getLong(KEY_ROUTE_ID));
        startActivity(intent);
    }

    public void setLocation(Bundle args) {
        mLocal_Location = new Local_LocationData();
        mLocal_Location.set_id(args.getInt(ARG_ID));
        mLocal_Location.setLatitude(args.getDouble(ARG_LATITUDE));
        mLocal_Location.setLongitude(args.getDouble(ARG_LONGITUDE));
        mLocal_Location.setAccuracy(args.getDouble(ARG_ACCURACY));
        mLocal_Location.setCreated_at(args.getString(ARG_CRESTED_AT));
        mLocal_Location.setUid(args.getString(ARG_UID));
        mLocal_Location.setRoute_id(args.getInt(ARG_ROUTE_ID));
    }

    public Local_LocationData getLocation() {
        return mLocal_Location;
    }

//    private Cursor getItems(long location_id) {
//        return db.query(
//                LocationContract.Locations.TABLE_NAME,
//                null,
//                LocationContract.Locations._ID + " == ?",
//                new String[]{String.valueOf(location_id)},
//                null,
//                null,
//                null
//        );
//    }



}
