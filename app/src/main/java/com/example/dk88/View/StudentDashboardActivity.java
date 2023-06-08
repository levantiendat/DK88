package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dk88.Controller.StudentDashboardController;
import com.example.dk88.R;

public class StudentDashboardActivity extends AppCompatActivity {
    private String token;
    private String studentID;
    private String userName;
    private Button btnTradeProfile,btnProfile,btnMyGroup,btnTradeCourse;
    private StudentDashboardController mStudentDashboardController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_menu_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mStudentDashboardController=new StudentDashboardController( token, studentID, userName, StudentDashboardActivity.this);

        // Xử lý sự kiện khi nhấn nút "My Account"
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.showMyProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Trade Profile"
        btnTradeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.showMyTradeProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "My Group"
        btnMyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.checkInGroup();
                if (mStudentDashboardController.getMyGroup()!=null) {
                    mStudentDashboardController.showMyAvailableGroup();
                }else{
                    Toast.makeText(StudentDashboardActivity.this, "You are currently not in the group!"+mStudentDashboardController.getMyGroup(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút "Trade Course"
        btnTradeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.checkInGroup();
                if (mStudentDashboardController.getMyGroup()==null) {
                    mStudentDashboardController.showMyAvailableGroup();
                }else{
                    Toast.makeText(StudentDashboardActivity.this, "You are already in a group!"+mStudentDashboardController.getMyGroup(), Toast.LENGTH_LONG).show();
                }
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
        btnProfile = findViewById(R.id.myAccount);
        btnTradeProfile = findViewById(R.id.tradeProfile);
        btnMyGroup = findViewById(R.id.myCourse);
        btnTradeCourse = findViewById(R.id.tradeCourse);
    }


}
