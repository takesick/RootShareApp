package com.example.rootshareapp.Adapter;

import android.content.res.Resources;
import android.util.Log;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_location, parent, false));
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

            timeView.setText("計測日時：" + String.format("%.5f", locationData.getCreated_at()));
            accuracyView.setText("|精度：" + String.format("%.5f", locationData.getAccuracy()));
            latitudeView.setText("|緯度："+ String.format("%.5f", locationData.getLatitude()));
            longitudeView.setText("|経度："+ String.format("%.5f", locationData.getLongitude()));

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
