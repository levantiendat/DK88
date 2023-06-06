package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

import com.example.dk88.Controller.AdminDashboardController;
import com.example.dk88.Model.Admin;
import com.example.dk88.R;

public class AdminDashboardActivity extends AppCompatActivity {
    private CardView cvUsers, cvRequest, cvProfile, cvLogout;
    private String token;
    private Admin admin;
    private AdminDashboardController mAdminDashboardController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mAdminDashboardController=new AdminDashboardController(cvUsers,cvRequest,cvProfile,cvLogout,token,admin,AdminDashboardActivity.this);
        // Xử lý sự kiện khi nhấn nút "Logout"
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.logout();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Profile"
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.goToAdminProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Request"
        cvRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.goToAdminRequest();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Users"
        cvUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement the logic for handling the "Users" button click
            }
        });
    }

    // Nhận dữ liệu từ Intent
    private void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        admin = (Admin) getIntent().getSerializableExtra("admin");
    }

    // Khởi tạo các view
    private void initView() {
        cvUsers = findViewById(R.id.userManagement);
        cvRequest = findViewById(R.id.requestManagement);
        cvProfile = findViewById(R.id.adminProfile);
        cvLogout = findViewById(R.id.logout);
    }


}
