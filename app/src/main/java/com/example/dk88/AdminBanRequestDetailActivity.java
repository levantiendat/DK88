package com.example.dk88;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

public class AdminBanRequestDetailActivity extends AppCompatActivity {
    EditText edtTarget, edtDetail;
    String token;
    Request request;

    ArrayList<Picture> arrayPicture;
    ListView listPicture;
    PictureAdapter adapter;
    Button btnAccept, btnDecline;
    int check = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_ban_request_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        // Lấy dữ liệu từ server
        getData();
    }

    // Nhận dữ liệu từ Intent
    private void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        request = (Request) getIntent().getSerializableExtra("request");
    }

    // Khởi tạo các view
    private void initView() {
        edtTarget = (EditText) findViewById(R.id.target);
        edtDetail = (EditText) findViewById(R.id.content);
        edtTarget.setText(edtTarget.getText() + request.getTargetID());

        listPicture = (ListView) findViewById(R.id.listView);
        btnAccept = (Button) findViewById(R.id.accept);
        btnDecline = (Button) findViewById(R.id.decline);

        arrayPicture = new ArrayList<>();
        adapter = new PictureAdapter(this, R.layout.picture_layout, arrayPicture);
        listPicture.setAdapter(adapter);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAccept(true);
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAccept(false);
            }
        });
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
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String moreDetail = data.get("moreDetail").toString();
                edtDetail.setText(moreDetail);
                List<String> listUrl = (List<String>) data.get("imageProof");

                for (String url : listUrl) {
                    Log.e(TAG, url);
                    loadImage(url);
                    Toast.makeText(AdminBanRequestDetailActivity.this, url, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.headers().get("token");

                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Hiển thị danh sách ảnh
    private void showPicture() {
        adapter.notifyDataSetChanged();
    }

    // Xử lý yêu cầu chấp nhận hoặc từ chối
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
                    Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.headers().get("token");
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (accept) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, "BAN SUCCESSFULLY", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminBanRequestDetailActivity.this, "DECLINED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(AdminBanRequestDetailActivity.this, AdminRequestActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
