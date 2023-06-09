package com.example.dk88.View;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dk88.Controller.StudentActiveController;
import com.example.dk88.R;

import java.io.FileNotFoundException;

public class StudentActiveActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private ImageButton imgFront, imgBack;
    private ImageView imageFront, imageBack;
    private String token;
    private Uri mUri1, mUri2;
    private Button btnOK;
    private String strFront = "", strBack = "";
    private String studentID;
    private int check = 0;
    private StudentActiveController mStudentActiveController;
    private static final String TAG = StudentActiveActivity.class.getName();


    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        if (mStudentActiveController.imageCode == 1) {
                            mUri1 = uri;
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            Log.e(TAG, mUri1.toString());
                            imageFront.setImageBitmap(bitmap);
                        } else if (mStudentActiveController.imageCode == 2) {
                            mUri2 = uri;
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            Log.e(TAG, mUri1.toString());
                            imageBack.setImageBitmap(bitmap);
                        }
                        try {
                            Glide.with(StudentActiveActivity.this)
                                    .asBitmap()
                                    .load(uri)
                                    .apply(new RequestOptions()
                                            .override(1024, 1024))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            Log.e(TAG, "Error transform");
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_active_request_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mStudentActiveController = new StudentActiveController(imageFront,imageBack,token,strFront,strBack,studentID,check,StudentActiveActivity.this);
        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentActiveController.imageCode = 1;
                onClickRequestPermission();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentActiveController.imageCode = 2;
                onClickRequestPermission();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                if (mUri1 != null) {
                    mStudentActiveController.minimizeUri(mUri1);
                    mUri1 = mStudentActiveController.uriFinal;
                    mStudentActiveController.uploadPicture(mUri1, "Front");
                }
                if (mUri2 != null) {
                    mStudentActiveController.minimizeUri(mUri2);
                    mUri2 = mStudentActiveController.uriFinal;
                    mStudentActiveController.uploadPicture(mUri2, "Back");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void initView(){
        imgFront = findViewById(R.id.imgFront);
        imgBack = findViewById(R.id.imgBack);
        imageFront = findViewById(R.id.picture);
        imageBack = findViewById(R.id.picture1);
        btnOK = findViewById(R.id.ok);    }
    private void getDataFromIntent(){
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");

    }
    public void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                String[] permission = {Manifest.permission.READ_MEDIA_IMAGES};
                requestPermissions(permission, MY_REQUEST_CODE);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, MY_REQUEST_CODE);
                }
            } else {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, MY_REQUEST_CODE);
            }
        }
    }




    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select PictureAdapter"));
    }
}
