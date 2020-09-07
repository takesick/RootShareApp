package com.example.rootshareapp;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_GALLERY = 1;

    private Uri uri;
    private List<Uri> photos = new ArrayList<>();
    boolean mIsStarted = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_review);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsStarted) {
            showPicker();
            mIsStarted = true;
        }
    }

    public void showPicker() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "画像を選択"), REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    // 選択画像が単数の場合の処理
                    uri = data.getData();
                    photos.add(uri);
                } else {
                    // 選択画像が複数の場合の処理
                    ClipData cd = data.getClipData();
                    for (int i = 0; i < cd.getItemCount(); i++) {
                        uri = cd.getItemAt(i).getUri();
                        photos.add(uri);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
