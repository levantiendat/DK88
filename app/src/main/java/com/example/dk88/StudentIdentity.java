package com.example.dk88;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class StudentIdentity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1000;
    ImageButton imgFront,imgBack;
    ImageView image;
    private static final String TAG=StudentIdentity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_active_request_layout);

        String token=getIntent().getStringExtra("token");
        Student student=(Student) getIntent().getSerializableExtra("student");

        imgFront= findViewById(R.id.imgFront);
        imgBack=(ImageButton) findViewById(R.id.imgBack);
        image=(ImageView) findViewById(R.id.picture);

        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG,"onActivityResult");
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data=result.getData();
                        if(data==null){
                            return;
                        }
                        Uri uri=data.getData();
                        try{
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            image.setImageBitmap(bitmap);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private void onClickRequestPermission() {

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }
}
