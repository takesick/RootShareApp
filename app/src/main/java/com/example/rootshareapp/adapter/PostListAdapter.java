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
import com.example.rootshareapp.model.Photo;
import com.example.rootshareapp.model.Post;
import com.example.rootshareapp.model.Public_Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

    public PostListAdapter(Query query, OnPostSelectedListener listener, FragmentManager fragmentManager) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

        FirebaseFirestore mDatabase;
        CollectionReference mUserRef, mPhotoRef;
        DocumentReference mRouteRef;
        List<Public_Location> mLocationLists = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        StringBuilder center_and_zoom = new StringBuilder();

        Context mContext;
        FragmentManager mFragmentManager;
        ImageView uIconBtn;
        ImageView imageView1, imageView2, imageView3;
        List<Photo> mPhotos = new ArrayList<>();
        TextView authorView;
        TextView unameView;
        TextView created_atView;
        TextView bodyView;
        TextView routeTitleView;
        LinearLayout subImageContainer;
        Boolean setMap_isStarted = false;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            uIconBtn = itemView.findViewById(R.id.u_icon);
            authorView = itemView.findViewById(R.id.author);
            unameView = itemView.findViewById(R.id.uname);
            created_atView = itemView.findViewById(R.id.post_created_at);
            bodyView = itemView.findViewById(R.id.body);
            imageView1 = itemView.findViewById(R.id.image_view1);
            imageView2 = itemView.findViewById(R.id.image_view2);
            imageView3 = itemView.findViewById(R.id.image_view3);
            subImageContainer = itemView.findViewById(R.id.sub_image_container);
            routeTitleView = itemView.findViewById(R.id.post_route_title);
        }

        public void bind(final DocumentSnapshot snapshot, final OnPostSelectedListener listener) {

            Post post = snapshot.toObject(Post.class);
            mDatabase = FirebaseFirestore.getInstance();
            mUserRef = mDatabase.collection("users");
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
            if(mRouteRef != null) {
                mRouteRef.collection("locations").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        Public_Location public_location = document.toObject(Public_Location.class);
                                        mLocationLists.add(public_location);
                                    }
                                    generateString();
                                    setMapImage();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                routeTitleView.setText(post.route_name);
            } else {
                imageView1.setVisibility(View.GONE);
                routeTitleView.setVisibility(View.GONE);
            }

//            mPhotoRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Log.d(TAG, document.getId() + " => " + document.getData());
//                            Photo photo = document.toObject(Photo.class);
//                            mPhotos.add(photo);
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                }
//            });

            setImages();

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

        public void generateString() {
            double latitude, longitude, max_lat = 0, min_lat = 0, max_long = 0, min_long = 0;
            LatLng latLng1,latLng2, center;
            int zoom;
            for(int i=0; i<mLocationLists.size(); i++) {
                latitude = mLocationLists.get(i).latitude;
                longitude = mLocationLists.get(i).longitude;

                if(i==0) {
                    max_lat = min_lat = latitude;
                    max_long = min_long = longitude;
                }
                if(latitude>max_lat) max_lat = latitude;
                if(latitude<min_lat) min_lat = latitude;
                if(longitude>max_long) max_long = longitude;
                if(longitude<min_long) min_long = longitude;

                path.append("|" + latitude + "," + longitude);

            }

            double distance = getDistance(max_lat, min_lat, max_long,min_long);
            if(distance>1) {
                zoom = 14;
            } else if(distance> 0.5 && distance<=1) {
                zoom = 15;
            } else if(distance> 0.2 && distance<=0.5) {
                zoom = 17;
            } else {
                zoom = 18;
            }
            latLng1 = new LatLng(max_lat, max_long);
            latLng2 = new LatLng(min_lat, min_long);

            center = LatLngBounds.builder().include(latLng1).include(latLng2).build().getCenter();
            center_and_zoom.append("&center=" + center.latitude + "," + center.longitude +"&zoom=" + zoom);
        }

        public void setMapImage(){
            String locations = new String(path);
            String camera = new String(center_and_zoom);

            String url = "https://maps.googleapis.com/maps/api/staticmap?size=200x200&scale=2" +
                    camera + "&path=color:0xff0000ff|weight:3" + locations + "&key=" + mContext.getString(R.string.google_maps_key);

            Glide.with(mContext)
                    .load(url)
                    .into(imageView1);
            setMap_isStarted = true;
        }

        public void setImages(){
            if(setMap_isStarted){
                switch (mPhotos.size()) {
                    case 0:
                        subImageContainer.setVisibility(View.GONE);
                        break;

                    case 1:
//                        imageView2.setImageURI(mPhotos.get(0).uri);
                        imageView3.setVisibility(View.GONE);
                        break;

                    case 2:
//                        imageView2.setImageURI(mPhotos.get(0).uri.);
//                        imageView2.setImageURI(mPhotos.get(0).uri);
                }
            }
        }

        public double getDistance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            double dist_km = dist * 60 * 1.1515 * 1.609344;
            return dist_km;
        }

        private double rad2deg(double radian) {
            return radian * (180f / Math.PI);
        }

        public double deg2rad(double degrees) {
            return degrees * (Math.PI / 180f);
        }
    }
}
