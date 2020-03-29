package com.example.rootshareapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class PostListAdapter extends FirestoreAdapter<PostListAdapter.ViewHolder> {



    public interface OnPostSelectedListener {

        void onPostSelected(DocumentSnapshot post);

    }

    private OnPostSelectedListener mListener;

    public PostListAdapter(Query query, OnPostSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView authorView;
//        TextView uidView;
        TextView created_atView;
        TextView bodyView;

        public ViewHolder(View itemView) {
            super(itemView);
            authorView = itemView.findViewById(R.id.author);
//            uidView = itemView.findViewById(R.id.uid);
            created_atView = itemView.findViewById(R.id.post_created_at);
            bodyView = itemView.findViewById(R.id.body);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPostSelectedListener listener) {

            Post post = snapshot.toObject(Post.class);


//            uidView.setText(post.getUid());
            authorView.setText(post.getAuthor());
            created_atView.setText(post.getCreated_at());
            bodyView.setText( post.getBody());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPostSelected(snapshot);
                    }
                }
            });
        }

    }
}
