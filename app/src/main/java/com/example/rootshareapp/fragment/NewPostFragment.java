package com.example.rootshareapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rootshareapp.R;

public class NewPostFragment extends Fragment implements View.OnClickListener {

    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_post, container, false);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mRecyclerView = view.findViewById(R.id.featuredSpots);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onClick(View v) {
//        if (v != null) {
//            switch (v.getId()) {
//                case R.id.selectRouteBtn:
////                    CustomDialogFragment newFragment = CustomDialogFragment.newInstance();
////                    newFragment.show(getFragmentManager(), "dialog");
//                    break;
//
//                case R.id.addSpotBtn:
//                    break;
//
//                case R.id.submitBtn:
//                    break;
//
//                case R.id.cameraBtn:
//                    break;
//
//                case R.id.gallaryBtn:
//                    break;
//
//                default:
//                    break;
//            }
//        }
    }
}
