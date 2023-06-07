package com.example.dk88.Controller;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Picture;
import com.example.dk88.Model.PictureAdapter;
import com.example.dk88.Model.RealPathUtil;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;
import com.example.dk88.View.StudentReportActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class StudentReportController {
    private String token = "";
    private String studentID;
    private String userName;

    private EditText edtTarget, edtProblem;
    private ListView listPicture;
    private PictureAdapter adapter;
    private int check = 0;
    public Uri uriFinal = null;
    private String url = "";

    private static final int MY_REQUEST_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    public ArrayList<Picture> arrayPicture=new ArrayList<>();
    public ArrayList<Uri> uriPicture=new ArrayList<>();
    private ArrayList<String> strPicture=new ArrayList<>();

    private AppCompatActivity activity;

    public StudentReportController(String token, String studentID, String userName, EditText edtTarget, EditText edtProblem, ListView listPicture,  AppCompatActivity activity) {
        this.token = token;
        this.studentID = studentID;
        this.userName = userName;
        this.edtTarget = edtTarget;
        this.edtProblem = edtProblem;
        this.listPicture = listPicture;
        this.adapter = adapter;
        this.url = url;
        this.arrayPicture = arrayPicture;
        this.uriPicture = uriPicture;
        this.strPicture = strPicture;
        this.activity = activity;
    }

    public void getData() {
        adapter = new PictureAdapter(activity, R.layout.picture_layout, arrayPicture);
        listPicture.setAdapter(adapter);
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

    public void uploadPicture(Uri uri) {
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
                    Toast.makeText(activity, "Error uploading picture", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                url = tmp.getData().toString();
                check++;
                if (url.length() > 0) {
                    strPicture.add(url);
                }
                if (check == uriPicture.size()) {
                    sendBan();
                }
                Toast.makeText(activity, tmp.getData().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error uploading picture", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendBan() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Map<String, Object> banInfo = new HashMap<>();
        banInfo.put("requestID", 2);
        banInfo.put("targetID", edtTarget.getText().toString());
        banInfo.put("requestCode", 1);
        banInfo.put("moreDetail", edtProblem.getText().toString());
        banInfo.put("imageProof", strPicture);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().sendBanRequest(headers, banInfo);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error uploading", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Your request to ban the account is successful. Please wait for an admin to ban it.", Toast.LENGTH_LONG).show();
                Toast.makeText(activity, "You can swipe back.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error uploading", Toast.LENGTH_LONG).show();
            }
        });
    }

}
