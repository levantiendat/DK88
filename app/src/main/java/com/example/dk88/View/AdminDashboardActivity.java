package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dk88.Controller.AdminDashboardController;
import com.example.dk88.Model.Admin;
import com.example.dk88.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private String token;
    private Admin admin;
    private Button btnUser,btnRequest,btnProfile;
    private TextView btnLogout;
    private AdminDashboardController mAdminDashboardController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mAdminDashboardController=new AdminDashboardController(token,admin,AdminDashboardActivity.this);
        // Xử lý sự kiện khi nhấn nút "Logout"
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.logout();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Profile"
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.goToAdminProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Request"
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.goToAdminRequest();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Users"
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminDashboardController.goToAdminUserManagementActivity();
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
        btnUser = (Button)findViewById(R.id.userManagement);
        btnRequest = (Button)findViewById(R.id.requestManagement);
        btnProfile = (Button)findViewById(R.id.adminProfile);
        btnLogout = (TextView) findViewById(R.id.logout);
    }


}
