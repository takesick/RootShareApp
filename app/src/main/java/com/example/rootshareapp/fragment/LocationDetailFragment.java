package com.example.rootshareapp.fragment;

import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LocationDetailFragment extends Fragment {

    private View view;
    private TextView timeView;
    private TextView accuracyView;
    private TextView latitudeView;
    private TextView longitudeView;
    private EditText editCommentView;
    private FloatingActionButton saveFab, deleteFab;
    private Local_Location mLocal_Location;
    private LocationDataViewModel mLocationDataViewModel;
//
//    int id = this.getArguments().getInt(KEY_LOCATION_ID);

    public static LocationDetailFragment newInstance() {

        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
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

        mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
        mLocal_Location = mLocationDataViewModel.getSelectedLocation();

        timeView.setText("計測日時：" + mLocal_Location.created_at);
        accuracyView.setText("|精度：" + mLocal_Location.accuracy);
        latitudeView.setText("|緯度：" + mLocal_Location.latitude);
        longitudeView.setText("|経度：" + mLocal_Location.longitude);
        editCommentView.setText(mLocal_Location.comment);

        saveFab = getActivity().findViewById(R.id.saveFab);
        deleteFab  = getActivity().findViewById(R.id.deleteFab);

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveFabClicked(v);
            }
        });
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
        String comment;
        comment = editCommentView.getText().toString();
        mLocal_Location.setComment(comment);
        mLocationDataViewModel.updateLocation(mLocal_Location);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.location_container, new LocationFragment());
        fragmentTransaction.commit();

    }

    private void RemoveLocationData() {
        mLocationDataViewModel.deleteLocation(mLocal_Location);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.location_container, new LocationFragment());
        fragmentTransaction.commit();
    }
}
