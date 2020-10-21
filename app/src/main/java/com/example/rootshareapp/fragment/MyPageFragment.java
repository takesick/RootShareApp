package com.example.rootshareapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";

    private View view;
    private ViewPager mViewPager;
    private DocumentReference mUserRef;

    private ImageView mUserIcon;
    private TextView mDisplayName;
    private TextView mUserName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_my_page, container, false);

        mUserIcon = view.findViewById(R.id.my_page_user_icon);
        mDisplayName = view.findViewById(R.id.my_page_display_name);
        mUserName = view.findViewById(R.id.my_page_user_name);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserRef = FirebaseFirestore.getInstance().collection("users").document(getUid());
        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();
                     if (document.exists()) {
                         Log.e(TAG, "DocumentSnapshot data: " + document.getData().get("user_icon"));
                         if(document.getData().get("user_icon") != null) {
                             Glide.with(getContext())
                                     .load(document.getData().get("user_icon"))
                                     .into(mUserIcon);
                         }

                         mDisplayName.setText(String.valueOf(document.getData().get("display_name")));
                         mUserName.setText(String.valueOf(document.getData().get("user_name")));

                     } else {
                         Log.d(TAG, "No such document");
                     }
                 } else {
                     Log.d(TAG, "get failed with ", task.getException());
                 }
             }
         });

        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    new MyPostFragment(),
                    new MyFavoriteFragment()
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.my_page_tab_1_label),
                    getString(R.string.my_page_tab_2_label)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

        };

// Set up the ViewPager with the sections adapter.
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
