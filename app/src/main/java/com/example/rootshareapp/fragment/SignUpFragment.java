package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.example.rootshareapp.MainActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.SignInActivity;
import com.example.rootshareapp.model.User;
import com.example.rootshareapp.room.Local_LocationData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpFragment extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mUserNameField;
    private EditText mDisplayName;
    private EditText mPasswordField;
    private ImageView mUserIcon;
    private Button mSignInButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sign_up);

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = findViewById(R.id.fieldEmail);
        mUserNameField = findViewById(R.id.fieldUerName);
        mDisplayName = findViewById(R.id.fieldDisplayName);
        mPasswordField = findViewById(R.id.fieldPassword);
        mUserIcon = findViewById(R.id.iconUser);
        mSignInButton = findViewById(R.id.buttonSignIn);
        mSignUpButton = findViewById(R.id.buttonSignUp);

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    public static LocationDetailFragment newInstance() {

        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

//        showProgressBar();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
//                        hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = mUserNameField.getText().toString();
        String displayname = mDisplayName.getText().toString();
        Uri user_icon = FileProvider.getUriForFile(getApplicationContext(), "user_icon_path", mUserIcon.get)

        // Write new user
        writeNewUser(user.getUid(), , user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mUserNameField.getText().toString())) {
            mUserNameField.setError("Required");
            result = false;
        } else {
            mUserNameField.setError(null);
        }

        if (TextUtils.isEmpty(mDisplayName.getText().toString())) {
            mDisplayName.setError("Required");
            result = false;
        } else {
            mDisplayName.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    public void showPicker() {
        //intentとは意図：新しく欲しいものの条件(他のアプリに伝える条件)
        //Intent.~意図(伝える条件)の編集ができる
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//.ACTION_OPEN_DOCUMENT：ストレージ内のドキュメントプロバイダ内のものを条件として指定
        intent.addCategory(Intent.CATEGORY_OPENABLE);//CATEGORY_OPENABLE開けるものを指定
        intent.setType("image/jpeg");//imageフォルダのjpegを指定
        /* REQUEST_PICK_PHOTO(REQUEST_CODE) は最初に定義されている値。
        写真選択リクエストの意味の変数名にしておくとよい。
        結果が欲しいので ForResult の方を使う */
        startActivityForResult(intent, REQUEST_PICK_PHOTO);//引数：(出来上がった条件, 意図の送信元のActivityのidみたいなもの)
    }

//    /* 結果を受け取るための callback 関数 */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode != REQUEST_PICK_PHOTO)
//            return;
//        if (resultCode != RESULT_OK)
//            return;
//        /* data の getData() で選択された画像の URI が取れるので、
//        それを ImageView に設定すれば画像が表示される。 */
//        Uri uri = data.getData();
//        /* Layout に入れた ImageView に java コードからアクセスするには
//        findViewById() を使ってインスタンスを取得すればよい。 */
//        mPresenter.result(resultCode == RESULT_OK, uri);
//    }

    public void finishWithResult(boolean result) {
        Intent intent = new Intent(this, PhotosActivity.class);
        setResult((result) ? RESULT_OK : RESULT_CANCELED, intent);
        finish();
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.collection("users").document(userId).set(user);
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {

    }
}
