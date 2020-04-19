package com.example.rootshareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Route;

import java.util.List;

public class RouteDialogAdapter extends RecyclerView.Adapter<RouteDialogAdapter.RouteDataViewHolder> {

    private Context mContext;
    private List<Local_Route> mRouteDataList;
    private  OnRouteSelectedListener mOnRouteSelectedListener;

    public RouteDialogAdapter(Context context, OnRouteSelectedListener onRouteSelectedListener) {
        mContext = context;
        mOnRouteSelectedListener = onRouteSelectedListener;
    }

    public class RouteDataViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        RadioButton mRadioBtnView;
        OnRouteSelectedListener onRouteSelectedListener;

        private RouteDataViewHolder(View itemView, OnRouteSelectedListener onRouteSelectedListener) {
            super(itemView);
            mRadioBtnView = itemView.findViewById(R.id.radioButton);
            this.onRouteSelectedListener = onRouteSelectedListener;

            mRadioBtnView.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onRouteSelectedListener.onRouteSelected(buttonView,getAdapterPosition());
        }
    }


    @Override
    public RouteDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(mContext);
        View itemView = Inflater.inflate(R.layout.list_route_item, parent, false);
        return new RouteDataViewHolder(itemView, mOnRouteSelectedListener);
    }

    @Override
    public void onBindViewHolder(RouteDataViewHolder holder, int position) {
        if (mRouteDataList != null) {
            Local_Route current = mRouteDataList.get(position);
            holder.itemView.setTag(current._id);
            holder.mRadioBtnView.setText(current.title);


        } else {
            // Covers the case of data not being ready yet.
            Log.e("test", "no route");
            holder.mRadioBtnView.setText("No Route");
        }
    }

    public void setRouteDataList(List<Local_Route> routeDataList) {
        mRouteDataList = routeDataList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mRouteDataList != null)
            return mRouteDataList.size();
        else return 0;
    }

    public interface OnRouteSelectedListener {
        void onRouteSelected(CompoundButton buttonView, int position);
    }
}
