package com.example.dk88.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.dk88.View.StudentActiveActivity;

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

public class StudentActiveController {
    private static final int MY_REQUEST_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private ImageView imageFront, imageBack;
    public int imageCode = 0;
    private String token;

    public Uri uriFinal;
    private String strFront = "", strBack = "";
    private String studentID;
    private int check = 0;
    private AppCompatActivity activity;
    private static final String TAG = StudentActiveActivity.class.getName();

    public StudentActiveController(ImageView imageFront, ImageView imageBack,  String token,   String strFront, String strBack, String studentID, int check, AppCompatActivity activity) {
        this.imageFront = imageFront;
        this.imageBack = imageBack;

        this.token = token;


        this.strFront = strFront;
        this.strBack = strBack;
        this.studentID = studentID;
        this.check = check;
        this.activity = activity;
    }


    public void minimizeUri(Uri uri) {
        try {
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int quality = 100;
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            while (outputStream.toByteArray().length > 1024 * 1024 && quality > 0) {
                outputStream.reset();
                quality -= 5;
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            }

            File outputDir = activity.getApplicationContext().getCacheDir();
            File outputFile = File.createTempFile("compressed_image", ".jpg", outputDir);

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(outputStream.toByteArray());
            fileOutputStream.close();

            uriFinal = Uri.fromFile(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadPicture(Uri uri, String text) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        String strRealPath = RealPathUtil.getRealPath(activity, uri);
        File file = new File(strRealPath);

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part picture = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().uploadPicture(headers, picture);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error picture1", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (text.compareTo("Front") == 0) {
                    strFront = tmp.getData().toString();
                } else {
                    strBack = tmp.getData().toString();
                }
                check++;
                Toast.makeText(activity, "Upload success in " + text + " picture", Toast.LENGTH_LONG).show();
                if (check == 2) {
                    sendActive();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error_picture", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(activity, "Error_upload1", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Your request to activate the account is successful, please wait for admin to activate", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, SignInActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error_UPLOAD", Toast.LENGTH_LONG).show();
            }
        });
    }


}
