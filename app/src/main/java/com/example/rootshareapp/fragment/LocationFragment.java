package com.example.rootshareapp.fragment;

import android.content.Intent;
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

import com.example.rootshareapp.R;
import com.example.rootshareapp.RouteDetailActivity;
import com.example.rootshareapp.adapter.LocationListAdapter;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class LocationFragment extends Fragment implements View.OnClickListener, LocationListAdapter.OnLocationSelectedListener {

    public static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "LocationList";

    private int route_id;
    private View view;
    private RecyclerView mRecyclerView;
    private LocationListAdapter mLocationListAdapter;
    private LocationDataViewModel mLocationDataViewModel;
    private TextView addSpotBtn, addTagsBtn, addReviewBtn;

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
                mLocationListAdapter.setLocationDataList(local_locationList);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                setSpot(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.e(TAG, "Place: " );
            }
        }
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
//                    Intent intent = new Intent(getActivity(), AddSpotActivity.class);
//                    startActivity(intent);
                    break;

                case R.id.addTagsBtn:

                    break;

                case R.id.addReviewBtn:
//                    FragmentManager fragmentManager = getParentFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container, SignInFragment.newInstance());
//                    fragmentTransaction.commit();
                    break;

                default:
                    break;
            }
        }
    }


    public void setSpot(Place place){
        addSpotBtn.setText(place.getName());
//        MapFragment fragment = (MapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_container);
//        fragment.addSpotMarker();
    }
}
