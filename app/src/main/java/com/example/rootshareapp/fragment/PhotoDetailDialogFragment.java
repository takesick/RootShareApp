package com.example.rootshareapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.rootshareapp.R;

public class PhotoDetailDialogFragment extends DialogFragment {
    public static final String TAG = "AddRouteDialog";
    private ImageView imageView;

    public static PhotoDetailDialogFragment newInstance(String url) {
        PhotoDetailDialogFragment fragment = new PhotoDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("imageUrl", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_image_detail, container, false);

        imageView = view.findViewById(R.id.photo_detail);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Bundle args = getArguments();
        String url = args.getString("imageUrl");
        Glide.with(getContext())
                .load(url)
                .into(imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("dialog", "show");
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

}
