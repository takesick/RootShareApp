package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.Adapter.LocationsAdapter;
import com.example.rootshareapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class RootsFragment extends Fragment implements LocationsAdapter.OnLocationsSelectedListener {

    private RecyclerView mRecyclerView;
    private LocationsAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private int LIMIT = 10;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.roots_frag, container, false);
        mRecyclerView = view.findViewById(R.id.LocationList);;

        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("locations")
                .orderBy("created_at")
                .limit(LIMIT);

        mAdapter = new LocationsAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
//                Snackbar.make(findViewById(android.R.id.content),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

//        mAdapterをmRecyclerViewにセットする
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        Log.e("mAdapter", String.valueOf(mAdapter));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }


    @Override
    public void onLocationsSelected(DocumentSnapshot locationData) {
//        Intent intent = new Intent(this, LocationDetailActivity.class);
//        intent.putExtra(LocatonDetailActivity.KEY_RESTAURANT_ID, locationData.getId());
//
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }
}
