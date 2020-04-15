package com.example.rootshareapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;
import com.example.rootshareapp.fragment.MyPageFragment;
import com.example.rootshareapp.model.Post;
import com.example.rootshareapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class PostListAdapter extends FirestoreAdapter<PostListAdapter.ViewHolder> {

    public static final String TAG = "PostListAdapter";

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
        return new ViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        Context mContext;
        ImageView uIconBtn;
        TextView authorView;
        TextView unameView;
        TextView created_atView;
        TextView bodyView;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            uIconBtn = itemView.findViewById(R.id.u_icon);
            authorView = itemView.findViewById(R.id.author);
            unameView = itemView.findViewById(R.id.uname);
            created_atView = itemView.findViewById(R.id.post_created_at);
            bodyView = itemView.findViewById(R.id.body);
        }
        public void bind(final DocumentSnapshot snapshot, final OnPostSelectedListener listener) {

            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

            Post post = snapshot.toObject(Post.class);

            final String post_userId = post.getUid();
            mDatabase.collection("users").document(post_userId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("user_icon"));
                            Glide.with(mContext)
                                    .load(document.getData().get("user_icon"))
                                    .into(uIconBtn);

                            authorView.setText(String.valueOf(document.getData().get("display_name")));
                            unameView.setText(String.valueOf(document.getData().get("user_name")));

                            uIconBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //それぞれのuserのページに飛べるようにする処理を書く
                                }
                            });
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

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
