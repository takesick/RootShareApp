package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.MainActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.RecentPostActivity;
import com.example.rootshareapp.adapter.PostListSearchedAdapter;
import com.example.rootshareapp.model.Post;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import java.util.List;

public class RecentPostsFragment extends PostListFragment implements MainActivity.SetQuery, PostListSearchedAdapter.OnPostSelectedListener{

    public static final int LIMIT = 50;

    private RecyclerView mRecyclerView;

    @Override
    public Query getQuery(CollectionReference collectionReference) {
        Query query = collectionReference
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(LIMIT);
        return query;
    }

    @Override
    public void onPostSelected(View view, int position) {
        Intent intent = new Intent(getActivity(), RecentPostActivity.class);
        intent.putExtra(RecentPostActivity.KEY_SNAPSHOT, position);
        startActivity(intent);
    }

    @Override
    public void setQuery(List<Post> postList) {
        PostListSearchedAdapter mSearchAdapter = new PostListSearchedAdapter(postList, this);
        mRecyclerView = getView().findViewById(R.id.PostsList);
        mRecyclerView.setAdapter(mSearchAdapter);
    }
}
