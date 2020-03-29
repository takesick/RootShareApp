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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.room.Local_RouteData;
import com.example.rootshareapp.room.LocationDataViewModel;
import com.example.rootshareapp.room.RouteListAdapter;

import java.util.List;

public class MyRoutesFragment extends Fragment implements RouteListAdapter.OnRouteSelectedListener {

    private View view;
    private RecyclerView mRecyclerView;
    private RouteListAdapter mAdapter;
    private LocationDataViewModel mLocationDataViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_my_route, container, false);
        mRecyclerView = view.findViewById(R.id.RouteList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new RouteListAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        mLocationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        mLocationDataViewModel.getLatestRoutes().observe(this, new Observer<List<Local_RouteData>>() {
            @Override
            public void onChanged(@Nullable final List<Local_RouteData> local_routeDataList) {
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

}
