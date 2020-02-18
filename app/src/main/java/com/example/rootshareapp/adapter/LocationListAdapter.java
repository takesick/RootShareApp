package com.example.rootshareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.local.Local_LocationData;

import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationDataViewHolder> {

class LocationDataViewHolder extends RecyclerView.ViewHolder {
    private final TextView timeView;
    private final TextView accuracyView;
    private final TextView latitudeView;
    private final TextView longitudeView;

    private LocationDataViewHolder(View itemView) {
        super(itemView);
        timeView = itemView.findViewById(R.id.created_at);
        accuracyView = itemView.findViewById(R.id.accuracy);
        latitudeView = itemView.findViewById(R.id.lat);
        longitudeView = itemView.findViewById(R.id.lng);
    }
}

    private final LayoutInflater mInflater;
    private List<Local_LocationData> mLocationDataList; // Cached copy of words

    public LocationListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public LocationDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_location_item, parent, false);
        return new LocationDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationDataViewHolder holder, int position) {
        if (mLocationDataList != null) {
            Local_LocationData current = mLocationDataList.get(position);
            holder.timeView.setText("計測日時：" + current.getCreated_at());
            holder.accuracyView.setText("|精度：" + current.getAccuracy());
            holder.latitudeView.setText("|緯度："+ current.getLatitude());
            holder.longitudeView.setText("|経度："+ current.getLongitude());
        } else {
            // Covers the case of data not being ready yet.
            Log.e("test", "no words");
            holder.timeView.setText("No Created_at");
            holder.accuracyView.setText("No Accuracy");
            holder.latitudeView.setText("No Latitude");
            holder.longitudeView.setText("No Longitude");
        }
    }

    public void setLocationDataList(List<Local_LocationData> locationDataList) {
        mLocationDataList = locationDataList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mLocationDataList != null)
            return mLocationDataList.size();
        else return 0;
    }
}
