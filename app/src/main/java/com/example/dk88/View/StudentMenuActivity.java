package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Controller.StudentDashboardController;
import com.example.dk88.R;

public class StudentMenuActivity extends AppCompatActivity {
    private String token;
    private String studentID;
    private String userName;
    private Button btnTradeProfile,btnProfile,btnMyGroup,btnTradeCourse;
    private TextView btnLogout;
    private StudentDashboardController mStudentDashboardController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_menu_layout);



        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mStudentDashboardController=new StudentDashboardController( token, studentID, userName, StudentMenuActivity.this);

        // Kiểm tra xem myGroup lần đầu
        mStudentDashboardController.checkInGroup();

        // Xử lý sự kiện khi nhấn nút "My Account"
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.showMyProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Trade Profile"
//        btnTradeProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mStudentDashboardController.showMyTradeProfile();
//            }
//        });

        // Xử lý sự kiện khi nhấn nút "My Group"
        btnMyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.checkInGroup();
                if (mStudentDashboardController.getMyGroup()!=null) {
                    mStudentDashboardController.showMyGroup();
                }else{
                    Toast.makeText(StudentMenuActivity.this, "You are currently not in the group!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút "Trade Course"
        btnTradeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.showMyAvailableGroup();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentDashboardController.logout();
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
//        btnTradeProfile = findViewById(R.id.tradeProfile);
        btnMyGroup = findViewById(R.id.myCourse);
        btnTradeCourse = findViewById(R.id.tradeCourse);
        btnLogout =  findViewById(R.id.signOut);
    }


}
