package com.example.rootshareapp.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.room.Local_LocationData;
import com.example.rootshareapp.room.LocationDataRepository;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.example.rootshareapp.sqlite.LocationContract;
//import com.example.rootshareapp.sqlite.LocationOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.example.rootshareapp.RouteDetailActivity.KEY_LOCATION_ID;
import static com.example.rootshareapp.RouteDetailActivity.KEY_ROUTE_ID;

public class LocationDetailFragment extends Fragment {

    public static final long LOCATION_DEFAULT = 1;

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

    public static LocationDetailFragment newInstance(int location_id) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LOCATION_ID, location_id);
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
        int id = args.getInt(KEY_LOCATION_ID);
        long location_id = id;
        LocationDataViewModel mLocationDataViewModel = new LocationDataViewModel(getActivity().getApplication(), location_id);
        mLocationDataViewModel.getSelectedLocation(location_id).observe(this, new Observer<Local_LocationData>() {
            @Override
            public void onChanged(@Nullable final Local_LocationData mLocal_LocationData) {
                // Update the cached copy of the words in the adapter.
                timeView.setText("計測日時：" + mLocal_LocationData.created_at);
                accuracyView.setText("|精度：" + mLocal_LocationData.accuracy);
                latitudeView.setText("|緯度：" + mLocal_LocationData.latitude);
                longitudeView.setText("|経度：" + mLocal_LocationData.longitude);
                editCommentView.setText(mLocal_LocationData.comment);
                setLocation(mLocal_LocationData);
            }
        });

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

        int location_id;
        int route_id;
        double latitude, longitude, accuracy;
        String created_at, uid, comment;
        LocationDataViewModel mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        Local_LocationData mLocal_LocationData = getLocaton();

        location_id = mLocal_LocationData.getId();
        latitude = mLocal_LocationData.getLatitude();
        longitude = mLocal_LocationData.getLongitude();
        accuracy = mLocal_LocationData.getAccuracy();
        created_at = mLocal_LocationData.getCreated_at();
        route_id = (int) mLocal_LocationData.getRoute_id();

        uid = mLocal_LocationData.getUid();
        comment = editCommentView.getText().toString();

        Local_LocationData local_locationData = new Local_LocationData();
        local_locationData.set_id(location_id);
        local_locationData.setLatitude(latitude);
        local_locationData.setLongitude(longitude);
        local_locationData.setAccuracy(accuracy);
        local_locationData.setCreated_at(created_at);
        local_locationData.setUid(uid);
        local_locationData.setRoute_id(route_id);
        local_locationData.setComment(comment);

        mLocationDataViewModel.updateLocation(local_locationData);

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

    public void setLocation(Local_LocationData mlocation) {
        mLocal_Location = mlocation;
    }

    public Local_LocationData getLocaton() {
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
