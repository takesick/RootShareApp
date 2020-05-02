package com.example.rootshareapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.adapter.LocationListAdapter;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;

import java.util.List;

public class LocationFragment extends Fragment implements LocationListAdapter.OnLocationSelectedListener {

    private int route_id;
    private View view;
    private RecyclerView mRecyclerView;
    private LocationListAdapter mAdapter;
    private LocationDataViewModel mLocationDataViewModel;
    private Button addSpotBtn, addTagBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_location_list, container, false);
        mRecyclerView = view.findViewById(R.id.LocationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new LocationListAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        route_id = getActivity().getIntent().getExtras().getInt(RouteDetailActivity.KEY_ROUTE_ID);
        Log.e("res2", String.valueOf(route_id));
//
//        // Get a new or existing ViewModel from the ViewModelProvider.
        mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mLocationDataViewModel.getLatestLocations(route_id).observe(this, new Observer<List<Local_Location>>() {
            @Override
            public void onChanged(@Nullable final List<Local_Location> local_locationList) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setLocationDataList(local_locationList);
            }
        });

//        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
//        //        データベースファイルの削除
////        SQLiteDatabase.deleteDatabase(context.getDatabasePath(locationOpenHelper.getDatabaseName()));
//        Log.e("tag1", String.valueOf(getActivity().getIntent().getExtras().getLong(KEY_ROOT_ID)));
//        final SQLiteDatabase db = locationOpenHelper.getWritableDatabase();
//        mAdapter = new LocationAdapter(getContext(),getAllItems(db), this);

//        mStartRecordingFab = getActivity().findViewById(R.id.StartRecordingFab);
//        mStartRecordingFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity().getApplication(), LocationService.class);
//
//                // API 26 以降
//                getActivity().startForegroundService(intent);
//            }
//        });
//
//        mStopRecordingFab  = getActivity().findViewById(R.id.StopRecordingFab);
//        mStopRecordingFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Serviceの停止
//                Intent intent = new Intent(getActivity().getApplication(), LocationService.class);
//                getActivity().stopService(intent);
//            }
//        });

    }


    @Override
    public void onLocationSelected(Local_Location local_location, int position) {
        MapFragment fragment = (MapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_container);
        fragment.onMarkerSelected(position);
        mLocationDataViewModel.setSelectedLocation(local_location);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.location_container, LocationDetailFragment.newInstance());
        fragmentTransaction.commit();

    }
}
