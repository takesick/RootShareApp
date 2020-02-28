package com.example.rootshareapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.rootshareapp.fragment.LocationDetailFragment;
import com.example.rootshareapp.fragment.RouteDetailFragment;

public class LocationFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public LocationFragmentStatePagerAdapter(FragmentManager fm, LocationFragmentStatePagerAdapter mAdapter) {
        super( fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch(i){
            case 0:
                return new LocationDetailFragment();
            default:
                return new RouteDetailFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
