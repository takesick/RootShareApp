package com.example.rootshareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_RouteData;

import java.util.List;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteDataViewHolder> {

    private Context mContext;
    private List<Local_RouteData> mRouteDataList;
    private  OnRouteSelectedListener mOnRouteSelectedListener;

    public RouteListAdapter(Context context, OnRouteSelectedListener onRouteSelectedListener) {
        mContext = context;
        mOnRouteSelectedListener = onRouteSelectedListener;
    }

    public class RouteDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView numberView;
        TextView timeView;
        TextView titleView;
        OnRouteSelectedListener onRouteSelectedListener;

        private RouteDataViewHolder(View itemView, OnRouteSelectedListener onRouteSelectedListener) {
            super(itemView);
            numberView = itemView.findViewById(R.id.route_id);
            timeView = itemView.findViewById(R.id.route_created_at);
            titleView = itemView.findViewById(R.id.route_title);
            this.onRouteSelectedListener = onRouteSelectedListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRouteSelectedListener.onRouteSelected(v,getAdapterPosition());
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
            Local_RouteData current = mRouteDataList.get(position);
            holder.itemView.setTag(current._id);
            holder.numberView.setText(String.valueOf(current._id));
            holder.timeView.setText("計測日時：" + current.created_at);
            holder.titleView.setText("タイトル：" + current.title);


        } else {
            // Covers the case of data not being ready yet.
            Log.e("test", "no words");
            holder.timeView.setText("No Created_at");
            holder.numberView.setText("No Accuracy");
            holder.titleView.setText("No Latitude");
        }
    }

    public void setRouteDataList(List<Local_RouteData> routeDataList) {
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
        void onRouteSelected(View view, int position);
    }
}
