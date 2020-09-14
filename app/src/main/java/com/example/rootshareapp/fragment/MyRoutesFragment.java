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
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.example.rootshareapp.adapter.RouteListAdapter;

import java.util.List;

public class MyRoutesFragment extends Fragment implements RouteListAdapter.OnRouteSelectedListener {

    private View view;
    private RecyclerView mRecyclerView;
    private RouteListAdapter mAdapter;
    private LocationDataViewModel mLocationDataViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        mLocationDataViewModel.getLatestRoutes().observe(this, new Observer<List<Local_Route>>() {
            @Override
            public void onChanged(@Nullable final List<Local_Route> local_routeList) {
                mAdapter.setRouteDataList(local_routeList);
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

    @Override
    public void onDeleteBtnClicked(int route_id) {
//
        RouteDeleteDialogFragment newFragment = RouteDeleteDialogFragment.newInstance(route_id);
        Log.e("dialog", "show");
        newFragment.show(getChildFragmentManager(), "dialog");
    }

    public void onDeleteRoute(int route_id){
        mLocationDataViewModel.deleteRoute(route_id);
        mLocationDataViewModel.deleteLocationInRoute(route_id);
    }


}
