package com.example.dk88.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.View.AdminRequestActivity;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActiveRequestDetailController {
    private TextView edtStudentId;
    private ImageView imgFront, imgBack;
    private Button btnAccept, btnDecline;
    private String token;
    private Request request;
    private AppCompatActivity activity;

    public AdminActiveRequestDetailController(TextView edtStudentId, ImageView imgFront, ImageView imgBack, Button btnAccept, Button btnDecline, String token, Request request, AppCompatActivity activity) {
        this.edtStudentId = edtStudentId;
        this.imgFront = imgFront;
        this.imgBack = imgBack;
        this.btnAccept = btnAccept;
        this.btnDecline = btnDecline;
        this.token = token;
        this.request = request;
        this.activity = activity;
    }
    // Lấy dữ liệu từ server
    public void getData() {
        Map<String, Object> header = new HashMap<>();
        header.put("token", token);

        Map<String, Object> detail = new HashMap<>();
        detail.put("requestID", request.getRequestID());
        detail.put("targetID", request.getTargetID());
        detail.put("requestCode", request.getRequestCode());

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readDetailRequest(header, detail);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResponseObject tmp = response.body();


                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String requestId = data.get("requestID").toString();
                String targetId = data.get("targetID").toString();
                edtStudentId.setText(edtStudentId.getText() + targetId);

                String urlFront = data.get("imageFront").toString();
                String urlBack = data.get("imageBack").toString();
                loadImage(urlFront, imgFront);
                loadImage(urlBack, imgBack);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tải ảnh từ server
    private void loadImage(String url, ImageView imgTemp) {
        Map<String, Object> header = new HashMap<>();
        header.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readImage(header, url);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }


                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String base64 = tmp.getData().toString();
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgTemp.setImageBitmap(decodedByte);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xác nhận chấp nhận hoặc từ chối
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
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }


                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (accept) {
                    Toast.makeText(activity, "ACTIVE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "DECLINED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(activity, AdminRequestActivity.class);
                intent.putExtra("token", token);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
