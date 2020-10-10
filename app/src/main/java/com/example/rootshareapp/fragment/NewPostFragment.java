package com.example.rootshareapp.fragment;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.bumptech.glide.Glide;
import com.example.rootshareapp.MainActivity;
import com.example.rootshareapp.R;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;
import com.example.rootshareapp.model.Photo;
import com.example.rootshareapp.model.Post;
import com.example.rootshareapp.model.Public_Location;
import com.example.rootshareapp.model.Public_Route;
import com.example.rootshareapp.viewmodel.LocationDataViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

public class NewPostFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NewPostFragment";
    private static final int REQUEST_PICK_PHOTO = 2;

    private EditText editBodyView;
    private TextView selectRouteBtn, deselectRouteBtn, addSpotBtn;
    private ImageButton cameraBtn, galleryBtn;
    private Button submitBtn;
    private LinearLayout imagesView, imagesSubView;
    private ImageView imageView1, imageView2, imageView3;
    private List<String> photosUri = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private CollectionReference mPostRef, mRouteRef;
    private DocumentReference mLocationRef;

    private Uri uri;
    private List<Uri> mUri = new ArrayList<>();
    private String photos_uri;
    private Post post;
    private Local_Route local_Route = null;
    private List<Local_Location> local_locations;
    private Public_Route mRoute;
    private Public_Location mLocation;
    private List<Public_Location> mLocations = new ArrayList<>();
    private StringBuilder path = new StringBuilder();
    private StringBuilder center_and_zoom = new StringBuilder();
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private Boolean setMap_isStarted = false;

    private LocationDataViewModel mLocationDataViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_post, container, false);

        mLocationDataViewModel = ViewModelProviders.of(getActivity()).get(LocationDataViewModel.class);

        mPostRef = mDatabase.collection("posts");
        mRouteRef = mDatabase.collection("routes");

        mStorageRef = FirebaseStorage.getInstance().getReference("images").child("photos");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editBodyView = view.findViewById(R.id.fieldBody);
        selectRouteBtn = view.findViewById(R.id.selectRouteBtn);
        deselectRouteBtn  = view.findViewById(R.id.deselectRouteBtn);
        addSpotBtn = view.findViewById(R.id.addSpotBtn);
        submitBtn = view.findViewById(R.id.submitBtn);
        cameraBtn = view.findViewById(R.id.cameraBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);

        imagesView = view.findViewById(R.id.media_container);
        imagesSubView = view.findViewById(R.id.sub_image_container);
        imageView1 = view.findViewById(R.id.image_view1);
        imageView2 = view.findViewById(R.id.image_view2);
        imageView3 = view.findViewById(R.id.image_view3);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);

        submitBtn.setOnClickListener(this);
        selectRouteBtn.setOnClickListener(this);
        deselectRouteBtn.setOnClickListener(this);
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

                case R.id.deselectRouteBtn:
                    removeMapImage();
                    deselectRouteBtn.setVisibility(View.GONE);
                    selectRouteBtn.setText("+ ルートを追加");
                    selectRouteBtn.setVisibility(View.VISIBLE);
                    break;

                case R.id.addSpotBtn:
                    break;

                case R.id.submitBtn:
                    createPost();

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
                    showPicker();
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

    public void createPost() {
        post = new Post(
                FirebaseAuth.getInstance().getCurrentUser(),
                getNowDate(),
                editBodyView.getText().toString()
        );

        mPostRef.add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        final String post_id = documentReference.getId();
                        mPostRef.document(post_id).update("id", post_id);
                        if(local_Route!=null) {
                            mRouteRef.add(mRoute)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(final DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            mRouteRef.document(documentReference.getId()).update("ref", documentReference.getPath());
                                            mPostRef.document(post_id).update("route_ref", documentReference);
                                            mPostRef.document(post_id).update("route_name", mRoute.title);
                                            for(int i = 0; i < mLocations.size(); i++) {
                                                mRouteRef.document(documentReference.getId()).collection("locations").add(mLocations.get(i));
                                            }
                                            mDatabase.collection("post-route").document(post_id).set(getHashMap(true));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        } else {
                            mDatabase.collection("post-route").document(post_id).set(getHashMap(false));
                        }

                        if(photosUri != null){
                            if(setMap_isStarted){
                                Photo photo = new Photo(photosUri.get(0), getNowDate());
                                mPostRef.document(post_id).collection("photos").add(photo);
                                for(int i=0; i<mUri.size(); i++) {
                                    uploadImages(mUri.get(mUri.size()-i-1), post_id);
                                }
                                mDatabase.collection("post-photo").document(post_id).set(getHashMap(true));
                            } else {
                                for (int i = 0; i < mUri.size(); i++) {
                                    uploadImages(mUri.get(mUri.size()-i-1), post_id);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
                            mDatabase.collection("post-photo").document(post_id).set(getHashMap(false));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("aaa", "Error adding document", e);
                    }
                });
    }

    public void showPhoto() {
        switch (photosUri.size()) {
            case 0:
                imagesView.setVisibility(View.GONE);
                break;

            case 1:
                imagesView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(photosUri.get(0))
                        .into(imageView1);
//                imageView1.setImageURI(photosUri.get(0));
                imagesSubView.setVisibility(View.GONE);
//                imageView2.setVisibility(View.GONE);
//                imageView3.setVisibility(View.GONE);
                break;

            case 2:
                imagesView.setVisibility(View.VISIBLE);
                imagesSubView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(photosUri.get(0))
                        .into(imageView1);
                Glide.with(getContext())
                        .load(photosUri.get(1))
                        .into(imageView2);
//                imageView2.setImageURI(photosUri.get(1));
                imageView3.setVisibility(View.GONE);
                break;

            case 3:
                imagesView.setVisibility(View.VISIBLE);
                imagesSubView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(photosUri.get(0))
                        .into(imageView1);
                Glide.with(getContext())
                        .load(photosUri.get(1))
                        .into(imageView2);
                Glide.with(getContext())
                        .load(photosUri.get(2))
                        .into(imageView3);
//                imageView1.setImageURI(photosUri.get(0));
//                imageView2.setImageURI(photosUri.get(1));
//                imageView3.setImageURI(photosUri.get(2));
                break;
        }
    }

    public void showPicker() {
        //intentとは意図：新しく欲しいものの条件(他のアプリに伝える条件)
        //Intent.~意図(伝える条件)の編集ができる
        if(setMap_isStarted) {
            int size = photosUri.size();
            Log.e(TAG, String.valueOf(size));
            for (int i = 1; i < size; i++) {
                photosUri.remove(1);
            }
        } else {
            photosUri.clear();
        }
        mUri.clear();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//.ACTION_OPEN_DOCUMENT：ストレージ内のドキュメントプロバイダ内のものを条件として指定
        intent.addCategory(Intent.CATEGORY_OPENABLE);//CATEGORY_OPENABLE開けるものを指定
        intent.setType("image/*");//imageフォルダのjpegを指定
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_PICK);
        /* REQUEST_PICK_PHOTO(REQUEST_CODE) は最初に定義されている値。
        写真選択リクエストの意味の変数名にしておくとよい。
        結果が欲しいので ForResult の方を使う */
        startActivityForResult(intent, REQUEST_PICK_PHOTO);//引数：(出来上がった条件, 意図の送信元のActivityのidみたいなもの)
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getData() != null) {
                // 選択画像が単数の場合の処理
                uri = data.getData();
                Log.e(TAG, String.valueOf(uri));
                photosUri.add(uri.toString());
                mUri.add(uri);
                showPhoto();
            } else {
                // 選択画像が複数の場合の処理
                ClipData cd = data.getClipData();
                if (cd.getItemCount() > 2) {
                    Log.e(TAG, "選択多すぎ");
                    toastMake("画像は3枚以上選択できません。");
                } else {
                    for (int i = 0; i < 2; i++) {
                        uri = cd.getItemAt(i).getUri();
                        photosUri.add(uri.toString());
                        mUri.add(uri);
                    }
                    showPhoto();
                }
            }
        }
    }

    public void setRoute() {
        local_Route = mLocationDataViewModel.getSelectedRoute();
        try {
            local_locations = mLocationDataViewModel.getLocationsWithinRoute(local_Route._id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        selectRouteBtn.setText(local_Route.title);
        convertRouteToPublic();
        convertLocationsToPublic();

        generateString();
        setMapImage();
        selectRouteBtn.setVisibility(View.GONE);
        deselectRouteBtn.setVisibility(View.VISIBLE);
    }

    public void convertRouteToPublic(){
        mRoute = new Public_Route(
                local_Route.title,
                local_Route.created_at,
                local_Route.uid
//                local_Route.spots
        );
    }

    public void convertLocationsToPublic(){
        for(int i = 0; i < local_locations.size(); i++){
            mLocation = new Public_Location(
                    local_locations.get(i).created_at,
                    local_locations.get(i).latitude,
                    local_locations.get(i).longitude,
                    local_locations.get(i).accuracy,
                    local_locations.get(i).created_at,
                    local_locations.get(i).comment
            );
            mLocations.add(mLocation);
        }
    }

    public void generateString() {
        double latitude, longitude, max_lat = 0, min_lat = 0, max_long = 0, min_long = 0;
        LatLng latLng1,latLng2, center;
        int zoom;
        for(int i=0; i<mLocations.size(); i++) {
            latitude = mLocations.get(i).latitude;
            longitude = mLocations.get(i).longitude;

            if(i==0) {
                max_lat = min_lat = latitude;
                max_long = min_long = longitude;
            }
            if(latitude>max_lat) max_lat = latitude;
            if(latitude<min_lat) min_lat = latitude;
            if(longitude>max_long) max_long = longitude;
            if(longitude<min_long) min_long = longitude;

            path.append("|" + latitude + "," + longitude);

        }

        double distance = getDistance(max_lat, min_lat, max_long,min_long);
        if(distance>1) {
            zoom = 14;
        } else if(distance> 0.5 && distance<=1) {
            zoom = 15;
        } else if(distance> 0.2 && distance<=0.5) {
            zoom = 17;
        } else {
            zoom = 18;
        }
        latLng1 = new LatLng(max_lat, max_long);
        latLng2 = new LatLng(min_lat, min_long);

        center = LatLngBounds.builder().include(latLng1).include(latLng2).build().getCenter();
        center_and_zoom.append("&center=" + center.latitude + "," + center.longitude +"&zoom=" + zoom);
    }

    public void setMapImage(){
        String locations = new String(path);
        String camera = new String(center_and_zoom);

        String uri = "https://maps.googleapis.com/maps/api/staticmap?size=200x200&scale=2" +
                camera + "&path=color:0xff0000ff|weight:3" + locations + "&key=" + getContext().getString(R.string.google_maps_key);

        int size = mUri.size();
        if(setMap_isStarted != true && size == 0) {
            photosUri.add(uri);
        } else if (setMap_isStarted) {
            photosUri.set(0, uri);
        } else {
            photosUri.clear();
            photosUri.add(uri);
            for(int i = 0; i < size; i++) {
                photosUri.add(mUri.get(i).toString());
            }
        }
        setMap_isStarted = true;
        showPhoto();
    }

    public void removeMapImage(){
        if(photosUri.size() != 0) {
            photosUri.clear();
            for(int i = 0; i < mUri.size(); i++) {
                photosUri.add(mUri.get(i).toString());
            }
        }
        setMap_isStarted = false;
        showPhoto();
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double dist_km = dist * 60 * 1.1515 * 1.609344;
        return dist_km;
    }

    private double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public double deg2rad(double degrees) {
        return degrees * (Math.PI / 180f);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImages(Uri uri, final String id) {
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(uri));

        mUploadTask = fileReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUri = uri.toString();
                                Photo photo = new Photo(imageUri, getNowDate());
                                mPostRef.document(id).collection("photos").add(photo);
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
    }

    private HashMap<String, Boolean> getHashMap(Boolean existance){
        HashMap<String, Boolean> result = new HashMap<>();
        result.put("existance", true);
        return result;
    }




    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void toastMake(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.show();
    }
}
