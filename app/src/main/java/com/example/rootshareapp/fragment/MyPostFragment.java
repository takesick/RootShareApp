package com.example.rootshareapp.fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class MyPostFragment extends PostListFragment {

    public MyPostFragment(){}

    @Override
    public Query getQuery(CollectionReference collectionReference) {
        Query query = collectionReference
                .whereEqualTo("uid", getUid())
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(LIMIT);
        return query;
    }
}
