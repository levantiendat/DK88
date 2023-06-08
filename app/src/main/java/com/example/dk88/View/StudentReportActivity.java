package com.example.dk88.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.StudentReportController;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Picture;
import com.example.dk88.Model.PictureAdapter;
import com.example.dk88.Model.RealPathUtil;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentReportActivity extends AppCompatActivity {

    private String token = "";
    private String studentID;
    private String userName;
    private Button btnSave, btnUpload;
    private EditText edtTarget, edtProblem;
    private ListView listPicture;
    private PictureAdapter adapter;
    private int check = 0;
    private Uri uriFinal = null;
    private String url = "";

    private static final int MY_REQUEST_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;


    private StudentReportController mStudentReportController;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            Toast.makeText(StudentReportActivity.this, "Upload file failed", Toast.LENGTH_SHORT);
                            return;
                        }
                        Uri uri = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            mStudentReportController.arrayPicture.add(new Picture(bitmap));
                            mStudentReportController.uriPicture.add(uri);
                            mStudentReportController.getData();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_report_layout);

        initView();
        getDataFromIntent();
        mStudentReportController=new StudentReportController(token,studentID,userName,edtTarget,edtProblem,listPicture,StudentReportActivity.this);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Uri uri : mStudentReportController.uriPicture) {
                    mStudentReportController.minimizeUri(uri);
                    if (mStudentReportController.uriFinal != null) {
                        mStudentReportController.uploadPicture(mStudentReportController.uriFinal);
                    }
                    mStudentReportController.uriFinal = null;
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentReportActivity.this, StudentTradeFinishActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        finish();
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                String[] permission = {android.Manifest.permission.READ_MEDIA_IMAGES};
                requestPermissions(permission, MY_REQUEST_CODE);
            }
        } else {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    String[] permission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, MY_REQUEST_CODE);
                }
            } else {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, MY_REQUEST_CODE);
            }
        }
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void initView(){
        btnSave = findViewById(R.id.save);
        btnUpload = findViewById(R.id.upload);
        edtTarget = findViewById(R.id.targetID);
        edtProblem = findViewById(R.id.reportProblem);
        listPicture = findViewById(R.id.listView);


    }

    private void getDataFromIntent(){
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");
        userName = getIntent().getStringExtra("userName");
    }


}

