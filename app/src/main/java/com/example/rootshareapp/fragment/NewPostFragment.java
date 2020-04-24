package com.example.rootshareapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.rootshareapp.MainActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.model.Post;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewPostFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NewPostFragment";
    private static final int REQUEST_PICK_PHOTO = 2;

    private EditText editBodyView;
    private TextView selectRouteBtn, addSpotBtn;
    private ImageButton cameraBtn, galleryBtn;
    private Button submitBtn;

    private RecyclerView mRecyclerView;
    private FirebaseFirestore mDatabase;
    private CollectionReference postRef;

    private String uid, created_at, body;
    private Post post;
    private Local_Route local_Route;

    private LocationDataViewModel mLocationDataViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_post, container, false);
        mRecyclerView = view.findViewById(R.id.featuredSpots);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);
        mDatabase = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editBodyView = view.findViewById(R.id.fieldBody);
        selectRouteBtn = view.findViewById(R.id.selectRouteBtn);
        addSpotBtn = view.findViewById(R.id.addSpotBtn);
        submitBtn = view.findViewById(R.id.submitBtn);
        cameraBtn = view.findViewById(R.id.cameraBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);

        submitBtn.setOnClickListener(this);
        selectRouteBtn.setOnClickListener(this);
//        addSpotBtn.setOnClickListener(this);
//        cameraBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.selectRouteBtn:
                    AddRouteDialogFragment newFragment = AddRouteDialogFragment.newInstance();
                    Log.e("dialog", "show");
                    newFragment.show(getChildFragmentManager(), "dialog");
                    break;

                case R.id.addSpotBtn:
                    break;

                case R.id.submitBtn:
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    createPost(uid);
                    mDatabase.collection("posts")
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                    Client client = new Client(getString(R.string.algolia_id), getString(R.string.algolia_key));
                    Index index = client.getIndex("posts");

                    List<JSONObject> postList = new ArrayList<>();
                    postList.add(new JSONObject(post.toMap()));
                    index.addObjectsAsync(new JSONArray(postList), null);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    break;

                case R.id.cameraBtn:
                    break;

                case R.id.galleryBtn:
                    break;

                default:
                    break;
            }
        }
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public void createPost(String uid) {
        created_at = getNowDate();
        body = editBodyView.getText().toString();
        post = new Post(uid, created_at, body);
    }

    public void showPhoto(Uri photoImage) {
//        mUserIcon.setImageURI(photoImage);
//        mIconUri = photoImage;
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

    public void setRoute() {
        local_Route = mLocationDataViewModel.getSelectedRoute();
        selectRouteBtn.setText(local_Route.title);
    }
}
