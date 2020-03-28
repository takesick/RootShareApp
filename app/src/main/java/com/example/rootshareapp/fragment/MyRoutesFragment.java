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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.LocationService;
import com.example.rootshareapp.R;
import com.example.rootshareapp.room.Local_RouteData;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.example.rootshareapp.room.RouteListAdapter;
import com.example.rootshareapp.sqlite.RouteAdapter;
import com.example.rootshareapp.sqlite.RouteContract;
import com.example.rootshareapp.sqlite.RouteOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyRoutesFragment extends Fragment implements RouteListAdapter.OnRouteSelectedListener {

    private View view;
    private RecyclerView mRecyclerView;
    private RouteListAdapter mAdapter;
//    private FirebaseFirestore mFirestore;
//    private Query mQuery;
//    private int LIMIT = 10;

    private LocationDataViewModel mLocationDataViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_route_list, container, false);
        mRecyclerView = view.findViewById(R.id.RouteList);
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

//
        mAdapter = new RouteListAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mLocationDataViewModel.getLatestRoutes().observe(this, new Observer<List<Local_RouteData>>() {
            @Override
            public void onChanged(@Nullable final List<Local_RouteData> local_routeDataList) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setRouteDataList(local_routeDataList);
            }
        });
    }

    @Override
    public void onRouteSelected(View view, int position) {
        int id = (int) view.getTag();
        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.KEY_ROUTE_ID, id);
        Log.e("rep3", String.valueOf(id));
        startActivity(intent);
    }

//    private Cursor getAllItems(SQLiteDatabase db) {
//        return db.query(
//                RouteContract.Routes.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                RouteContract.Routes._ID + " desc"
//        );
//
//    }
}
