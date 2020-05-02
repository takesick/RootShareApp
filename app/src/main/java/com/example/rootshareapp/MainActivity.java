package com.example.rootshareapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.example.rootshareapp.fragment.MyPageFragment;
import com.example.rootshareapp.fragment.MyRoutesFragment;
import com.example.rootshareapp.fragment.RecentPostsFragment;
import com.example.rootshareapp.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_MULTI_PERMISSIONS = 101;

    private static final String TAG = "MainActivity";
    private FloatingActionButton mOpenDrawerFab, mCloseDrawerFab, mStartRecordingFab, mStopRecordingFab, mWriteNewPostFab, mSearchFab;

    public interface SetQuery{
        void setQuery(List<Post> postList);
    }

//        if(savedInstanceState == null){
//            // FragmentManagerのインスタンス生成
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            // FragmentTransactionのインスタンスを取得
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            // インスタンスに対して張り付け方を指定する
//            fragmentTransaction.replace(R.id.container, new MyRoutesFragment());
//            // 張り付けを実行
//            fragmentTransaction.commit();
//        }

//        setContentView(R.layout.activity_main);

    private ViewPager mViewPager;
    private SetQuery mSetQuery;
//    private RecentPostsFragment fragment;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiPermissions();
        } else {

        }

        // Create the adapter that will return a fragment for each section
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new MyRoutesFragment(),
                    new MyPageFragment(),
            };
//            private final String[] mFragmentNames = new String[]{
//                    getString(R.string.home_heading_recent),
//                    getString(R.string.home_heading_my_routes),
//                    getString(R.string.home_heading_my_page)
//            };
//
//            private int[] imageResId = {
//                    R.drawable.ic_home_black_24dp,
//                    R.drawable.ic_place_black_24dp,
//                    R.drawable.ic_person_black_24dp
//            };

//            @Override
//            public CharSequence getPageTitle(int position) {
//
//                Drawable image = ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
//                image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//                SpannableString sb = new SpannableString(mFragmentNames[position]);
//                ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//                sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                return sb;
//            }
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return mFragmentNames[position];
//            }

        };

// Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_place_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_black_24dp);

        mOpenDrawerFab = findViewById(R.id.OpenDrawerFab);
        mCloseDrawerFab = findViewById(R.id.CloseDrawerFab);
        mWriteNewPostFab = findViewById(R.id.WriteNewPostFab);
        mStartRecordingFab = findViewById(R.id.StartRecordingFab);
        mStopRecordingFab = findViewById(R.id.StopRecordingFab);
        mSearchFab = findViewById(R.id.Search);

        mOpenDrawerFab.setOnClickListener(this);
        mCloseDrawerFab.setOnClickListener(this);
        mWriteNewPostFab.setOnClickListener(this);
        mStartRecordingFab.setOnClickListener(this);
        mStopRecordingFab.setOnClickListener(this);
        mSearchFab.setOnClickListener(this);

        mSetQuery = (SetQuery) mPagerAdapter.getItem(mViewPager.getCurrentItem());
//        fragment = (RecentPostsFragment) mPagerAdapter.getItem(mViewPager.getCurrentItem());
//

    }

    // 位置情報許可の確認、外部ストレージのPermissionにも対応できるようにしておく
    private void checkMultiPermissions() {
        // 位置情報の Permission
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // 外部ストレージ書き込みの Permission
//        int permissionExtStorage = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ArrayList reqPermissions = new ArrayList<>();

        // 位置情報の Permission が許可されているか確認
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            // 許可済
        } else {
            // 未許可
            reqPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        // 未許可
        if (!reqPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[]) reqPermissions.toArray(new String[0]),
                    REQUEST_MULTI_PERMISSIONS);
            // 未許可あり
        } else {
            // 許可済

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_MULTI_PERMISSIONS) {
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    // 位置情報
                    if (permissions[i].
                            equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // 許可された

                        } else {
                            // それでも拒否された時の対応
                            toastMake("位置情報の許可がないので計測できません");
                        }
                    }
//                    // 外部ストレージ
//                    else if (permissions[i].
//                            equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            // 許可された
//                        } else {
//                            // それでも拒否された時の対応
//                            toastMake("外部書込の許可がないので書き込みできません");
//                        }
//                    }
                }
//                startLocationService();
            }
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // トーストの生成
    private void toastMake(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
    }

    //search data
//    private Query firebaseSearch(String searchText){
//        mCollection = db.collection("posts");
//        firebaseSearchQuery = mCollection.orderBy("body").startAt(searchText).endAt(searchText+"\uf8ff");
//        return firebaseSearchQuery;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_item, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) menuItem.getActionView();

        Client client = new Client(getString(R.string.algolia_id), getString(R.string.algolia_key));
        final Index index = client.getIndex("posts");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
//                msetQuery.setQuery(firebaseSearch(query));
//                fragment.setQuery(firebaseSearch(query));
                Query query = new Query(searchText)
                        .setHitsPerPage(50);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<Post> mPostList = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++) {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                Post post = new Post();
                                post.setUid(jsonObject.getString("uid"));
                                post.setCreated_at(jsonObject.getString("created_at"));
                                post.setBody(jsonObject.getString("body"));
                                mPostList.add(post);
                            }
                            mSetQuery.setQuery(mPostList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                msetQuery.setQuery(firebaseSearch(newText));
//                fragment.setQuery(firebaseSearch(newText));
                Query query = new Query(newText)
                        .setHitsPerPage(50);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<Post> mPostList = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++) {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                Post post = new Post();
                                Log.e("post", jsonObject.getString("uid"));
                                post.setUid(jsonObject.getString("uid"));
                                post.setCreated_at(jsonObject.getString("created_at"));
                                post.setBody(jsonObject.getString("body"));
                                mPostList.add(post);
                            }
                            mSetQuery.setQuery(mPostList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.OpenDrawerFab:
                    if (mStopRecordingFab.getVisibility() == View.VISIBLE) {
                        String nowRecording = "位置情報の記録を停止してください";
                        toastMake(nowRecording);
                    } else if (mStopRecordingFab.getVisibility() == View.GONE) {
                        mOpenDrawerFab.setVisibility(View.GONE);
                        mCloseDrawerFab.setVisibility(View.VISIBLE);
                        mWriteNewPostFab.setVisibility(View.VISIBLE);
                        mStartRecordingFab.setVisibility(View.VISIBLE);
                        mSearchFab.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.CloseDrawerFab:
                    mWriteNewPostFab.setVisibility(View.GONE);
                    mStartRecordingFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.GONE);
                    mSearchFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    break;

                case R.id.WriteNewPostFab:
                    mWriteNewPostFab.setVisibility(View.GONE);
                    mStartRecordingFab.setVisibility(View.GONE);
                    mStopRecordingFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.GONE);
                    mSearchFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    Intent intent_newPost = new Intent(this, NewPostActivity.class);
                    startActivity(intent_newPost);
                    break;

                case R.id.StartRecordingFab:
                    mWriteNewPostFab.setVisibility(View.GONE);
                    mStartRecordingFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.GONE);
                    mSearchFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    mStopRecordingFab.setVisibility(View.VISIBLE);
                    Intent intent_start = new Intent(this, LocationService.class);
                    startForegroundService(intent_start);
                    break;

                case R.id.StopRecordingFab:
                    mStopRecordingFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.GONE);
                    mSearchFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.VISIBLE);
                    mWriteNewPostFab.setVisibility(View.VISIBLE);
                    mStartRecordingFab.setVisibility(View.VISIBLE);
                    Intent intent_stop = new Intent(this, LocationService.class);
                    stopService(intent_stop);
                    break;

                case R.id.Search:
                    mSearchFab.setVisibility(View.GONE);
                    mWriteNewPostFab.setVisibility(View.GONE);
                    mStartRecordingFab.setVisibility(View.GONE);
                    mStopRecordingFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }

    }
}

