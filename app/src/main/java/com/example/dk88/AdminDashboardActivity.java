package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dk88.Model.Admin;

public class AdminDashboardActivity extends AppCompatActivity {
    private CardView cvUsers, cvRequest, cvProfile, cvLogout;
    private String token;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        // Xử lý sự kiện khi nhấn nút "Logout"
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Profile"
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAdminProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Request"
        cvRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAdminRequest();
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

    // Xử lý đăng xuất
    private void logout() {
        Intent intent = new Intent(AdminDashboardActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    // Chuyển đến màn hình Admin Profile
    private void goToAdminProfile() {
        Intent intent = new Intent(AdminDashboardActivity.this, AdminProfileActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);
        startActivity(intent);
        finish();
    }

    // Chuyển đến màn hình Admin Request
    private void goToAdminRequest() {
        Intent intent = new Intent(AdminDashboardActivity.this, AdminRequestActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);
        startActivity(intent);
        finish();
    }
}
