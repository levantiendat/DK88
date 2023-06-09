package com.example.dk88.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiRequester;
import com.example.dk88.Model.RealPathUtil;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.SignInActivity;
import com.example.dk88.View.StudentActiveActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().uploadPicture(headers, picture);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error picture1", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (text.compareTo("Front") == 0) {
                    strFront = tmp.getData().toString();
                } else {
                    strBack = tmp.getData().toString();
                }
                check++;
                Toast.makeText(activity, "Upload success in " + text + " picture", Toast.LENGTH_SHORT).show();
                if (check == 2) {
                    sendActive();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error_picture", Toast.LENGTH_SHORT).show();
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

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().sendActiveRequest(headers, activeInfo);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error_upload1", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(activity, "Your request to activate the account is successful, please wait for admin to activate", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, SignInActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error_UPLOAD", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
