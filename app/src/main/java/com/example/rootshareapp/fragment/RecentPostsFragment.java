package com.example.rootshareapp.fragment;

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

import com.example.rootshareapp.R;
import com.example.rootshareapp.adapter.PostListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class RecentPostsFragment extends Fragment implements PostListAdapter.OnPostSelectedListener {

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

    }

    //
//    private static final String TAG = "MainActivity";
//
//    private static final int RC_SIGN_IN = 9001;
//
//    private static final int LIMIT = 50;
//
//    private Toolbar mToolbar;
//    private TextView mCurrentSearchView;
//    private TextView mCurrentSortByView;
//    private RecyclerView mRestaurantsRecycler;
//    private ViewGroup mEmptyView;
//
//    private FirebaseFirestore mFirestore;
//    private Query mQuery;
//
//    private FilterDialogFragment mFilterDialog;
//    private RestaurantAdapter mAdapter;
//
//    private MainActivityViewModel mViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mToolbar = findViewById(R.id.toolbar);
//        mCurrentSearchView = findViewById(R.id.textCurrentSearch);
//        mCurrentSortByView = findViewById(R.id.textCurrentSortBy);
//        mRestaurantsRecycler = findViewById(R.id.recyclerRestaurants);
//        mEmptyView = findViewById(R.id.viewEmpty);
//        setSupportActionBar(mToolbar);
//
//        findViewById(R.id.filterBar).setOnClickListener(this);
//        findViewById(R.id.buttonClearFilter).setOnClickListener(this);
//
//        // View model
//        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//
//        // Enable Firestore logging
//        FirebaseFirestore.setLoggingEnabled(true);
//
//        // Firestore
//        mFirestore = FirebaseFirestore.getInstance();
//
//        // Get ${LIMIT} restaurants
//        mQuery = mFirestore.collection("restaurants")
//                .orderBy("avgRating", Query.Direction.DESCENDING)
//                .limit(LIMIT);
//
//        // RecyclerView
//        mAdapter = new RestaurantAdapter(mQuery, this) {
//            @Override
//            protected void onDataChanged() {
//                // Show/hide content if the query returns empty.
//                if (getItemCount() == 0) {
//                    mRestaurantsRecycler.setVisibility(View.GONE);
//                    mEmptyView.setVisibility(View.VISIBLE);
//                } else {
//                    mRestaurantsRecycler.setVisibility(View.VISIBLE);
//                    mEmptyView.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            protected void onError(FirebaseFirestoreException e) {
//                // Show a snackbar on errors
//                Snackbar.make(findViewById(android.R.id.content),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
//            }
//        };
//
//        mRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this));
//        mRestaurantsRecycler.setAdapter(mAdapter);
//
//        // Filter Dialog
//        mFilterDialog = new FilterDialogFragment();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Start sign in if necessary
//        if (shouldStartSignIn()) {
//            startSignIn();
//            return;
//        }
//
//        // Apply filters
//        onFilter(mViewModel.getFilters());
//
//        // Start listening for Firestore updates
//        if (mAdapter != null) {
//            mAdapter.startListening();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAdapter != null) {
//            mAdapter.stopListening();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_add_items:
//                onAddItemsClicked();
//                break;
//            case R.id.menu_sign_out:
//                AuthUI.getInstance().signOut(this);
//                startSignIn();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//            mViewModel.setIsSigningIn(false);
//
//            if (resultCode != RESULT_OK) {
//                if (response == null) {
//                    // User pressed the back button.
//                    finish();
//                } else if (response.getError() != null
//                        && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    showSignInErrorDialog(R.string.message_no_network);
//                } else {
//                    showSignInErrorDialog(R.string.message_unknown);
//                }
//            }
//        }
//    }
//
//    public void onFilterClicked() {
//        // Show the dialog containing filter options
//        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
//    }
//
//    public void onClearFilterClicked() {
//        mFilterDialog.resetFilters();
//
//        onFilter(Filters.getDefault());
//    }
//
//    @Override
//    public void onRestaurantSelected(DocumentSnapshot restaurant) {
//        // Go to the details page for the selected restaurant
//        Intent intent = new Intent(this, RestaurantDetailActivity.class);
//        intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.getId());
//
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
//    }
//
//    @Override
//    public void onFilter(Filters filters) {
//        // Construct query basic query
//        Query query = mFirestore.collection("restaurants");
//
//        // Category (equality filter)
//        if (filters.hasCategory()) {
//            query = query.whereEqualTo(Restaurant.FIELD_CATEGORY, filters.getCategory());
//        }
//
//        // City (equality filter)
//        if (filters.hasCity()) {
//            query = query.whereEqualTo(Restaurant.FIELD_CITY, filters.getCity());
//        }
//
//        // Price (equality filter)
//        if (filters.hasPrice()) {
//            query = query.whereEqualTo(Restaurant.FIELD_PRICE, filters.getPrice());
//        }
//
//        // Sort by (orderBy with direction)
//        if (filters.hasSortBy()) {
//            query = query.orderBy(filters.getSortBy(), filters.getSortDirection());
//        }
//
//        // Limit items
//        query = query.limit(LIMIT);
//
//        // Update the query
//        mAdapter.setQuery(query);
//
//        // Set header
//        mCurrentSearchView.setText(Html.fromHtml(filters.getSearchDescription(this)));
//        mCurrentSortByView.setText(filters.getOrderDescription(this));
//
//        // Save filters
//        mViewModel.setFilters(filters);
//    }
//
//    private boolean shouldStartSignIn() {
//        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
//    }
//
//    private void startSignIn() {
//        // Sign in with FirebaseUI
//        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
//                .setAvailableProviders(Collections.singletonList(
//                        new AuthUI.IdpConfig.EmailBuilder().build()))
//                .setIsSmartLockEnabled(false)
//                .build();
//
//        startActivityForResult(intent, RC_SIGN_IN);
//        mViewModel.setIsSigningIn(true);
//    }
//
//    private void onAddItemsClicked() {
//        // Add a bunch of random restaurants
//        WriteBatch batch = mFirestore.batch();
//        for (int i = 0; i < 10; i++) {
//            DocumentReference restRef = mFirestore.collection("restaurants").document();
//
//            // Create random restaurant / ratings
//            Restaurant randomRestaurant = RestaurantUtil.getRandom(this);
//            List<Rating> randomRatings = RatingUtil.getRandomList(randomRestaurant.getNumRatings());
//            randomRestaurant.setAvgRating(RatingUtil.getAverageRating(randomRatings));
//
//            // Add restaurant
//            batch.set(restRef, randomRestaurant);
//
//            // Add ratings to subcollection
//            for (Rating rating : randomRatings) {
//                batch.set(restRef.collection("ratings").document(), rating);
//            }
//        }
//
//        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "Write batch succeeded.");
//                } else {
//                    Log.w(TAG, "write batch failed.", task.getException());
//                }
//            }
//        });
//    }
//
//    private void showSignInErrorDialog(@StringRes int message) {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(R.string.title_sign_in_error)
//                .setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton(R.string.option_retry, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startSignIn();
//                    }
//                })
//                .setNegativeButton(R.string.option_exit, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                }).create();
//
//        dialog.show();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.filterBar:
//                onFilterClicked();
//                break;
//            case R.id.buttonClearFilter:
//                onClearFilterClicked();
//                break;
//        }
//    }
}
