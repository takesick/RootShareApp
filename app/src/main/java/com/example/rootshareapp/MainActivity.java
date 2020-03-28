package com.example.rootshareapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.rootshareapp.fragment.MyRoutesFragment;
import com.example.rootshareapp.fragment.RecentPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_MULTI_PERMISSIONS = 101;

    private static final String TAG = "MainActivity";
    private FloatingActionButton mOpenDrawerFab, mCloseDrawerFab, mStartRecordingFab, mStopRecordingFab, mWriteNewPostFab;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6, API 23以上でパーミッシンの確認
        if(Build.VERSION.SDK_INT >= 23){
            checkMultiPermissions();
        }
        else{
            startLocationService();
        }

        if(savedInstanceState == null){
            // FragmentManagerのインスタンス生成
            FragmentManager fragmentManager = getSupportFragmentManager();
            // FragmentTransactionのインスタンスを取得
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // インスタンスに対して張り付け方を指定する
            fragmentTransaction.replace(R.id.container, new MyRoutesFragment());
            // 張り付けを実行
            fragmentTransaction.commit();
        }

//        setContentView(R.layout.activity_main);

//        // Create the adapter that will return a fragment for each section
//        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
//                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//            private final Fragment[] mFragments = new Fragment[] {
//                    new RecentPostsFragment(),
//                    new MyRoutesFragment(),
//                    new MyPostsFragment(),
//            };
//            private final String[] mFragmentNames = new String[] {
//                    getString(R.string.heading_recent),
//                    getString(R.string.heading_my_routes),
//                    getString(R.string.heading_my_posts)
//            };
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments[position];
//            }
//            @Override
//            public int getCount() {
//                return mFragments.length;
//            }
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return mFragmentNames[position];
//            }
//        };
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = findViewById(R.id.container);
//        mViewPager.setAdapter(mPagerAdapter);
//        TabLayout tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);

        mOpenDrawerFab = findViewById(R.id.OpenDrawerFab);
        mCloseDrawerFab = findViewById(R.id.CloseDrawerFab);
        mWriteNewPostFab = findViewById(R.id.WriteNewPostFab);
        mStartRecordingFab = findViewById(R.id.StartRecordingFab);
        mStopRecordingFab = findViewById(R.id.StopRecordingFab);

        mOpenDrawerFab.setOnClickListener(this);
        mCloseDrawerFab.setOnClickListener(this);
        mWriteNewPostFab.setOnClickListener(this);
        mStartRecordingFab.setOnClickListener(this);
        mStopRecordingFab.setOnClickListener(this);

    }

    // 位置情報許可の確認、外部ストレージのPermissionにも対応できるようにしておく
    private  void checkMultiPermissions(){
        // 位置情報の Permission
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // 外部ストレージ書き込みの Permission
//        int permissionExtStorage = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ArrayList reqPermissions = new ArrayList<>();

        // 位置情報の Permission が許可されているか確認
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            // 許可済
        }
        else{
            // 未許可
            reqPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        // 未許可
        if (!reqPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[]) reqPermissions.toArray(new String[0]),
                    REQUEST_MULTI_PERMISSIONS);
            // 未許可あり
        }
        else{
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
        }
        else{
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
    private void toastMake(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
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
                    } else if (mStopRecordingFab.getVisibility() == View.GONE){
                        mOpenDrawerFab.setVisibility(View.GONE);
                        mCloseDrawerFab.setVisibility(View.VISIBLE);
                        mWriteNewPostFab.setVisibility(View.VISIBLE);
                        mStartRecordingFab.setVisibility(View.VISIBLE);
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

                default:
                    break;
            }
        }

    }
}

