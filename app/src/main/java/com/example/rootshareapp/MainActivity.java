package com.example.rootshareapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.rootshareapp.adapter.FirestoreAdapter;
import com.example.rootshareapp.fragment.MyPageFragment;
import com.example.rootshareapp.fragment.MyRoutesFragment;
import com.example.rootshareapp.fragment.RecentPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_MULTI_PERMISSIONS = 101;

    private static final String TAG = "MainActivity";
    private FloatingActionButton mOpenDrawerFab, mCloseDrawerFab, mStartRecordingFab, mStopRecordingFab, mWriteNewPostFab, mSearchFab;

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



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiPermissions();
        } else {
            startLocationService();
        }

        // Create the adapter that will return a fragment for each section
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new MyRoutesFragment(),
                    new MyPageFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_routes),
                    getString(R.string.heading_my_page)
            };
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

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

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
            startLocationService();
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
            //
        }
    }

    private void startLocationService() {
//        setContentView(R.layout.frag_location_list);
//
////        textView = findViewById(R.id.log_text);
//        mStartRecordingFab = findViewById(R.id.StartRecordingFab);
//        mStartRecordingFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(), LocationService.class);
//
//                // API 26 以降
//                startForegroundService(intent);
//
//                // Activityを終了させる
//            }
//        });
//
//
////        Button buttonLog = findViewById(R.id.button_log);
////        buttonLog.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                textView.setText(fileReadWrite.r
////                eadFile());
////            }
////        });
//
//        mStopRecordingFab  = findViewById(R.id.StopRecordingFab);
//        mStopRecordingFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Serviceの停止
//                Intent intent = new Intent(getApplication(), LocationService.class);
//                stopService(intent);
//            }
//        });
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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mCllection;
    private FirestoreAdapter mFirestore;

    //search data
    private void firebaseSearch(String searchText){
        mCllection = db.collection("posts");
        Query firebaseSearchQuery= mCllection.orderBy("body").startAt(searchText).endAt(searchText+"\uf8ff");
        mFirestore=new FirestoreAdapter(firebaseSearchQuery) {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                // 例外処理(3種類以外の変更など)
                if (e != null) {
                    Log.w(TAG, "onEvent:error", e);
                    onError(e);
                    return;
                }

                // Dispatch the event(=スナップショットの変更の詳細を3種に分類し、それぞれに対応したメソッドを実行する)
                Log.d(TAG, "onEvent:numChanges:" + documentSnapshots.getDocumentChanges().size());
                for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
                    switch (change.getType()) {
                        case ADDED:
                            onDocumentAdded(change);
                            break;
                        case MODIFIED:
                            onDocumentModified(change);
                            break;
                        case REMOVED:
                            onDocumentRemoved(change);
                            break;
                    }
                }

                onDataChanged();
            }

        };

        mFirestore.setQuery(firebaseSearchQuery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_item, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    break;

                case R.id.WriteNewPostFab:
                    mWriteNewPostFab.setVisibility(View.GONE);
                    mStartRecordingFab.setVisibility(View.GONE);
                    mStopRecordingFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    Intent intent_newPost = new Intent(this, NewPostActivity.class);
                    startActivity(intent_newPost);
                    break;

                case R.id.StartRecordingFab:
                    mWriteNewPostFab.setVisibility(View.GONE);
                    mStartRecordingFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.VISIBLE);
                    mStopRecordingFab.setVisibility(View.VISIBLE);
                    Intent intent_start = new Intent(this, LocationService.class);
                    startForegroundService(intent_start);
                    break;

                case R.id.StopRecordingFab:
                    mStopRecordingFab.setVisibility(View.GONE);
                    mOpenDrawerFab.setVisibility(View.GONE);
                    mCloseDrawerFab.setVisibility(View.VISIBLE);
                    mWriteNewPostFab.setVisibility(View.VISIBLE);
                    mStartRecordingFab.setVisibility(View.VISIBLE);
                    Intent intent_stop = new Intent(this, LocationService.class);
                    stopService(intent_stop);
                    break;

                case R.id.Search:
                    mSearchFab.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }

    }
}

