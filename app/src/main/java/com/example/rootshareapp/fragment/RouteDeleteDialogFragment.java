package com.example.rootshareapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rootshareapp.R;

public class RouteDeleteDialogFragment extends DialogFragment implements View.OnClickListener {

    private static int route_id;
    private Button submitBtn, cancelbtn;

    public static RouteDeleteDialogFragment newInstance(int id) {
        RouteDeleteDialogFragment fragment = new RouteDeleteDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        route_id = id;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_route_delete, container, false);

        submitBtn = view.findViewById(R.id.buttonPermit);
        cancelbtn = view.findViewById(R.id.buttonCancel);

        submitBtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        MyRoutesFragment fragment = (MyRoutesFragment) getParentFragment();
        fragment.onDeleteRoute(route_id);
        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

}
