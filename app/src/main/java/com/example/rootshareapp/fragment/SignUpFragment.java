package com.example.rootshareapp.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rootshareapp.R;
import com.example.rootshareapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    private final int REQUEST_PICK_PHOTO = 2;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private View view;

    private EditText mEmailField;
    private EditText mUserNameField;
    private EditText mDisplayName;
    private EditText mPasswordField;
    private ImageView mUserIcon;
    private Button mChangeIconBtn;
    private Button mSignInButton;
    private Button mSignUpButton;

    private Uri mIconUri;

    public static SignUpFragment newInstance() {

        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sign_up, container, false);
        // Views
        mEmailField = view.findViewById(R.id.fieldEmail);
        mUserNameField = view.findViewById(R.id.fieldUerName);
        mDisplayName = view.findViewById(R.id.fieldDisplayName);
        mPasswordField = view.findViewById(R.id.fieldPassword);
        mUserIcon = view.findViewById(R.id.iconUser);
        mChangeIconBtn = view.findViewById(R.id.changeIconBtn);
        mSignInButton = view.findViewById(R.id.toSignInBtn);
        mSignUpButton = view.findViewById(R.id.signUpBtn);

        mStorageRef = FirebaseStorage.getInstance().getReference("images").child("user_icon");

        // Click listeners
        mChangeIconBtn.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
//                        hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getContext(), "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Write new user
        uploadUserIcon(user);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.signInfragment, SignInFragment.newInstance());
        fragmentTransaction.commit();
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

    // [START basic_write]
    private void writeNewUser(String userId, String email, String user_icon) {
        String username = "@" + mUserNameField.getText().toString();
        String displayname = mDisplayName.getText().toString();
        User user = new User(username, displayname, email, user_icon);

        mDatabase.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_PICK_PHOTO)
            return;
        if (resultCode != RESULT_OK)
            return;
        /* data の getData() で選択された画像の URI が取れるので、
        それを ImageView に設定すれば画像が表示される。 */
        Uri uri = data.getData();
        Log.e("s", String.valueOf(uri));
        /* Layout に入れた ImageView に java コードからアクセスするには
        findViewById() を使ってインスタンスを取得すればよい。 */
        showPhoto(uri);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.changeIconBtn:
                    showPicker();
                    break;

                case R.id.signUpBtn:
                    signUp();
                    break;

                case R.id.toSignInBtn:
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, SignInFragment.newInstance());
                    fragmentTransaction.commit();
                    break;

                default:
                    break;
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadUserIcon(final FirebaseUser user) {
        if (mIconUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mIconUri));

            mUploadTask = fileReference.putFile(mIconUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String user_icon = uri.toString();
                                    writeNewUser(user.getUid(), user.getEmail(), user_icon);
                                    //Do what you want with the url
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPhoto(Uri photoImage) {
        mUserIcon.setImageURI(photoImage);
        mIconUri = photoImage;
    }

    public void showPicker() {
        //intentとは意図：新しく欲しいものの条件(他のアプリに伝える条件)
        //Intent.~意図(伝える条件)の編集ができる
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//.ACTION_OPEN_DOCUMENT：ストレージ内のドキュメントプロバイダ内のものを条件として指定
        intent.addCategory(Intent.CATEGORY_OPENABLE);//CATEGORY_OPENABLE開けるものを指定
        intent.setType("image/*");//imageフォルダのjpegを指定
        /* REQUEST_PICK_PHOTO(REQUEST_CODE) は最初に定義されている値。
        写真選択リクエストの意味の変数名にしておくとよい。
        結果が欲しいので ForResult の方を使う */
        startActivityForResult(intent, REQUEST_PICK_PHOTO);//引数：(出来上がった条件, 意図の送信元のActivityのidみたいなもの)
    }
}
