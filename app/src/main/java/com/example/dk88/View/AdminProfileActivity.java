package com.example.dk88.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.AdminProfileController;
import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileActivity extends AppCompatActivity {
    EditText edtOld, edtNew, edtName, edtPhone, edtEmail;
    Button btnOK, btnBack;
    String token;
    Admin admin;
    private AdminProfileController mAdminProfileController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mAdminProfileController=new AdminProfileController(edtOld,edtNew,edtName,edtPhone,edtEmail,btnOK,btnBack,token,admin,AdminProfileActivity.this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến AdminDashboardActivity
                mAdminProfileController.goToAdminDashboard();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra và thực hiện các thay đổi thông tin
                mAdminProfileController.checkAndPerformChanges();
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
        edtOld = findViewById(R.id.Password);
        edtNew = findViewById(R.id.Password1);
        edtName = findViewById(R.id.fullname);
        edtPhone = findViewById(R.id.phone);
        edtEmail = findViewById(R.id.email);
        btnOK = findViewById(R.id.ok);
        btnBack = findViewById(R.id.back);
        edtName.setText(admin.getName());
        edtPhone.setText(admin.getPhoneNumber());
        edtEmail.setText(admin.getEmail());
    }



    @Override
    public void onBackPressed() {
        mAdminProfileController.goToAdminDashboard();
    }
}
