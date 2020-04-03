package com.example.rootshareapp.room;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class LocationDataViewModel extends AndroidViewModel {

    private LocationDataRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Local_RouteData>> mLatestRoutes;
    private LiveData<List<Local_LocationData>> mLatestLocations;
    private Local_LocationData mSelectedLocation;
    private int temporary_id;

    public LocationDataViewModel(Application application) {
        super(application);
        mRepository = new LocationDataRepository(application);
    }


    public LiveData<List<Local_RouteData>> getLatestRoutes() {
        mLatestRoutes = mRepository.getLatestRoutes();
        return mLatestRoutes;
    }

    public LiveData<List<Local_LocationData>> getLatestLocations(int route_id) {
        mLatestLocations = mRepository.getLatestLocations(route_id);
        return mLatestLocations;
    }

    public Long insertRoute(Local_RouteData local_routeData) throws ExecutionException, InterruptedException {
        return mRepository.insertRoute(local_routeData);
    }

    public void updateRoute(Local_RouteData local_routeData) {
        mRepository.updateRoute(local_routeData);
    }

    public void deleteRoute(Local_RouteData local_routeData) {
        mRepository.deleteRoute(local_routeData);
    }

    public void deleteAllRoutes(Local_RouteData local_routeData) {
        mRepository.deleteAllRoutes(local_routeData);
    }

    public void insertLocation(Local_LocationData local_locationData) {
        mRepository.insertLocation(local_locationData);
    }

    public void updateLocation(Local_LocationData local_locationData) {
        mRepository.updateLocation(local_locationData);
    }

    public void deleteLocation(Local_LocationData local_locationData) {
        mRepository.deleteLocation(local_locationData);
    }

    public void deleteAllLocations(Local_LocationData local_locationData) {
        mRepository.deleteAllLocations(local_locationData);
    }

    public void setSelectedLocation(Local_LocationData local_locationData) {
        mSelectedLocation = local_locationData;
    }

    public Local_LocationData  getSelectedLocation() {
        return mSelectedLocation;
    }

}

//class MyRoutesFragment extends Fragment {
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LocationDataViewModel model = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
//        model.getSelectedLocation();
////        model.getSelected().observe(this, { item ->
////                // Update the UI.
////        });
//
//    }
//}