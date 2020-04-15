package com.example.rootshareapp.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.rootshareapp.fragment.SignUpFragment;
import com.example.rootshareapp.model.User;

public class SignInViewModel extends AndroidViewModel {

    public static final String TAG = "SIGN_IN_REPOSITORY";
    private FirestoreRepositry mRepository;
    private SignInContract.Activity activity;
    private SignInContract.SignUpView mSignUpView;

    private Uri icon_uri;
    private boolean mIsStarted = false;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FirestoreRepositry();
    }

//    public void insertUser(User user) {
//        mRepository.insertUser(user);
//    }
//
//    public void updateUser(User user) {
//        mRepository.updateUser(user);
//    }

    public void setResult(boolean result, Uri uri) {
        if (!result) {
            return;
        }
        mRepository.setTemporaryIcon(uri);
    }

    public void setUserIcon(String downloadUrl) {
        mRepository.setTemporaryDownloadUrl(downloadUrl);
    }

    public String getDownloadUrl() {
        return  mRepository.getTemporaryDownloadUrl();
    }

    public interface SignInContract {
        interface SignUpView {
            void showPhoto(Uri photoImage);
            void setViewModel(SignInViewModel signInViewModel);
            void showPicker();
        }

        interface Activity {

        }

        interface ViewModel {
            void start();
        }
    }
}
