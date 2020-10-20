package com.example.rootshareapp.fragment;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.AddReviewActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.adapter.LocationListAdapter;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class LocationFragment extends Fragment implements View.OnClickListener, LocationListAdapter.OnLocationSelectedListener {

    public static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final int PHOTO_URI_REQUEST_CODE = 2;
    private static final String TAG = "LocationList";

    private int route_id;
    private View view;
    private RecyclerView mRecyclerView;
    private LocationListAdapter mLocationListAdapter;
    private LocationDataViewModel mLocationDataViewModel;
    private Local_Route mRoute_Data;
    private Uri uri;
    private List<Uri> photos = new ArrayList<>();
    boolean mIsStarted = false;

    private TextView addSpotBtn, addTagsBtn, addReviewBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_location_list, container, false);
        mRecyclerView = view.findViewById(R.id.LocationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addSpotBtn = view.findViewById(R.id.addSpotBtn);
        addTagsBtn = view.findViewById(R.id.addTagsBtn);
        addReviewBtn = view.findViewById(R.id.addReviewBtn);

        addSpotBtn.setOnClickListener(this);
        addTagsBtn.setOnClickListener(this);
        addReviewBtn.setOnClickListener(this);

        mLocationListAdapter = new LocationListAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mLocationListAdapter);
        route_id = getActivity().getIntent().getExtras().getInt(RouteDetailActivity.KEY_ROUTE_ID);
        Log.e("res2", String.valueOf(route_id));

        mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
        mRoute_Data = mLocationDataViewModel.getSelectedRoute();

        mLocationDataViewModel.getLatestLocations(route_id).observe(this, new Observer<List<Local_Location>>() {
            @Override
            public void onChanged(@Nullable final List<Local_Location> local_locationList) {
                mLocationListAdapter.setLocationDataList(local_locationList);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_URI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    // 選択画像が単数の場合の処理
                    uri = data.getData();
                    photos.add(uri);
                } else {
                    // 選択画像が複数の場合の処理
                    ClipData cd = data.getClipData();
                    for (int i = 0; i < cd.getItemCount(); i++) {
                        uri = cd.getItemAt(i).getUri();
                        photos.add(uri);
                    }
                }

            }
        }
    }


    @Override
    public void onLocationSelected(Local_Location local_location, int position) {
        LocalMapFragment fragment = (LocalMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_container);
        fragment.onMarkerSelected(position);
        mLocationDataViewModel.setSelectedLocation(local_location);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.location_container, LocationDetailFragment.newInstance());
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.addSpotBtn:

                    // Set the fields to specify which types of place data to
                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.OVERLAY, fields)
                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                            .setHint("店舗名、または施設名")
                            .build(getActivity());
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

                    break;

                case R.id.addTagsBtn:

                    break;

                case R.id.addReviewBtn:
                    Intent intent1 = new Intent(getActivity(), AddReviewActivity.class);
                    startActivity(intent1);
                    break;

                default:
                    break;
            }
        }
    }


//    public void setSpot(Place place){
//        addSpotBtn.setText(place.getName());
//        mRoute_Data.setSpots(place.toString());
//        mLocationDataViewModel.updateRoute(mRoute_Data);
//        MapFragment fragment = (MapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_container);
//        fragment.addSpotMarker();
//    }
}
