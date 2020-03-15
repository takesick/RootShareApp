package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.LocationService;
import com.example.rootshareapp.R;
import com.example.rootshareapp.room.Local_LocationData;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.example.rootshareapp.room.LocationListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LocationFragment extends Fragment implements LocationListAdapter.OnLocationSelectedListener {

    private View view;
    private RecyclerView mRecyclerView;
    private LocationListAdapter mAdapter;
    private LocationDataViewModel mLocationDataViewModel;
    private FloatingActionButton mStartRecordingFab, mStopRecordingFab;

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

        // Get a new or existing ViewModel from the ViewModelProvider.
        LocationDataViewModel mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mLocationDataViewModel.getLatestLocations().observe(this, new Observer<List<Local_LocationData>>() {
            @Override
            public void onChanged(@Nullable final List<Local_LocationData> local_locationDataList) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setLocationDataList(local_locationDataList);
            }
        });

//        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
//        //        データベースファイルの削除
////        SQLiteDatabase.deleteDatabase(context.getDatabasePath(locationOpenHelper.getDatabaseName()));
//        Log.e("tag1", String.valueOf(getActivity().getIntent().getExtras().getLong(KEY_ROOT_ID)));
//        final SQLiteDatabase db = locationOpenHelper.getWritableDatabase();
//        mAdapter = new LocationAdapter(getContext(),getAllItems(db), this);

        mStartRecordingFab = getActivity().findViewById(R.id.StartRecordingFab);
        mStartRecordingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), LocationService.class);

                // API 26 以降
                getActivity().startForegroundService(intent);
            }
        });

        mStopRecordingFab  = getActivity().findViewById(R.id.StopRecordingFab);
        mStopRecordingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Serviceの停止
                Intent intent = new Intent(getActivity().getApplication(), LocationService.class);
                getActivity().stopService(intent);
            }
        });

    }


    @Override
    public void onLocationSelected(View view, int position) {
        int id = (int) view.getTag();
//        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
//        intent.putExtra(RouteDetailActivity.KEY_LOCATION_ID, id);

//        startActivity(intent);

        LocationDataViewModel locationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
        locationDataViewModel.setSelectedLocation(id);
        Log.e("tag", String.valueOf(locationDataViewModel.getLatestLocations()));
        FragmentManager fragmentManager = getParentFragmentManager();
//        LocationDetailFragment mLocationDetailFragment = LocationDetailFragment.newInstance(id);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.location_container,mLocationDetailFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.location_container, new LocationDetailFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
