package com.example.rootshareapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rootshareapp.db.LocationDataRepository;
import com.example.rootshareapp.model.local.Local_LocationData;

import java.util.List;


public class LocationDataViewModel extends AndroidViewModel {

    private LocationDataRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Local_LocationData>> mLatestRoot;

    public LocationDataViewModel(Application application) {
        super(application);
        mRepository = new LocationDataRepository(application);
        mLatestRoot = mRepository.getLatestRoot();
    }

    public LiveData<List<Local_LocationData>> getLatestRoot() {
        return mLatestRoot;
    }

    public void insert(Local_LocationData local_locationData) {
        mRepository.insert(local_locationData);
    }
}
