package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.MainActivity;
import com.example.rootshareapp.PostDetailActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.adapter.PostListAdapter;
import com.example.rootshareapp.model.Post;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class RecentPostsFragment extends Fragment implements PostListAdapter.OnPostSelectedListener, MainActivity.setQuery{

    public static final int LIMIT = 50;

    private View view;
    private RecyclerView mRecyclerView;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private PostListAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_recent_posts, container, false);
        mRecyclerView = view.findViewById(R.id.PostsList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get ${LIMIT} restaurants
        mQuery = mFirestore.collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(LIMIT);

        // RecyclerView
        mAdapter = new PostListAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
//                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
//                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(view.findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        // Filter Dialog
//        mFilterDialog = new FilterDialogFragment();

    }



    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
//        if (shouldStartSignIn()) {
//            startSignIn();
//            return;
//        }

        // Apply filters
//        onFilter(mViewModel.getFilters());

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onPostSelected(DocumentSnapshot post) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.KEY_SNAPSHOT, post.getId());
        startActivity(intent);
    }

    @Override
    public void setQuery(Query query) {
        mAdapter.setQuery(query);
    }
}
