package com.example.rootshareapp.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public LocationAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView timeView;
        TextView accuracyView;
        TextView latitudeView;
        TextView longitudeView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            timeView = itemView.findViewById(R.id.created_at);
            accuracyView = itemView.findViewById(R.id.accuracy);
            latitudeView = itemView.findViewById(R.id.lat);
            longitudeView = itemView.findViewById(R.id.lng);
        }
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(mContext);
        View view = Inflater.inflate(R.layout.list_location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        double latitude = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_LATITUDE));
        double longitude = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_LONGITUDE));
        double accuracy = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_ACCURACY));
        String created_at = mCursor.getString(mCursor.getColumnIndex(LocationContract.Locations.COL_CREATED_AT));

        holder.timeView.setText("計測日時：" + String.valueOf(created_at));
        holder.accuracyView.setText("|精度：" + accuracy);
        holder.latitudeView.setText("|緯度："+ latitude);
        holder.longitudeView.setText("|経度："+ longitude);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;
        if (newCursor != null){
            notifyDataSetChanged();
        }
    }
}
