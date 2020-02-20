package com.example.rootshareapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.rootshareapp.sqlite.Local_Location;
import com.example.rootshareapp.sqlite.LocationContract;
import com.example.rootshareapp.sqlite.LocationOpenHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class LocationService extends Service implements LocationListener {
    private LocationManager locationManager;
    private Context context;

    private static final int MinTime = 300;
    private static final float MinDistance = 1;

//    private StorageReadWrite fileReadWrite;
    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private SQLiteDatabase db;

    private String startDate;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String created_at;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        startDate = getNowDate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int requestCode = 0;
        String channelId = "default";
        String title = context.getString(R.string.app_name);

//        PendingIntent：
//        intentを予約して指定したタイミングで発行する。
//        難しいことは抜きにしてAlarmManagerやNotification等で何かイベントを起こしたいときに用いる
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, requestCode,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // ForegroundにするためNotificationが必要、Contextを設定
        NotificationManager notificationManager =
                (NotificationManager)context.
                        getSystemService(Context.NOTIFICATION_SERVICE);

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
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300, 1, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MinTime, MinDistance, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            strBuf.append("locationManager=null\n");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        accuracy = location.getAccuracy();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        String currentTime = sdf.format(location.getTime());
        created_at = currentTime;

        writeToDatabase(latitude, longitude, accuracy, created_at);

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
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
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
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void writeToDatabase(double latitude, double longitude, double accuracy, String created_at) {
        final String tag = startDate;
        final String uid = getUid();

        Local_Location location = new Local_Location(tag, latitude, longitude, accuracy, created_at, uid);
        //        open db
        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(this);
//        データベースファイルの削除
//        SQLiteDatabase.deleteDatabase(context.getDatabasePath(locationOpenHelper.getDatabaseName()));
        db = locationOpenHelper.getWritableDatabase();

//        処理(select, insert, delete, update)
        ContentValues newLocation = new ContentValues();
//        newLocation.put(LocationContract.Locations.COL_TAG, tag);
        newLocation.put(LocationContract.Locations.COL_LATITUDE, latitude);
        newLocation.put(LocationContract.Locations.COL_LONGITUDE, longitude);
        newLocation.put(LocationContract.Locations.COL_ACCURACY, accuracy);
        newLocation.put(LocationContract.Locations.COL_CREATED_AT, created_at);
        newLocation.put(LocationContract.Locations.COL_COMMENT, "");
        newLocation.put(LocationContract.Locations.COL_UID, uid);
        long newId = db.insert(
                LocationContract.Locations.TABLE_NAME,
                null,
                newLocation
        );
        Cursor cursor = null;
        cursor = db.query(
                LocationContract.Locations.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                LocationContract.Locations._ID + " desc",
                "1"
        );
        Log.e("DB_TEST", "Count: " + cursor.getCount());
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(LocationContract.Locations._ID));
            double db_latitude = cursor.getDouble(cursor.getColumnIndex(LocationContract.Locations.COL_LATITUDE));
            double db_longitude = cursor.getDouble(cursor.getColumnIndex(LocationContract.Locations.COL_LONGITUDE));
            String db_created_at = cursor.getString(cursor.getColumnIndex(LocationContract.Locations.COL_CREATED_AT));
            Log.e("DB_TEST", "id: " + id + ", created_at: " + db_created_at + ", latitude: " + db_latitude + ", longitude: " + db_longitude);
        }

        cursor.close();

//        close db
        db.close();

//        Map<String, Object> locationData = location.toMap();

// Add a new document with a generated ID
        mDatabase.collection("locations")
                .add(location)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        mDatabase.collection("locations")
                .whereEqualTo("title", startDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                final String uid = getUid();
                                final String lid = document.getId();

                                mDatabase.collection("roots").document(uid)
                                        .collection(document.getString("title")).document(lid)
                                        .set(document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
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
