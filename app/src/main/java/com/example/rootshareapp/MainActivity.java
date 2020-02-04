package com.example.rootshareapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.rootshareapp.fragment.RootsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MULTI_PERMISSIONS = 101;

    private RootsFragment mfragment_roots;
    private FloatingActionButton mStartRecordingFab, mStopRecordingFab;

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

//        if(savedInstanceState == null){
//            // FragmentManagerのインスタンス生成
//            FragmentManager fragmentManager = getSupportFragmentManager();
//
//            // FragmentTransactionのインスタンスを取得
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            // インスタンスに対して張り付け方を指定する
//            fragmentTransaction.replace(R.id.container, new RootsFragment());
//
//            // 張り付けを実行
//            fragmentTransaction.commit();
//        }

    }

    // 位置情報許可の確認、外部ストレージのPermissionにも対応できるようにしておく
    private  void checkMultiPermissions(){
        // 位置情報の Permission
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
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
                startLocationService();
            }
        }
        else{
            //
        }
    }

    private void startLocationService() {
        setContentView(R.layout.roots_frag);

//        textView = findViewById(R.id.log_text);
        mStartRecordingFab = findViewById(R.id.StartRecordingFab);
        mStartRecordingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), LocationService.class);

                // API 26 以降
                startForegroundService(intent);

                // Activityを終了させる
            }
        });


//        Button buttonLog = findViewById(R.id.button_log);
//        buttonLog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textView.setText(fileReadWrite.readFile());
//            }
//        });

        mStopRecordingFab  = findViewById(R.id.StopRecordingFab);
        mStopRecordingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Serviceの停止
                Intent intent = new Intent(getApplication(), LocationService.class);
                stopService(intent);
            }
        });
    }

    // トーストの生成
    private void toastMake(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
    }

}

