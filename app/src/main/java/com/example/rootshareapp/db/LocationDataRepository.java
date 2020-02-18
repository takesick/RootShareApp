package com.example.rootshareapp.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.rootshareapp.dao.LocationDataDao;
import com.example.rootshareapp.model.local.Local_LocationData;

import java.util.List;

public class LocationDataRepository {
    private LocationDataDao mLocationDataDao;
    private LiveData<List<Local_LocationData>> mLatestRootData;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public LocationDataRepository(Application application) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        mLocationDataDao = db.locationDataDao();
        mLatestRootData = mLocationDataDao.getLatestRoot();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Local_LocationData>> getLatestRoot() {
        return mLatestRootData;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocationDataDao.insert(local_locationData);
            }
        });
    }

    public void update(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocationDataDao.update(local_locationData);
            }
        });
    }

    public void delete(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocationDataDao.delete(local_locationData);
            }
        });
    }

    public void deleteAll(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocationDataDao.deleteAll();
            }
        });
    }


}
