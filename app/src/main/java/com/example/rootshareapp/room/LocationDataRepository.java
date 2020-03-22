package com.example.rootshareapp.room;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.transform.Result;

public class LocationDataRepository {
    private LocationDao locationDao;
    private RouteDao routeDao;

    private LiveData<List<Local_RouteData>> mLatestRoutes;
    private LiveData<List<Local_LocationData>> mLatestLocations;
    private LiveData<Local_LocationData> mLocal_LocationData;

    public LocationDataRepository(Application application) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        routeDao = db.routeDao();
        locationDao = db.locationDao();
        mLatestRoutes = routeDao.getLatestRoutes();
    }

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public LocationDataRepository(Application application, int route_id) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        locationDao = db.locationDao();
        mLatestLocations = locationDao.getLatestLocations(route_id);
    }

    public LocationDataRepository(Application application, long location_id) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        int id = Math.toIntExact(location_id);
        locationDao = db.locationDao();
        routeDao = db.routeDao();
        mLocal_LocationData = locationDao.getSelectedLocation(id);
    }



    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Local_RouteData>> getLatestRoutes() {
        return mLatestRoutes;
    }
    public LiveData<List<Local_LocationData>> getLatestLocations(int route_id) {
        mLatestLocations = locationDao.getLatestLocations(route_id);
        return mLatestLocations;
    }

    public LiveData<Local_LocationData> getSelectedLocation(int location_id){
        Log.e("AsyncTaskCallback", String.valueOf(location_id));
        getSelectedLocationAsyncTask Task = new getSelectedLocationAsyncTask(locationDao);
        Task.execute(location_id);
        return mLocal_LocationData;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public Long insertRoute(final Local_RouteData local_routeData) throws ExecutionException, InterruptedException {
        Future<Long> future = LocationRoomDatabase.databaseWriteExecutor.submit(new Callable<Long>() {
            public Long call() {
                return routeDao.insertRoute(local_routeData);
            }
        });
        return future.get();
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


//    private static class InsertLocationAsyncTask extends AsyncTask<Local_LocationData, Void, Long> {
//        private LocationDao locationDao;
//
//        private InsertLocationAsyncTask(LocationDao locationDao) {
//            this.locationDao = locationDao;
//        }
//
//        @Override
//        protected Long doInBackground(Local_LocationData... local_locationData) {
//            return locationDao.insertLocation(local_locationData[0]);
//        }
//    }
//
//    private static class UpdateLocationAsyncTask extends AsyncTask<Local_LocationData, Void, Long> {
//        private LocationDao locationDao;
//
//        private UpdateLocationAsyncTask(LocationDao locationDao) {
//            this.locationDao = locationDao;
//        }
//
//        @Override
//        protected Long doInBackground(Local_LocationData... local_locationData) {
//            return locationDao.updateLocation(local_locationData[0]);
//        }
//    }

    private static class getSelectedLocationAsyncTask extends AsyncTask<Integer, Void, LiveData<Local_LocationData>> {
        private LocationDao locationDao;

        private getSelectedLocationAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected LiveData<Local_LocationData> doInBackground(Integer... location_id) {
            return locationDao.getSelectedLocation(location_id[0]);
        }

        @Override
        protected void onPostExecute(LiveData<Local_LocationData> result) {
            super.onPostExecute(result);
        }
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

}
