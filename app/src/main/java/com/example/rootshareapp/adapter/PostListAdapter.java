package com.example.rootshareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;
import com.example.rootshareapp.fragment.PhotoDetailDialogFragment;
import com.example.rootshareapp.model.Photo;
import com.example.rootshareapp.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class PostListAdapter extends FirestoreAdapter<PostListAdapter.ViewHolder> {

    public static final String TAG = "PostListAdapter";

    public interface OnPostSelectedListener {
        void onPostSelected(DocumentSnapshot post);
    }

    private OnPostSelectedListener mListener;
    private FragmentManager mFragmentManager;

    public PostListAdapter(Query query, OnPostSelectedListener listener, FragmentManager fragmentManager) {
        super(query);
        mListener = listener;
        mFragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item, parent, false);
        return new ViewHolder(v, parent.getContext(), mFragmentManager);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FirebaseFirestore mDatabase;
        CollectionReference mUserRef, mPostRef, mPhotoRef;
        DocumentReference mRouteRef;

        Context mContext;
        FragmentManager mFragmentManager;

        ImageView uIconBtn;
        ImageView imageView1, imageView2, imageView3;
        List<String> mPhotos = new ArrayList<>();
        List<ImageView> imageViewList = new ArrayList<>();
        TextView authorView;
        TextView unameView;
        TextView created_atView;
        TextView bodyView;
        TextView routeTitleView;
        LinearLayout imagesView, imagesSubView;

        public ViewHolder(View itemView, Context context , FragmentManager fragmentManager) {
            super(itemView);
            mContext = context;
            mFragmentManager = fragmentManager;

            uIconBtn = itemView.findViewById(R.id.u_icon);
            authorView = itemView.findViewById(R.id.author);
            unameView = itemView.findViewById(R.id.uname);
            created_atView = itemView.findViewById(R.id.post_created_at);
            bodyView = itemView.findViewById(R.id.body);

            imagesView = itemView.findViewById(R.id.media_container);
            imagesSubView = itemView.findViewById(R.id.sub_image_container);
            imageView1 = itemView.findViewById(R.id.image_view1);
            imageView2 = itemView.findViewById(R.id.image_view2);
            imageView3 = itemView.findViewById(R.id.image_view3);
            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            routeTitleView = itemView.findViewById(R.id.post_route_title);

            imageViewList.add(imageView1);
            imageViewList.add(imageView2);
            imageViewList.add(imageView3);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
        }

        public void bind(final DocumentSnapshot snapshot, final OnPostSelectedListener listener) {

            Post post = snapshot.toObject(Post.class);
            mDatabase = FirebaseFirestore.getInstance();
            mUserRef = mDatabase.collection("users");
            mPostRef = mDatabase.collection("posts");
            if (post.id != null) {
                mPhotoRef = mPostRef.document(post.id).collection("posts");
            }
            mRouteRef = post.getRef();
//            mPhotoRef = mDatabase.collection("posts").document(post._id).collection("photos");
            Log.e(TAG, String.valueOf(mRouteRef));

            final String post_userId = post.getUid();
            mUserRef.document(post_userId)
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
            if(post.id != null) {
                if (mRouteRef != null) {
                    routeTitleView.setText(post.route_name);
                    routeTitleView.setVisibility(View.VISIBLE);
                } else {
                    routeTitleView.setVisibility(View.GONE);
                }
                mPostRef.document(post.id).collection("photos").orderBy("created_at", Query.Direction.DESCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.e(TAG, document.getId() + " => " + document.getData());
                                        Photo photo = document.toObject(Photo.class);
                                        mPhotos.add(photo.uri);
                                    }
                                    setImages();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            } else {
                imagesView.setVisibility(View.GONE);
                routeTitleView.setVisibility(View.GONE);
            }

            created_atView.setText(post.getCreated_at());
            bodyView.setText(post.getBody());


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

        public void setImages(){
            switch (mPhotos.size()){
                case 0:
                    imagesView.setVisibility(View.GONE);
                    imagesSubView.setVisibility(View.GONE);
                    break;

                case 1:
                    imagesView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(mPhotos.get(0))
                            .into(imageViewList.get(0));
                    imagesSubView.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                    break;

                case 2:
                    imagesView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imagesSubView.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(mPhotos.get(1))
                            .into(imageViewList.get(0));
                    Glide.with(mContext)
                            .load(mPhotos.get(0))
                            .into(imageViewList.get(1));
                    imageView3.setVisibility(View.GONE);
                    break;

                case 3:
                    imagesView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imagesSubView.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(mPhotos.get(2))
                            .into(imageViewList.get(0));
                    Glide.with(mContext)
                            .load(mPhotos.get(1))
                            .into(imageViewList.get(1));
                    Glide.with(mContext)
                            .load(mPhotos.get(0))
                            .into(imageViewList.get(2));
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.image_view1:
                    openDialogForPhoto(0);
                    break;

                case R.id.image_view2:
                    openDialogForPhoto(1);
                    break;

                case R.id.image_view3:
                    openDialogForPhoto(2);
                    break;
            }
        }

        public void openDialogForPhoto(int num){
            PhotoDetailDialogFragment newFragment = PhotoDetailDialogFragment.newInstance(mPhotos.get(mPhotos.size()-num-1));
            newFragment.show(mFragmentManager, "dialog");
        }
    }

    public void Redirect(){
        notifyDataSetChanged();
    }
}
