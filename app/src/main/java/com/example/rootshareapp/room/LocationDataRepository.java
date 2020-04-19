package com.example.rootshareapp.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LocationDataRepository {
    private LocationDao locationDao;
    private RouteDao routeDao;

    private LiveData<List<Local_Route>> mLatestRoutes;
    private LiveData<List<Local_Location>> mLatestLocations;

    public LocationDataRepository(Application application) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        routeDao = db.routeDao();
        locationDao = db.locationDao();
    }

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Local_Route>> getLatestRoutes() {
        mLatestRoutes = routeDao.getLatestRoutes();
        return mLatestRoutes;
    }
    public LiveData<List<Local_Location>> getLatestLocations(int route_id) {
        mLatestLocations = locationDao.getLatestLocations(route_id);
        return mLatestLocations;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public Long insertRoute(final Local_Route local_route) throws ExecutionException, InterruptedException {
        Future<Long> future = LocationRoomDatabase.databaseWriteExecutor.submit(new Callable<Long>() {
            public Long call() {
                return routeDao.insertRoute(local_route);
            }
        });
        return future.get();
    }

    public void updateRoute(final Local_Route local_route) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.updateRoute(local_route);
            }
        });
    }

    public void deleteRoute(final Local_Route local_route) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteRoute(local_route);
            }
        });
    }

    public void deleteAllRoutes(final Local_Route local_route) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteAllRoutes();
            }
        });
    }

    public void insertLocation(final Local_Location local_location) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.insertLocation(local_location);
            }
        });
    }

    public void updateLocation(final Local_Location local_location) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.updateLocation(local_location);
            }
        });
    }

    public void deleteLocation(final Local_Location local_location) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteLocation(local_location);
            }
        });
    }

    public void deleteAllLocations() {
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

    private static class getSelectedLocationAsyncTask extends AsyncTask<Integer, Void, LiveData<Local_Location>> {
        private LocationDao locationDao;

        private getSelectedLocationAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected LiveData<Local_Location> doInBackground(Integer... location_id) {
            return locationDao.getSelectedLocation(location_id[0]);
        }

        @Override
        protected void onPostExecute(LiveData<Local_Location> result) {
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
