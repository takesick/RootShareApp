package com.example.rootshareapp.sqlite;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.fragment.LocationDetailFragment;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.example.rootshareapp.sqlite.LocationAdapter;
import com.example.rootshareapp.sqlite.LocationContract;
//import com.example.rootshareapp.sqlite.LocationOpenHelper;

import static com.example.rootshareapp.RouteDetailActivity.KEY_ROUTE_ID;


//public class SQLiteRouteDetailFragment extends Fragment implements LocationAdapter.OnLocationSelectedListener {
//
//    private View view;
//    private RecyclerView mRecyclerView;
//    private LocationAdapter mAdapter;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.frag_location_list, container, false);
//        mRecyclerView = view.findViewById(R.id.LocationList);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//
//
////        // Get a new or existing ViewModel from the ViewModelProvider.
////        mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
////
////        // Add an observer on the LiveData returned by getAlphabetizedWords.
////        // The onChanged() method fires when the observed data changes and the activity is
////        // in the foreground.
////        mLocationDataViewModel.getLatestRoot().observe(this, new Observer<List<Local_LocationData>>() {
////            @Override
////            public void onChanged(@Nullable final List<Local_LocationData> local_locationDataList) {
////                // Update the cached copy of the words in the adapter.
////                mAdapter.setLocationDataList(local_locationDataList);
////            }
////        });
////
////        mAdapter = new LocationListAdapter(getActivity());
//
//        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
//        //        データベースファイルの削除
////        SQLiteDatabase.deleteDatabase(context.getDatabasePath(locationOpenHelper.getDatabaseName()));
//        Log.e("tag1", String.valueOf(getActivity().getIntent().getExtras().getLong(KEY_ROUTE_ID)));
//        final SQLiteDatabase db = locationOpenHelper.getWritableDatabase();
//        mAdapter = new LocationAdapter(getContext(),getAllItems(db), this);
//        mRecyclerView.setAdapter(mAdapter);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }
//
//
//    @Override
//    public void onLocationSelected(View view, int position) {
//        long id = (long) view.getTag();
////        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
////        intent.putExtra(RouteDetailActivity.KEY_LOCATION_ID, id);
////        Log.e("tag", String.valueOf(id));
////        startActivity(intent);
////        LocationDataViewModel locationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
////        locationDataViewModel.setSelectedLocation(id);
//        FragmentManager fragmentManager = getParentFragmentManager();
////        LocationDetailFragment mLocationDetailFragment = LocationDetailFragment.newInstance(id);
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.replace(R.id.location_container,mLocationDetailFragment);
////        fragmentTransaction.addToBackStack(null);
////        fragmentTransaction.commit();
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.location_container, new LocationDetailFragment());
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//
//    }
//
//    private Cursor getAllItems(SQLiteDatabase db) {
//        return db.query(
//                LocationContract.Locations.TABLE_NAME,
//                null,
//                LocationContract.Locations.COL_ROUTE_ID + " == ?",
//                new String[]{String.valueOf(getActivity().getIntent().getExtras().getLong(KEY_ROUTE_ID))},
//                null,
//                null,
//                LocationContract.Locations._ID + " desc"
//        );
//    }
//
//}
