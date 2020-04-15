package com.example.rootshareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.rootshareapp.fragment.MapFragment;
import com.example.rootshareapp.fragment.SignInFragment;
import com.example.rootshareapp.fragment.SignUpFragment;
import com.example.rootshareapp.model.User;
import com.example.rootshareapp.viewmodel.FirestoreRepositry;
import com.example.rootshareapp.viewmodel.SignInViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity  {

    public static final int REQUEST_ADD_PHOTO = 1;
    private final int REQUEST_PICK_PHOTO = 2;
    private SignInViewModel mSignInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.signInfragment, SignInFragment.newInstance());
            fragmentTransaction.commit();
        }
    }


    public void finishWithResult(boolean result) {
        Intent intent = new Intent(this, SignInActivity.class);
        setResult((result) ? RESULT_OK : RESULT_CANCELED, intent);
        finish();
    }

}
