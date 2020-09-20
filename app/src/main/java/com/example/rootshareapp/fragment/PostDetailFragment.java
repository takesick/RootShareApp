package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.android.volley.VolleyLog.TAG;

public class PostDetailFragment extends Fragment {

    private View view;
    Uri uri;
    Post post;
    ImageView uIconBtn;
    TextView authorView;
    TextView unameView;
    TextView created_atView;
    TextView bodyView;
    ImageView imageView1, imageView2, map_view;

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
        imageView1 = view.findViewById(R.id.image_view2);
        imageView2 = view.findViewById(R.id.image_view3);
        map_view = view.findViewById(R.id.image_view1);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.add(R.id.map_container, new LocalMapFragment());

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        Intent intent = getActivity().getIntent();
        created_atView.setText(intent.getStringExtra("created_at"));
        bodyView.setText(intent.getStringExtra("body"));



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
    }
}
