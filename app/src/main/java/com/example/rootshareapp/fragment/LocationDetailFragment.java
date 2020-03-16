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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.room.Local_LocationData;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.example.rootshareapp.sqlite.LocationContract;
import com.example.rootshareapp.sqlite.LocationOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.rootshareapp.RouteDetailActivity.KEY_LOCATION_ID;
import static com.example.rootshareapp.RouteDetailActivity.KEY_ROUTE_ID;

public class LocationDetailFragment extends Fragment {

    public static final long LOCATION_DEFAULT = 1;
    private static long location_id;

    private View view;
    private TextView timeView;
    private TextView accuracyView;
    private TextView latitudeView;
    private TextView longitudeView;
    private EditText editCommentView;
    private FloatingActionButton saveFab, deleteFab;
    private LocationDataViewModel mLocationDataViewModel;
    private Local_LocationData mLocal_locationData;
//
//    int id = this.getArguments().getInt(KEY_LOCATION_ID);

    public static LocationDetailFragment newInstance(int location_id) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_LOCATION_ID, location_id);
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

        Bundle args = getArguments();
        int location_id = args.getInt(KEY_LOCATION_ID);
        mLocationDataViewModel = new LocationDataViewModel(getActivity().getApplication(), location_id);
        mLocal_locationData = mLocationDataViewModel.getSelectedLocation(location_id);

        timeView.setText("計測日時：" + mLocal_locationData.created_at);
        accuracyView.setText("|精度：" + mLocal_locationData.accuracy);
        latitudeView.setText("|緯度：" + mLocal_locationData.latitude);
        longitudeView.setText("|経度：" + mLocal_locationData.longitude);
        editCommentView.setText(mLocal_locationData.comment);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
//
//        mLocationDataViewModel = new LocationDataViewModel(getActivity().getApplication(), location_id);
//        mLocationDataViewModel.updateLocation(mLocal_locationData);
//
//        Bundle args = getArguments();
//        long location_id = args.getLong(KEY_LOCATION_ID);
//        String comment = editCommentView.getText().toString();

//        ContentValues newComment = new ContentValues();
//        newComment.put(LocationContract.Locations.COL_COMMENT, comment);
//        long updateCount = db.update(
//                LocationContract.Locations.TABLE_NAME,
//                newComment,
//                LocationContract.Locations._ID + " = ?",
//                new String[]{ String.valueOf(location_id) }
//        );
//        db.close();

        Intent intent = new Intent(getContext(), RouteDetailActivity.class);
        startActivity(intent);

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
