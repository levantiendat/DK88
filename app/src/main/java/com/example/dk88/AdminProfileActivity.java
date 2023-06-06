package com.example.dk88;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_layout);

        // Nhận dữ liệu từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến AdminDashboardActivity
                goToAdminDashboard();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra và thực hiện các thay đổi thông tin
                checkAndPerformChanges();
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

    // Chuyển đến AdminDashboardActivity
    private void goToAdminDashboard() {
        Intent intent = new Intent(AdminProfileActivity.this, AdminDashboardActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    // Kiểm tra và thực hiện các thay đổi thông tin
    private void checkAndPerformChanges() {
        if (edtOld.getText().toString().isEmpty() && edtName.getText().toString().equals(admin.getName())
                && edtEmail.getText().toString().equals(admin.getEmail()) && edtPhone.getText().toString().equals(admin.getPhoneNumber())) {
            Toast.makeText(AdminProfileActivity.this, "Nothing change information", Toast.LENGTH_LONG).show();
        }else{
            if (!edtOld.getText().toString().isEmpty()) {
                if (edtOld.getText().toString().equals(edtNew.getText().toString())) {
                    Toast.makeText(AdminProfileActivity.this, "The new password is duplicated than old password", Toast.LENGTH_LONG).show();
                } else {
                    // Thay đổi mật khẩu
                    changePassword();
                }
            }
            if (!edtName.getText().toString().equals(admin.getName())
                    || !edtEmail.getText().toString().equals(admin.getEmail())
                    || !edtPhone.getText().toString().equals(admin.getPhoneNumber())) {
                // Thay đổi thông tin
                changeProfile();
            } else {
                Toast.makeText(AdminProfileActivity.this, "Nothing change information", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Thay đổi mật khẩu
    private void changePassword() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Map<String, Object> passInfo = new HashMap<>();
        passInfo.put("userName", admin.getUserName());
        passInfo.put("oldHashPass", edtOld.getText().toString().trim());
        passInfo.put("newHashPass", edtNew.getText().toString().trim());

        Call<ResponseObject> call1 = ApiUserRequester.getJsonPlaceHolderApi().changePass(headers, passInfo);
        call1.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String userRole = response.headers().get("UserRole");
                Toast.makeText(AdminProfileActivity.this, "Change Password successfully ", Toast.LENGTH_LONG).show();
                goToAdminDashboard();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Thay đổi thông tin
    private void changeProfile() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Map<String, Object> changeInfo = new HashMap<>();
        changeInfo.put("userName", admin.getUserName());
        changeInfo.put("name", edtName.getText().toString());
        changeInfo.put("phoneNumber", edtPhone.getText().toString());
        changeInfo.put("email", edtEmail.getText().toString());
        changeInfo.put("roleCode", admin.getRoleCode());

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers, changeInfo);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String userRole = response.headers().get("UserRole");
                Toast.makeText(AdminProfileActivity.this, "Change Data successfully ", Toast.LENGTH_LONG).show();
                goToAdminDashboard();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goToAdminDashboard();
    }
}
