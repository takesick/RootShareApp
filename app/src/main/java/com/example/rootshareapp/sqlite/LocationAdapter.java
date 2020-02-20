package com.example.rootshareapp.sqlite;

import android.content.Context;
import android.database.Cursor;

import android.util.Log;
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
    private  OnLocationSelectedListener mOnLocationSelectedListener;
//
    public LocationAdapter(Context context, Cursor cursor, OnLocationSelectedListener onLocationSelectedListener) {
        mContext = context;
        mCursor = cursor;
        mOnLocationSelectedListener = onLocationSelectedListener;
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timeView;
        TextView accuracyView;
        TextView latitudeView;
        TextView longitudeView;
        TextView commentView;
        OnLocationSelectedListener onLocationSelectedListener;

        public LocationViewHolder(@NonNull View itemView, OnLocationSelectedListener onLocationSelectedListener) {
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
            onLocationSelectedListener.onLocationSelected(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(mContext);
        View view = Inflater.inflate(R.layout.list_location_item, parent, false);
        return new LocationViewHolder(view, mOnLocationSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        long id = mCursor.getLong(mCursor.getColumnIndex(LocationContract.Locations._ID));
        double latitude = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_LATITUDE));
        double longitude = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_LONGITUDE));
        double accuracy = mCursor.getDouble(mCursor.getColumnIndex(LocationContract.Locations.COL_ACCURACY));
        String created_at = mCursor.getString(mCursor.getColumnIndex(LocationContract.Locations.COL_CREATED_AT));
        String comment = mCursor.getString(mCursor.getColumnIndex(LocationContract.Locations.COL_COMMENT));

        holder.itemView.setTag(id);
        holder.timeView.setText("計測日時：" + created_at);
        holder.accuracyView.setText("|精度：" + accuracy);
        holder.latitudeView.setText("|緯度：" + latitude);
        holder.longitudeView.setText("|経度：" + longitude);
        holder.commentView.setText("|コメント：" + comment);

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

    public interface OnLocationSelectedListener {
        void onLocationSelected(View view, int position);
    }
}
