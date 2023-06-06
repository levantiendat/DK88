package com.example.dk88.View;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.StudentProfileController;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileActivity extends AppCompatActivity {
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

        // Tải dữ liệu sinh viên từ server
        mStudentProfileController = new StudentProfileController(edtOld, edtNew, edtName, edtPhone, edtFacebook, btnOK, btnBack, txtGetAdmin, token, studentID, getAdmin, userName, StudentProfileActivity.this);
        mStudentProfileController.loadDataFromServer(studentID);

    }


}

