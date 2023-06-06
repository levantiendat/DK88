package com.example.dk88;

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

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.View.StudentDashboardActivity;

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
    private Student student;
    private TextView getAdmin;
    private String userName;

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
                showAdminInformationPopup();
            }
        });

        // Trở về màn hình Dashboard của sinh viên
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToStudentDashboard();
            }
        });

        // Xử lý sự kiện khi người dùng click nút OK
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay đổi mật khẩu
                changePassword();

                // Thay đổi thông tin sinh viên
                changeStudentInfo();
            }
        });
    }

    @Override
    public void onBackPressed() {
        navigateToStudentDashboard();
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
        loadDataFromServer(studentID);
    }

    // Hiển thị popup thông tin admin
    private void showAdminInformationPopup() {
        Context context = StudentProfileActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.admin_information_dialog, null);

        // Xây dựng AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        // Thiết lập kích thước cho dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.9);
        layoutParams.width = width;
        layoutParams.height = height;
        dialog.getWindow().setAttributes(layoutParams);

        // Hiển thị dialog dưới dạng dialog
        dialog.show();
    }

    // Trở về màn hình Dashboard của sinh viên
    private void navigateToStudentDashboard() {
        Intent intent = new Intent(StudentProfileActivity.this, StudentDashboardActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        // Bắt đầu Activity tiếp theo với hiệu ứng chuyển động
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    // Thay đổi mật khẩu
    private void changePassword() {
        if (!edtOld.getText().toString().isEmpty()) {
            if (edtOld.getText().toString().equals(edtNew.getText().toString())) {
                Toast.makeText(StudentProfileActivity.this, "The new password is duplicated than old password", Toast.LENGTH_LONG).show();
            } else {
                Map<String, Object> headers = new HashMap<>();
                headers.put("token", token);
                Map<String, Object> passInfo = new HashMap<>();
                passInfo.put("userName", userName);
                passInfo.put("oldHashPass", edtOld.getText().toString().trim());
                passInfo.put("newHashPass", edtNew.getText().toString().trim());

                Call<ResponseObject> call1 = ApiUserRequester.getJsonPlaceHolderApi().changePass(headers, passInfo);
                call1.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(StudentProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ResponseObject tmp = response.body();
                        if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                            Toast.makeText(StudentProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(StudentProfileActivity.this, "Change Password successfully ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(StudentProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    // Thay đổi thông tin sinh viên
    private void changeStudentInfo() {
        if (!(edtName.getText().toString().equals(student.getName())
                && (edtPhone.getText().toString().equals(student.getPhoneNumber())))) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);
            Map<String, Object> changeInfo = new HashMap<>();
            changeInfo.put("userName", userName);
            changeInfo.put("name", edtName.getText().toString());
            changeInfo.put("phoneNumber", edtPhone.getText().toString());
            changeInfo.put("facebook", edtFacebook.getText().toString());
            changeInfo.put("roleCode", student.getRoleCode());

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers, changeInfo);
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(StudentProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                        Toast.makeText(StudentProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(StudentProfileActivity.this, "Change Data successfully ", Toast.LENGTH_LONG).show();

                    navigateToStudentDashboard();
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toast.makeText(StudentProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(StudentProfileActivity.this, "Nothing change information", Toast.LENGTH_LONG).show();
        }
    }

    // Load dữ liệu sinh viên từ server
    private void loadDataFromServer(String studentID) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(StudentProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                student = new Student();
                student.setRoleCode(1);
                student.setName(data.get("name").toString());
                student.setPhoneNumber(data.get("phoneNumber").toString());
                student.setUserName(userName);
                student.setStatus(1);
                student.setStudentID(data.get("studentID").toString());
                student.setFacebook(data.get("facebook").toString());

                edtName.setText(student.getName());
                edtPhone.setText(student.getPhoneNumber());
                edtFacebook.setText(student.getFacebook());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(StudentProfileActivity.this, "Error load data", Toast.LENGTH_LONG).show();
            }
        });
    }
}

