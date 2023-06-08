package com.example.dk88.Controller;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.View.AdminRequestActivity;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Picture;
import com.example.dk88.Model.PictureAdapter;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBanRequestDetailController {
    EditText edtTarget, edtDetail;
    String token;
    Request request;

    ArrayList<Picture> arrayPicture;
    ListView listPicture;
    PictureAdapter adapter;
    Button btnAccept, btnDecline;
    int check = 0;

    private AppCompatActivity activity;

    public AdminBanRequestDetailController(EditText edtTarget, EditText edtDetail, String token, Request request, ArrayList<Picture> arrayPicture, ListView listPicture, PictureAdapter adapter, Button btnAccept, Button btnDecline, AppCompatActivity activity) {
        this.edtTarget = edtTarget;
        this.edtDetail = edtDetail;
        this.token = token;
        this.request = request;
        this.arrayPicture = arrayPicture;
        this.listPicture = listPicture;
        this.adapter = adapter;
        this.btnAccept = btnAccept;
        this.btnDecline = btnDecline;
        this.activity = activity;
    }
    public void getData() {
        Map<String, Object> header = new HashMap<>();
        header.put("token", token);

        Map<String, Object> detail = new HashMap<>();
        detail.put("requestID", request.getRequestID());
        detail.put("targetID", request.getTargetID());
        detail.put("requestCode", request.getRequestCode());

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readDetailRequest(header, detail);
        call.enqueue(new Callback<ResponseObject>() {
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String moreDetail = data.get("moreDetail").toString();
                edtDetail.setText(moreDetail);
                List<String> listUrl = (List<String>) data.get("imageProof");

                for (String url : listUrl) {
                    Log.e(TAG, url);
                    loadImage(url);
                    Toast.makeText(activity, url, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Tải ảnh từ server
    private void loadImage(String url) {
        Map<String, Object> header = new HashMap<>();
        header.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readImage(header, url);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                    return;
                }


                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                String base64 = tmp.getData().toString();
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                arrayPicture.add(new Picture(decodedByte));
                check++;

                if (check == arrayPicture.size()) {
                    showPicture();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Hiển thị danh sách ảnh
    private void showPicture() {
        adapter.notifyDataSetChanged();
    }

    // Xử lý yêu cầu chấp nhận hoặc từ chối
    public void isAccept(Boolean accept) {
        Map<String, Object> header = new HashMap<>();
        header.put("token", token);

        Map<String, Object> handle = new HashMap<>();
        handle.put("requestID", request.getRequestID());
        handle.put("targetID", request.getTargetID());
        handle.put("requestCode", request.getRequestCode());
        handle.put("isAccepted", accept);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().handleRequest(header, handle);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (accept) {
                    Toast.makeText(activity, "BAN SUCCESSFULLY", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "DECLINED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(activity, AdminRequestActivity.class);
                intent.putExtra("token", token);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
