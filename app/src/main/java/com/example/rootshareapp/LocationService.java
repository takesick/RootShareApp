package com.example.rootshareapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

//import com.example.rootshareapp.sqlite.LocationOpenHelper;

public class LocationService extends Service implements LocationListener {
    private LocationManager locationManager;
    private Context context;

    private static final int MinTime = 10;
    private static final float MinDistance = 1;

    private LocationDataViewModel mLocationDataViewModel = new LocationDataViewModel(getApplication());

    int num = 1;
    private String startDate;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String created_at;
    private int route_id;
    String title = null;
    String uid;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        startDate = getNowDate();
        uid = getUid();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int requestCode = 0;
        String channelId = "default";

        Bundle extras = intent.getExtras();
        Log.e("intent", String.valueOf(extras));
        if(extras.get("routeTitle") != null) {
            title = extras.getString("routeTitle");

        }

//        PendingIntent：
//        intentを予約して指定したタイミングで発行する。
//        難しいことは抜きにしてAlarmManagerやNotification等で何かイベントを起こしたいときに用いる
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                    context, requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // ForegroundにするためNotificationが必要、Contextを設定
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification　Channel 設定
        NotificationChannel channel = new NotificationChannel(
                channelId, title , NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Silent Notification");
        // 通知音を消さないと毎回通知音が出てしまう
        // この辺りの設定はcleanにしてから変更
        channel.setSound(null,null);
        // 通知ランプを消す
        channel.enableLights(false);
        channel.setLightColor(Color.BLUE);
        // 通知バイブレーション無し
        channel.enableVibration(false);

        if(notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    // 本来なら衛星のアイコンですがandroid標準アイコンを設定
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setContentText("GPS")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();

            // startForeground
            startForeground(1, notification);
        }

        startGPS();

        return START_NOT_STICKY;
    }

    protected void startGPS() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("startGPS\n");

        final boolean gpsEnabled
                = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            enableLocationSettings();
        }

        if (locationManager != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                if(title == null) title = startDate;

                created_at = startDate;
                route_id = writeRouteDataToDb(title, created_at, uid).intValue();
                
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MinTime, MinDistance, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MinTime, MinDistance, this);
                Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                Log.e("location", String.valueOf(location));
                writeLocationDataToDb(location);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            strBuf.append("locationManager=null\n");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        writeLocationDataToDb(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Android 6, API 23以上でパーミッシンの確認
//        if(Build.VERSION.SDK_INT <= 28){
//            StringBuilder strBuf = new StringBuilder();
//
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    //strBuf.append("LocationProvider.AVAILABLE\n");
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:
//                    strBuf.append("LocationProvider.OUT_OF_SERVICE\n");
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    strBuf.append("LocationProvider.TEMPORARILY_UNAVAILABLE\n");
//                    break;
//            }
//
//            fileReadWrite.writeFile(strBuf.toString(), true);
//        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    private void stopGPS(){
        if (locationManager != null) {
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGPS();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        Log.e("intent", String.valueOf(extras));
        if(extras.get("routeTitle") != null) {
            title = extras.get("routeTitle").toString();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void writeLocationDataToDb(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        String currentTime = sdf.format(location.getTime());
        created_at = currentTime;

        String comment = "";

        Local_Location local_location = new Local_Location(latitude, longitude, accuracy, created_at, uid, route_id, comment);
        mLocationDataViewModel.insertLocation(local_location);

        String message = "Now recording(n=" + num + ")";
        toastMake(message);
        num++;

    }

    private Long writeRouteDataToDb(String title, String created_at, String uid) throws ExecutionException, InterruptedException {
        Local_Route local_route = new Local_Route(title, created_at, uid);
        String message = "Start recording";
        toastMake(message);
        return mLocationDataViewModel.insertRoute(local_route);
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void toastMake(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.show();
    }

//    public void swapCursor(Cursor mCursor, Cursor newCursor) {
//        if (mCursor != null) {
//            mCursor.close();
//        }
//
//        mCursor = newCursor;
//        if (newCursor != null){
//            notifyDataSetChanged();
//        }
//    }
}
