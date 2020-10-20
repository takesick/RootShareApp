package com.example.rootshareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostListSearchedAdapter extends RecyclerView.Adapter<PostListSearchedAdapter.postSearchedViewHolder> {

    public static final String TAG = "PostListSearchedAdapter";

    private List<Post> mPostList;
    private OnPostSelectedListener mListener;

    public PostListSearchedAdapter(List<Post> postList, OnPostSelectedListener Listener) {
        mPostList = postList;
        mListener = Listener;
    }

    @Override
    public postSearchedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("post1", "hello");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item, parent, false);
        return new postSearchedViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull postSearchedViewHolder holder, int position) {
        Log.e("post2", "hello");
        holder.bind(mPostList.get(position), mListener);
    }

    static class postSearchedViewHolder extends RecyclerView.ViewHolder {

        Context mContext;
        ImageView uIconBtn;
        TextView authorView;
        TextView unameView;
        TextView created_atView;
        TextView bodyView;

        public postSearchedViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            mContext = context;
            uIconBtn = itemView.findViewById(R.id.u_icon);
            authorView = itemView.findViewById(R.id.author);
            unameView = itemView.findViewById(R.id.uname);
            created_atView = itemView.findViewById(R.id.post_created_at);
            bodyView = itemView.findViewById(R.id.body);
        }

        public void bind(final Post post, final OnPostSelectedListener listener) {

            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

            final String post_userId = post.getUid();
            Log.e("post3", "hello");
            mDatabase.collection("users").document(post_userId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                        Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("user_icon"));
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
            bodyView.setText(post.getBody());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPostSelected(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnPostSelectedListener {
        void onPostSelected(View view, int position);
    }

    @Override
    public int getItemCount() {
        if (mPostList != null)
            return mPostList.size();
        else return 0;
    }
}
