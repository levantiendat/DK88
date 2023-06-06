package com.example.dk88.View;

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
import android.widget.ListView;
import android.widget.Toast;

import com.example.dk88.AdminRequestActivity;
import com.example.dk88.Controller.AdminBanRequestDetailController;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Picture;
import com.example.dk88.Model.PictureAdapter;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;

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
    private AdminBanRequestDetailController mAdminBanRequestDetailController;
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

        mAdminBanRequestDetailController = new AdminBanRequestDetailController(edtTarget,edtDetail,token,request,arrayPicture,listPicture,adapter,btnAccept,btnDecline,AdminBanRequestDetailActivity.this);

        // Lấy dữ liệu từ server
        mAdminBanRequestDetailController.getData();
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
                mAdminBanRequestDetailController.isAccept(true);
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdminBanRequestDetailController.isAccept(false);
            }
        });
    }



}
