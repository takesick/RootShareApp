package com.example.rootshareapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.LocationData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class LocationsAdapter extends FirestoreAdapter<LocationsAdapter.ViewHolder> {



    public interface OnLocationsSelectedListener {

        void onLocationsSelected(DocumentSnapshot locationData);

    }

    private OnLocationsSelectedListener mListener;

    public LocationsAdapter(Query query, OnLocationsSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_location_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeView;
        TextView accuracyView;
        TextView latitudeView;
        TextView longitudeView;

        public ViewHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.created_at);
            accuracyView = itemView.findViewById(R.id.accuracy);
            latitudeView = itemView.findViewById(R.id.lat);
            longitudeView = itemView.findViewById(R.id.lng);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnLocationsSelectedListener listener) {

            LocationData locationData = snapshot.toObject(LocationData.class);

            timeView.setText("計測日時：" + locationData.getCreated_at());
            accuracyView.setText("|精度：" + locationData.getAccuracy());
            latitudeView.setText("|緯度："+ locationData.getLatitude());
            longitudeView.setText("|経度："+ locationData.getLongitude());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onLocationsSelected(snapshot);
                    }
                }
            });
        }

    }
}
