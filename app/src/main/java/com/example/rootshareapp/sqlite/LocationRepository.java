package com.example.rootshareapp.sqlite;

import android.content.Context;

import java.lang.ref.WeakReference;

public class LocationRepository {

//    private static LocationRepository mInstance = null;
//    private final LocationOpenHelper mDBHelper;
//    private final List<LocationData> mLocations;
//    private long mTemporaryLocationId;
//    private final WeakReference<Context> mContextRef;
//
//    private LocationRepository(Context context) {
//        mContextRef = new WeakReference<>(context);
//        Context context = mContextRef.get();
//        mDBHelper = new LocationOpenHelper(context, "locations.db", null, 1);
//        mLocations.addAll(mDBHelper.readData());
//    }
//
//    static public LocationRepository getInstance(Context context) {
//        if (mInstance == null)
//            mInstance = new LocationRepository(context);
//        return mInstance;
//    }
//    public void savePhoto(String memo) {
//        mDBHelper.insertData(mTemporaryPhotoUri.toString(), memo);
//        mTemporaryLocationId = null;
//    }
//
//    public void removePhoto(int index) {
//        mLocations.remove(index);
//    }
//
//    public void setTemporaryPhoto(Long id) {
//        mTemporaryLocationId = id;
//    }
//
//    public List<LocationData> getPhotos() {
//        return mLocations;
//    }

}
