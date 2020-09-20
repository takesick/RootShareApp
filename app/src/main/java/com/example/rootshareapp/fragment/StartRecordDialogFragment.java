package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rootshareapp.LocationService;
import com.example.rootshareapp.R;

public class StartRecordDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText route_orig, route_dist;
    private Button permitBtn, cancelBtn;
    private String routeTitle = null, mOrigin, mDestination;

    public static StartRecordDialogFragment newInstance() {
        StartRecordDialogFragment fragment = new StartRecordDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_start_record, container, false);

        route_orig = view.findViewById(R.id.routeOrigin);
        route_dist = view.findViewById(R.id.routeDestination);
        permitBtn = view.findViewById(R.id.buttonPermit);
        cancelBtn = view.findViewById(R.id.buttonCancel);

        permitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPermit:
                onSubmitClicked(v);
                break;
            case R.id.buttonCancel:
                onCancelClicked(v);
                break;
        }
    }

    public void onSubmitClicked(View view) {
        mOrigin = route_orig.getText().toString();
        mDestination = route_dist.getText().toString();
        if((mOrigin != null) || (mDestination != null)) {
            routeTitle = mOrigin + "〜" + mDestination;
            Log.e("intent", routeTitle);
        }
        Intent intent_start = new Intent(getContext(), LocationService.class);
        intent_start.putExtra("routeTitle", routeTitle);
        getActivity().startForegroundService(intent_start);
        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

    public String createTitle(){

        return routeTitle;
    }
}
