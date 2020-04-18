package com.example.rootshareapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Route;
import com.google.firebase.auth.FirebaseAuth;

public class AddRouteDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "AddRouteDialog";
    private RadioButton mB;

    interface OnRouteListener {

        void addRoute(Route route);

    }

    private OnRouteListener mOnRouteListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_route, container, false);
        mRatingText = v.findViewById(R.id.restaurant_form_text);

        v.findViewById(R.id.restaurant_form_button).setOnClickListener(this);
        v.findViewById(R.id.restaurant_form_cancel).setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnRouteListener) {
            mOnRouteListener = (OnRouteListener) context;
        }
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
            case R.id.restaurant_form_button:
                onSubmitClicked(v);
                break;
            case R.id.restaurant_form_cancel:
                onCancelClicked(v);
                break;
        }
    }

    public void onSubmitClicked(View view) {
        Route route = new Route(
                FirebaseAuth.getInstance().getCurrentUser(),
                mRatingBar.getRating(),
                mRatingText.getText().toString());

        if (mOnRouteListener != null) {
            mOnRouteListener.addRoute(Route);
        }

        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }
}
