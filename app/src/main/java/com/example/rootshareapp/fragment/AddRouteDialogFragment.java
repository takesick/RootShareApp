package com.example.rootshareapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.adapter.RouteDialogAdapter;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.model.Route;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AddRouteDialogFragment extends DialogFragment implements View.OnClickListener, RouteDialogAdapter.OnRouteSelectedListener {

    public static final String TAG = "AddRouteDialog";
    private LocationDataViewModel mLocationDataViewModel;
    private RecyclerView mRecyclerView;
    private RouteDialogAdapter mAdapter;
    private OnRouteSelectedListener mOnRouteSelectedListener;

    interface OnRouteSelectedListener {
        void setRoute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_add_route, container, false);

        mRecyclerView = view.findViewById(R.id.dialogRouteList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        view.findViewById(R.id.buttonSubmitRoute).setOnClickListener(this);
        view.findViewById(R.id.buttonCancel).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new RouteDialogAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
        mLocationDataViewModel.getLatestRoutes().observe(this, new Observer<List<Local_Route>>() {
            @Override
            public void onChanged(@Nullable final List<Local_Route> local_routeList) {
                mAdapter.setRouteDataList(local_routeList);
            }
        });

        Log.e("dialog", "show");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnRouteSelectedListener) {
            mOnRouteSelectedListener = (OnRouteSelectedListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmitRoute:
                onSubmitClicked(v);
                break;
            case R.id.buttonCancel:
                onCancelClicked(v);
                break;
        }
    }

    public void onSubmitClicked(View view) {

        if (mOnRouteSelectedListener != null) {
            mOnRouteSelectedListener.setRoute();
        }

        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

    @Override
    public void onRouteSelected(Local_Route local_route) {
        mLocationDataViewModel.setSelectedRoute(local_route);
    }

}
