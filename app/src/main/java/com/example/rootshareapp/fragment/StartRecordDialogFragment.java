package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rootshareapp.LocationService;
import com.example.rootshareapp.R;

public class StartRecordDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button permitBtn, cancelBtn;

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
        Intent intent_start = new Intent(getContext(), LocationService.class);
        getActivity().startForegroundService(intent_start);
        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }
}
