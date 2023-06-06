package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

import com.example.dk88.Controller.StudentDashboardController;
import com.example.dk88.R;

public class StudentDashboardActivity extends AppCompatActivity {
    private CardView cvTrade, cvProfile, cvLogout;
    private String token;
    private String studentID;
    private String userName;
    private StudentDashboardController mStudentDashboardController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mStudentDashboardController=new StudentDashboardController(cvTrade, cvProfile, cvLogout, token, studentID, userName, StudentDashboardActivity.this);

        // Xử lý sự kiện khi nhấn nút "Logout"
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.logout();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Profile"
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.goToStudentProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Trade"
        cvTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.goToAvailableGroups();
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


}
