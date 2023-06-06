package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dk88.Controller.AdminActiveRequestDetailController;
import com.example.dk88.Model.Request;
import com.example.dk88.R;

public class AdminActiveRequestDetailActivity extends AppCompatActivity {
    private TextView edtStudentId;
    private ImageView imgFront, imgBack;
    private Button btnAccept, btnDecline;
    private String token;
    private Request request;
    private AdminActiveRequestDetailController mAdminActiveRequestDetailController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_active_request_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mAdminActiveRequestDetailController =new AdminActiveRequestDetailController(edtStudentId,imgFront,imgBack,btnAccept,btnDecline,token,request,AdminActiveRequestDetailActivity.this);
        // Lấy dữ liệu từ server
        mAdminActiveRequestDetailController.getData();

        // Xử lý sự kiện khi nhấn nút "Accept"
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminActiveRequestDetailController.isAccept(true);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Decline"
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminActiveRequestDetailController.isAccept(false);
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


}
