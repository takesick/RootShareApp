package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Photo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class PostDetailFragment extends Fragment {

    private View view;
    private Intent intent;
    private String post_id;
    private ImageView uIconBtn;
    private TextView authorView;
    private TextView unameView;
    private TextView created_atView;
    private TextView bodyView;
    private TextView routeTitleView;
    private LinearLayout imagesView;
    private ImageView imageView1, imageView2;
    private FrameLayout mapContainer;

    private FirebaseFirestore mDatabase;
    private CollectionReference mPostRef;
    private Boolean mRouteExist, mPhotoExist;
    private List<String> mPhotos = new ArrayList<>();


    public static PostDetailFragment newInstance() {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uIconBtn = view.findViewById(R.id.u_icon);
        authorView = view.findViewById(R.id.author);
        unameView = view.findViewById(R.id.uname);
        created_atView = view.findViewById(R.id.post_created_at);
        bodyView = view.findViewById(R.id.body);
        imagesView = view.findViewById(R.id.imageView);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mapContainer = view.findViewById(R.id.map_container);
        routeTitleView = view.findViewById(R.id.image_view1);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intent = getActivity().getIntent();
        post_id = intent.getStringExtra("id");

        mDatabase = FirebaseFirestore.getInstance();
        mPostRef = mDatabase.collection("posts");

        mDatabase.collection("users").document(intent.getStringExtra("uid"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("user_icon"));
                        Glide.with(getContext())
                                .load(document.getData().get("user_icon"))
                                .into(uIconBtn);

                        authorView.setText(String.valueOf(document.getData().get("display_name")));
                        unameView.setText(String.valueOf(document.getData().get("user_name")));

                        uIconBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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

        mDatabase.collection("post-route").document(post_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("existance"));
                                mRouteExist = (Boolean) document.getData().get("existance");
                                if (mRouteExist == true) {
                                    setRoute();
                                } else {
                                    mapContainer.setVisibility(View.GONE);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        mDatabase.collection("post-photo").document(post_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("existance"));
                                mPhotoExist = (Boolean) document.getData().get("existance");
                                if(mPhotoExist == true){
                                    getImages();
                                } else {
                                    imagesView.setVisibility(View.GONE);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        created_atView.setText(intent.getStringExtra("created_at"));
        bodyView.setText(intent.getStringExtra("body"));

//        if((mRouteExist==true) && (mPhotoExist==true)) {
//            routeTitleView.setVisibility(View.VISIBLE);
//            imagesView.setVisibility(View.VISIBLE);
//            setRoute();
//            setImages();
//        } else if(mRouteExist==true) {
//            routeTitleView.setVisibility(View.VISIBLE);
//            imagesView.setVisibility(View.VISIBLE);
//            setRoute();
//        } else if(mPhotoExist==true) {
//            imagesView.setVisibility(View.VISIBLE);
//            setImages();
//        } else {
//            routeTitleView.setVisibility(View.GONE);
//            imagesView.setVisibility(View.GONE);
//        }
    }

    public void setRoute(){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map_container, PublicMapFragment.newInstance());
        fragmentTransaction.commit();
    }

    public void getImages(){
        mPostRef.document(post_id).collection("photos").orderBy("created_at", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                Photo photo = document.toObject(Photo.class);
                                mPhotos.add(photo.uri);
                            }
                            showPhoto();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void showPhoto() {
        switch (mPhotos.size()) {
            case 0:
                imagesView.setVisibility(View.GONE);
                break;

            case 1:
                if(mRouteExist){
                    mapContainer.setVisibility(View.VISIBLE);
                } else {
                    mapContainer.setVisibility(View.GONE);
                }
                imagesView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(mPhotos.get(0))
                        .into(imageView1);
                imageView2.setVisibility(View.GONE);
                break;

            case 2:
                if(mRouteExist){
                    mapContainer.setVisibility(View.VISIBLE);
                } else {
                    mapContainer.setVisibility(View.GONE);
                }

                imagesView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(mPhotos.get(0))
                        .into(imageView1);
                Glide.with(getContext())
                        .load(mPhotos.get(1))
                        .into(imageView2);
                break;
        }
    }
}
