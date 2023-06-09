package com.example.dk88.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.StudentProfileController;
import com.example.dk88.R;

public class StudentProfileActivity extends AppCompatActivity {
    volatile boolean isRunning = true;
    private EditText edtOld, edtNew, edtName, edtPhone, edtFacebook;
    private Button btnOK, btnBack;
    private TextView txtGetAdmin;
    private String token, studentID;

    private TextView getAdmin;
    private String userName;
    private StudentProfileController mStudentProfileController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        // Hiển thị thông tin của admin
        getAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentProfileController.showAdminInformationPopup();
            }
        });

        // Trở về màn hình Dashboard của sinh viên
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentProfileController.navigateToStudentDashboard();
            }
        });

        // Xử lý sự kiện khi người dùng click nút OK
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay đổi mật khẩu
                mStudentProfileController.changePassword();

                // Thay đổi thông tin sinh viên
                mStudentProfileController.changeStudentInfo();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mStudentProfileController.navigateToStudentDashboard();
    }

    // Nhận dữ liệu từ Intent
    private void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");
        userName = getIntent().getStringExtra("userName");
    }

    // Khởi tạo các view
    private void initView() {
        edtOld = findViewById(R.id.Password);
        edtNew = findViewById(R.id.Password1);
        edtName = findViewById(R.id.fullname);
        edtPhone = findViewById(R.id.phone);
        btnOK = findViewById(R.id.ok);
        btnBack = findViewById(R.id.back);
        txtGetAdmin = findViewById(R.id.getAdmin1);
        edtFacebook = findViewById(R.id.facebookLink);
        getAdmin = findViewById(R.id.getAdmin1);

    }

    class MainControlThread implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Tải dữ liệu sinh viên từ server
                        mStudentProfileController = new StudentProfileController(edtOld, edtNew, edtName, edtPhone, edtFacebook, btnOK, btnBack, txtGetAdmin, token, studentID, getAdmin, userName, StudentProfileActivity.this);
                        mStudentProfileController.loadDataFromServer(studentID);
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        Thread mainThread = new Thread(new MainControlThread());
        mainThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

}







