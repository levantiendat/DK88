package com.example.dk88;

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
import android.widget.Toast;

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
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.RealPathUtil;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.SignInActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentActiveActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private ImageButton imgFront, imgBack;
    private ImageView imageFront, imageBack;
    private int imageCode = 0;
    private String token;
    private Uri mUri1, mUri2, uriFinal;
    private Button btnOK;
    private String strFront = "", strBack = "";
    private String studentID;
    private int check = 0;
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
                        if (imageCode == 1) {
                            mUri1 = uri;
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            Log.e(TAG, mUri1.toString());
                            imageFront.setImageBitmap(bitmap);
                        } else if (imageCode == 2) {
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

        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCode = 1;
                onClickRequestPermission();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCode = 2;
                onClickRequestPermission();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                if (mUri1 != null) {
                    minimizeUri(mUri1);
                    mUri1 = uriFinal;
                    uploadPicture(mUri1, "Front");
                }
                if (mUri2 != null) {
                    minimizeUri(mUri2);
                    mUri2 = uriFinal;
                    uploadPicture(mUri2, "Back");
                }
            }
        });
    }

    private void minimizeUri(Uri uri) {
        try {
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int quality = 100;
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            while (outputStream.toByteArray().length > 1024 * 1024 && quality > 0) {
                outputStream.reset();
                quality -= 5;
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            }

            File outputDir = getApplicationContext().getCacheDir();
            File outputFile = File.createTempFile("compressed_image", ".jpg", outputDir);

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(outputStream.toByteArray());
            fileOutputStream.close();

            uriFinal = Uri.fromFile(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadPicture(Uri uri, String text) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        String strRealPath = RealPathUtil.getRealPath(this, uri);
        File file = new File(strRealPath);

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part picture = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().uploadPicture(headers, picture);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(StudentActiveActivity.this, "Error picture1", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentActiveActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (text.compareTo("Front") == 0) {
                    strFront = tmp.getData().toString();
                } else {
                    strBack = tmp.getData().toString();
                }
                check++;
                Toast.makeText(StudentActiveActivity.this, "Upload success in " + text + " picture", Toast.LENGTH_LONG).show();
                if (check == 2) {
                    sendActive();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(StudentActiveActivity.this, "Error_picture", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendActive() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Map<String, Object> activeInfo = new HashMap<>();
        activeInfo.put("requestID", 1);
        activeInfo.put("targetID", studentID);
        activeInfo.put("requestCode", 0);
        activeInfo.put("imageFront", strFront);
        activeInfo.put("imageBack", strBack);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().sendActiveRequest(headers, activeInfo);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(StudentActiveActivity.this, "Error_upload1", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentActiveActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(StudentActiveActivity.this, "Your request to activate the account is successful, please wait for admin to activate", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentActiveActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(StudentActiveActivity.this, "Error_UPLOAD", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onClickRequestPermission() {
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
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select PictureAdapter"));
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
}
