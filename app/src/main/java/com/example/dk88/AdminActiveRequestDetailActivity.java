package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActiveRequestDetailActivity extends AppCompatActivity {
    private TextView edtStudentId;
    private ImageView imgFront, imgBack;
    private Button btnAccept, btnDecline;
    private String token;
    private Request request;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_active_request_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        // Lấy dữ liệu từ server
        getData();

        // Xử lý sự kiện khi nhấn nút "Accept"
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAccept(true);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Decline"
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAccept(false);
            }
        });
    }

    // Nhận dữ liệu từ Intent
    private void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        request = (Request) getIntent().getSerializableExtra("request");
    }

    // Khởi tạo các view
    private void initView() {
        edtStudentId = findViewById(R.id.studentId);
        imgFront = findViewById(R.id.imgFront);
        imgBack = findViewById(R.id.imgBack);
        btnAccept = findViewById(R.id.accept);
        btnDecline = findViewById(R.id.decline);
    }

    // Lấy dữ liệu từ server
    private void getData() {
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
                    Toast.makeText(AdminActiveRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminActiveRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(AdminActiveRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AdminActiveRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.headers().get("token");
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminActiveRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                String base64 = tmp.getData().toString();
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgTemp.setImageBitmap(decodedByte);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminActiveRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Xác nhận chấp nhận hoặc từ chối
    private void isAccept(Boolean accept) {
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
                    Toast.makeText(AdminActiveRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.headers().get("token");
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminActiveRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (accept) {
                    Toast.makeText(AdminActiveRequestDetailActivity.this, "ACTIVE SUCCESSFULLY", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminActiveRequestDetailActivity.this, "DECLINED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(AdminActiveRequestDetailActivity.this, AdminRequestActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminActiveRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
