package com.example.rootshareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Location;

import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationDataViewHolder> {

    private Context mContext;
    private List<Local_Location> mLocationDataList;
    private  OnLocationSelectedListener mOnLocationSelectedListener;

    public LocationListAdapter(Context context, OnLocationSelectedListener onLocationSelectedListener) {
        mContext = context;
        mOnLocationSelectedListener = onLocationSelectedListener;
    }

    public class LocationDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView timeView;
        private final TextView accuracyView;
        private final TextView latitudeView;
        private final TextView longitudeView;
        private final TextView commentView;
        OnLocationSelectedListener onLocationSelectedListener;

        private LocationDataViewHolder(View itemView, OnLocationSelectedListener onLocationSelectedListener) {
            super(itemView);
            timeView = itemView.findViewById(R.id.created_at);
            accuracyView = itemView.findViewById(R.id.accuracy);
            latitudeView = itemView.findViewById(R.id.lat);
            longitudeView = itemView.findViewById(R.id.lng);
            commentView = itemView.findViewById(R.id.comment);
            this.onLocationSelectedListener = onLocationSelectedListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (onLocationSelectedListener != null && position != RecyclerView.NO_POSITION)
            onLocationSelectedListener.onLocationSelected(mLocationDataList.get(position));
        }
    }


    @Override
    public LocationDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(mContext);
        View itemView = Inflater.inflate(R.layout.list_location_item, parent, false);
        return new LocationDataViewHolder(itemView, mOnLocationSelectedListener);
    }

    @Override
    public void onBindViewHolder(LocationDataViewHolder holder, int position) {
        if (mLocationDataList != null) {
            Local_Location current = mLocationDataList.get(position);
            holder.itemView.setTag(current._id);
            holder.timeView.setText("計測日時：" + current.getCreated_at());
            holder.accuracyView.setText("|精度：" + current.getAccuracy());
            holder.latitudeView.setText("|緯度："+ current.getLatitude());
            holder.longitudeView.setText("|経度："+ current.getLongitude());
            holder.commentView.setText("|コメント：" + current.getComment());
            Log.e("adapter", String.valueOf(current.getId()));

        } else {
            // Covers the case of data not being ready yet.
            Log.e("test", "no words");
            holder.timeView.setText("No Created_at");
            holder.accuracyView.setText("No Accuracy");
            holder.latitudeView.setText("No Latitude");
            holder.longitudeView.setText("No Longitude");
        }
    }

    public void setLocationDataList(List<Local_Location> locationDataList) {
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

    public interface OnLocationSelectedListener {
        void onLocationSelected(Local_Location local_location);
    }
}
