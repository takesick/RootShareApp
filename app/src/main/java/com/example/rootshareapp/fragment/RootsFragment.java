package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.LocationDetailActivity;
import com.example.rootshareapp.LocationService;
import com.example.rootshareapp.R;
import com.example.rootshareapp.sqlite.LocationAdapter;
import com.example.rootshareapp.sqlite.LocationContract;
import com.example.rootshareapp.sqlite.LocationOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RootsFragment extends Fragment implements LocationAdapter.OnLocationSelectedListener {

    private View view;
    private FloatingActionButton mStartRecordingFab, mStopRecordingFab;
    private RecyclerView mRecyclerView;
    private LocationAdapter mAdapter;
//    private FirebaseFirestore mFirestore;
//    private Query mQuery;
//    private int LIMIT = 10;

//    private LocationDataViewModel mLocationDataViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.roots_frag, container, false);
        mRecyclerView = view.findViewById(R.id.LocationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



//        // Get a new or existing ViewModel from the ViewModelProvider.
//        mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
//
//        // Add an observer on the LiveData returned by getAlphabetizedWords.
//        // The onChanged() method fires when the observed data changes and the activity is
//        // in the foreground.
//        mLocationDataViewModel.getLatestRoot().observe(this, new Observer<List<Local_LocationData>>() {
//            @Override
//            public void onChanged(@Nullable final List<Local_LocationData> local_locationDataList) {
//                // Update the cached copy of the words in the adapter.
//                mAdapter.setLocationDataList(local_locationDataList);
//            }
//        });
//
//        mAdapter = new LocationListAdapter(getActivity());

        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
        //        データベースファイルの削除
//        SQLiteDatabase.deleteDatabase(context.getDatabasePath(locationOpenHelper.getDatabaseName()));
        final SQLiteDatabase db = locationOpenHelper.getWritableDatabase();
        mAdapter = new LocationAdapter(getContext(),getAllItems(db), this);
        mRecyclerView.setAdapter(mAdapter);

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
                mAdapter.swapCursor(getAllItems(db));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onLocationSelected(View view, int position) {
        long id = (long) view.getTag();
        Log.e("test2", String.valueOf(id));
        Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
        intent.putExtra(LocationDetailActivity.KEY_LOCATION_ID, id);
        startActivity(intent);
    }

    private Cursor getAllItems(SQLiteDatabase db) {
        return db.query(
                LocationContract.Locations.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                LocationContract.Locations._ID + " desc"

        );

    }
}
