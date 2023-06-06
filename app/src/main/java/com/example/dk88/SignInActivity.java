package com.example.dk88;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.Model.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private Button btnSignin, btnSignup;
    private EditText edtUser, edtPass;
    private ImageView btnEye;
    private CheckBox cbRemember;
    private TextView showAdmin;
    private boolean passwordVisible = false;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PASSWORD_PREFS_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        // Khởi tạo SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Ánh xạ các phần tử UI
        initView();

        // Lấy dữ liệu từ SharedPreferences và hiển thị lên giao diện
        getPreferencesData();

        // Xử lý sự kiện click button "Sign In"
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignIn();
            }
        });

        // Xử lý sự kiện click button "Sign Up"
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUp();
            }
        });

        // Xử lý sự kiện click icon "Eye"
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Xử lý sự kiện click text "Show Admin"
        showAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminInformationPopup();
            }
        });
    }

    // Ánh xạ các phần tử UI
    private void initView() {
        btnSignin = findViewById(R.id.signin1);
        edtUser = findViewById(R.id.Username);
        edtPass = findViewById(R.id.Password);
        btnSignup = findViewById(R.id.signup);
        cbRemember = findViewById(R.id.checkBox);
        btnEye = findViewById(R.id.eyeIcon);
        showAdmin = findViewById(R.id.show);
    }

    // Lấy dữ liệu từ SharedPreferences và hiển thị lên giao diện
    private void getPreferencesData() {
        if (mPrefs.contains("pref_name")) {
            String u = mPrefs.getString("pref_name", "not found");
            edtUser.setText(u);
        }
        if (mPrefs.contains("pref_pass")) {
            String p = mPrefs.getString("pref_pass", "not found");
            edtPass.setText(p);
        }
        if (mPrefs.contains("pref_check")) {
            boolean b = mPrefs.getBoolean("pref_check", false);
            cbRemember.setChecked(b);
        }
    }

    // Xử lý sự kiện click button "Sign In"
    private void performSignIn() {
        String username = edtUser.getText().toString();
        String password = edtPass.getText().toString();

        // Kiểm tra dữ liệu đăng nhập
        if (isValid(username, password)) {
            // Gửi yêu cầu đăng nhập
            login(username, password);
        } else {
            Toast.makeText(SignInActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }
    }

    // Kiểm tra dữ liệu đăng nhập
    private boolean isValid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    // Gửi yêu cầu đăng nhập
    private void login(String username, String password) {
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("userName", username);
        loginInfo.put("hashPass", password);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().login(loginInfo);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                String token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(SignInActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                handleLoginSuccess(tmp, token);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Xử lý khi đăng nhập thành công
    private void handleLoginSuccess(ResponseObject responseObject, String token) {
        Map<String, Object> data = (Map<String, Object>) responseObject.getData();
        Integer userRole = Math.toIntExact(Math.round(Double.parseDouble(data.get("roleCode").toString())));

        if (userRole.equals(User.ROLE_CODE_ADMIN)) {
            // Đăng nhập thành công với vai trò Admin
            Admin admin = new Admin();
            admin.setUserName(data.get("userName").toString());
            admin.setName(data.get("name").toString());
            admin.setEmail(data.get("email").toString());
            admin.setPhoneNumber(data.get("phoneNumber").toString());
            admin.setRoleCode(userRole);

            navigateToAdminDashboard(token, admin);
        } else {
            // Đăng nhập thành công với vai trò Student
            Student student = new Student();
            student.setRoleCode(userRole);
            student.setUserName(data.get("userName").toString());
            student.setName(data.get("name").toString());
            student.setPhoneNumber(data.get("phoneNumber").toString());
            student.setStatus(Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString()))));
            student.setStudentID(data.get("studentID").toString());
            student.setFacebook(data.get("facebook").toString());

            if (student.getStatus() == Student.STATUS_NEW_USER) {
                navigateToStudentActive(token, student);
            } else if (student.getStatus() == Student.STATUS_BAN_USER) {
                navigateToStudentBanStatus();
            } else {
                navigateToStudentDashboard(token, student);
            }
        }

        Toast.makeText(SignInActivity.this, "Login success as " + data.get("name"), Toast.LENGTH_LONG).show();
    }

    // Chuyển đến màn hình đăng ký
    private void navigateToSignUp() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    // Chuyển đến màn hình Admin Dashboard
    private void navigateToAdminDashboard(String token, Admin admin) {
        Intent intent = new Intent(SignInActivity.this, AdminDashboardActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);
        startActivity(intent);
        clearInputFields();
    }

    // Chuyển đến màn hình Student Active
    private void navigateToStudentActive(String token, Student student) {
        Intent intent = new Intent(SignInActivity.this, StudentActiveActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", student.getStudentID());
        startActivity(intent);
        clearInputFields();
    }

    // Chuyển đến màn hình Student Ban Status
    private void navigateToStudentBanStatus() {
        Intent intent = new Intent(SignInActivity.this, StudentBanStatusActivity.class);
        startActivity(intent);
        clearInputFields();
    }

    // Chuyển đến màn hình Student Dashboard
    private void navigateToStudentDashboard(String token, Student student) {
        Intent intent = new Intent(SignInActivity.this, StudentDashboardActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", student.getStudentID());
        intent.putExtra("userName", student.getUserName());
        startActivity(intent);
        clearInputFields();
    }

    // Hiển thị dialog thông tin Admin
    private void showAdminInformationPopup() {
        Context context = SignInActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.admin_information_dialog, null);

        // Xây dựng AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        // Tạo và hiển thị dialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alertDialog.show();
    }

    // Toggle hiển thị mật khẩu
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnEye.setImageResource(R.drawable.eye);
        } else {
            edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnEye.setImageResource(R.drawable.eye);
        }

        passwordVisible = !passwordVisible;
        edtPass.setSelection(edtPass.length());
    }

    // Xóa nội dung trường nhập liệu
    private void clearInputFields() {
        edtUser.setText("");
        edtPass.setText("");
    }
}
