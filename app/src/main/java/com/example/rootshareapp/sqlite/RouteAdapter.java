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

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnRouteSelectedListener mOnRouteSelectedListener;
    //
    public RouteAdapter(Context context, Cursor cursor, OnRouteSelectedListener onRouteSelectedListener) {
        mContext = context;
        mCursor = cursor;
        mOnRouteSelectedListener = onRouteSelectedListener;
    }


    public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView numberView;
        TextView timeView;
        TextView titleView;
        OnRouteSelectedListener onRouteSelectedListener;

        public RouteViewHolder(@NonNull View itemView, OnRouteSelectedListener onRouteSelectedListener) {
            super(itemView);

            numberView = itemView.findViewById(R.id.route_id);
            timeView = itemView.findViewById(R.id.route_created_at);
            titleView = itemView.findViewById(R.id.route_title);
            this.onRouteSelectedListener = onRouteSelectedListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnRouteSelectedListener.onRouteSelected(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Inflater = LayoutInflater.from(mContext);
        View view = Inflater.inflate(R.layout.list_route_item, parent, false);
        return new RouteViewHolder(view, mOnRouteSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        long id = mCursor.getLong(mCursor.getColumnIndex(RouteContract.Routes._ID));
        String created_at = mCursor.getString(mCursor.getColumnIndex(RouteContract.Routes.COL_CREATED_AT));
        String title = mCursor.getString(mCursor.getColumnIndex(RouteContract.Routes.COL_TITLE));

        holder.itemView.setTag(id);
        holder.numberView.setText(String.valueOf(id));
        holder.timeView.setText("計測日時：" + created_at);
        holder.titleView.setText("タイトル：" + title);

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

    public interface OnRouteSelectedListener {
        void onRouteSelected(View view, int position);
    }
}
