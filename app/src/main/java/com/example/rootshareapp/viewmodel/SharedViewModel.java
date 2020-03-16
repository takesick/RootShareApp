package com.example.rootshareapp.viewmodel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

public class SharedViewModel extends ViewModel {
//    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();
//
//    public void select(Item item) {
//        selected.setValue(item);
//    }
//
//    public LiveData<Item> getSelected() {
//        return selected;
//    }
//}
//
//public class SQLiteRouteDetailFragment extends Fragment {
//    private SharedViewModel model;
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
//        itemSelector.setOnClickListener(item -> {
//            model.select(item);
//        });
//    }
//}
//
//public class LocationDetailFragment extends Fragment {
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
//        model.getSelected().observe(this, { item ->
//                // Update the UI.
//        });
//    }
}
