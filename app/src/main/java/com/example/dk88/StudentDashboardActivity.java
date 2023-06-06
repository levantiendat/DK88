package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dk88.View.SignInActivity;

public class StudentDashboardActivity extends AppCompatActivity {
    private CardView cvTrade, cvProfile, cvLogout;
    private String token;
    private String studentID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard_layout);

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
                goToStudentProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Trade"
        cvTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAvailableGroups();
            }
        });
    }

    // Nhận dữ liệu từ Intent
    private void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");
        userName = getIntent().getStringExtra("userName");
    }

    // Khởi tạo các view
    private void initView() {
        cvTrade = findViewById(R.id.tradeCourse);
        cvProfile = findViewById(R.id.userProfile);
        cvLogout = findViewById(R.id.logout);
    }

    // Xử lý đăng xuất
    private void logout() {
        Intent intent = new Intent(StudentDashboardActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    // Chuyển đến màn hình Student Profile
    private void goToStudentProfile() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
    }

    // Chuyển đến màn hình Available Groups
    private void goToAvailableGroups() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentAvailableGroupActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
    }
}
