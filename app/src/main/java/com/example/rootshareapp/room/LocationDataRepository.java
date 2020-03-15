package com.example.rootshareapp.room;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class LocationDataRepository {
    private LocationDao locationDao;
    private RouteDao routeDao;

    private LiveData<List<Local_RouteData>> mLatestRoutes;
    private LiveData<List<Local_LocationData>> mLatestLocations;
    private LocationDataViewModel mLocationDataViewModel;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public LocationDataRepository(Application application) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        locationDao = db.locationDao();
        routeDao = db.routeDao();
        mLatestLocations = locationDao.getLatestLocations(route_id);
        mLatestRoutes = routeDao.getLatestRoutes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Local_RouteData>> getLatestRoutes() {
        return mLatestRoutes;
    }
    public LiveData<List<Local_LocationData>> getLatestLocations() {
        return mLatestLocations;
    }


    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public long insertRoute(final Local_RouteData local_routeData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            public long run() {
                long id = routeDao.insertRoute(local_routeData);
                return id;
            }
        });

    }

    public void updateRoute(final Local_RouteData local_routeData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.updateRoute(local_routeData);
            }
        });
    }

    public void deleteRoute(final Local_RouteData local_routeData ) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteRoute(local_routeData);
            }
        });
    }

    public void deleteAllRoutes(final Local_RouteData local_routeData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteAllRoutes();
            }
        });
    }

    public void insertLocation(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.insertLocation(local_locationData);
            }
        });
    }

    public void updateLocation(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.updateLocation(local_locationData);
            }
        });
    }

    public void deleteLocation(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteLocation(local_locationData);
            }
        });
    }

    public void deleteAllLocations(final Local_LocationData local_locationData) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteAllLocations();
            }
        });
    }


//    private static class InsertLocationAsyncTask extends AsyncTask<Local_LocationData, Void, Void> {
//        private LocationDao locationDao;
//
//        private InsertLocationAsyncTask(LocationDao locationDao) {
//            this.locationDao = locationDao;
//        }
//
//        @Override
//        protected Void doInBackground(Local_LocationData... local_locationData) {
//            locationDao.insert(local_locationData[0]);
//            return null;
//        }
//    }
//
//    private static class UpdateLocationAsyncTask extends AsyncTask<Local_LocationData, Void, Void> {
//        private LocationDao locationDao;
//
//        private UpdateLocationAsyncTask(LocationDao locationDao) {
//            this.locationDao = locationDao;
//        }
//
//        @Override
//        protected Void doInBackground(Local_LocationData... local_locationData) {
//            locationDao.update(local_locationData[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteLocationAsyncTask extends AsyncTask<Local_LocationData, Void, Void> {
//        private LocationDao locationDao;
//
//        private DeleteLocationAsyncTask(LocationDao locationDao) {
//            this.locationDao = locationDao;
//        }
//
//
//        @Override
//        protected Void doInBackground(Local_LocationData... local_locationData) {
//            locationDao.delete(local_locationData[0]);
//            return null;
//        }
//    }

}
