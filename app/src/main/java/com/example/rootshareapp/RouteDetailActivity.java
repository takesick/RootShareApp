package com.example.rootshareapp;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.rootshareapp.fragment.LocalMapFragment;
import com.example.rootshareapp.fragment.LocationFragment;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;

import java.util.concurrent.ExecutionException;

public class RouteDetailActivity extends AppCompatActivity {

    public static final String KEY_ROUTE_ID = "key_route_id";
    public static final String KEY_LOCATION_ID = "key_location_id";

    private LocationDataViewModel mLocationDataViewModel;
    private int route_id;
    private Local_Route local_route;
    private EditText routeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        routeTitle = findViewById(R.id.routeTitle);
        mLocationDataViewModel = ViewModelProviders.of(this).get(LocationDataViewModel.class);
        route_id = getIntent().getExtras().getInt(RouteDetailActivity.KEY_ROUTE_ID);
        try {
            local_route = mLocationDataViewModel.getSelectedRoute(route_id);
            routeTitle.setText(local_route.title);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.add(R.id.map_container, new LocalMapFragment());
            fragmentTransaction1.commit();
            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
            fragmentTransaction2.add(R.id.location_container, new LocationFragment());
            fragmentTransaction2.commit();

        }
    }
}

