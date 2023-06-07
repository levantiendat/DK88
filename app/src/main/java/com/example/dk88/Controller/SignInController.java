package com.example.dk88.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.dk88.View.AdminDashboardActivity;
import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.Model.User;
import com.example.dk88.R;
import com.example.dk88.View.SignUpActivity;
import com.example.dk88.View.StudentActiveActivity;
import com.example.dk88.View.StudentBanStatusActivity;
import com.example.dk88.View.StudentDashboardActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInController {
    private Button btnSignin, btnSignup;
    private EditText edtUser, edtPass;
    private ImageView btnEye;
    private CheckBox cbRemember;
    private TextView showAdmin;
    private boolean passwordVisible = false;
    private SharedPreferences mPrefs;
    private AppCompatActivity activity;
    private boolean checkSignin=false;
    private static final String PREFS_NAME = "PASSWORD_PREFS_NAME";

    public SignInController(Button btnSignin, Button btnSignup, EditText edtUser, EditText edtPass, ImageView btnEye, CheckBox cbRemember, TextView showAdmin, boolean passwordVisible, SharedPreferences mPrefs, AppCompatActivity activity) {
        this.btnSignin = btnSignin;
        this.btnSignup = btnSignup;
        this.edtUser = edtUser;
        this.edtPass = edtPass;
        this.btnEye = btnEye;
        this.cbRemember = cbRemember;
        this.showAdmin = showAdmin;
        this.passwordVisible = passwordVisible;
        this.mPrefs = mPrefs;
        this.activity = activity;
    }


    // Xử lý sự kiện click button "Sign In"
    public void performSignIn() {
        String username = edtUser.getText().toString();
        String password = edtPass.getText().toString();

        // Kiểm tra dữ liệu đăng nhập
        if (isValid(username, password)) {
            // Gửi yêu cầu đăng nhập
            login(username, password);
        } else {
            Toast.makeText(activity, "Invalid username or password", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                String token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (cbRemember.isChecked()){
                    Boolean boolIsChecked = cbRemember.isChecked();
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("pref_name",edtUser.getText().toString());
                    editor.putString("pref_pass",edtPass.getText().toString());
                    editor.putBoolean("pref_check",boolIsChecked);
                    editor.apply();
                    Toast.makeText(activity, "Your account have been saved",Toast.LENGTH_LONG).show();
                }else{
                    mPrefs.edit().clear().apply();
                }
                handleLoginSuccess(tmp, token);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
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

        Toast.makeText(activity, "Login success as " + data.get("name"), Toast.LENGTH_LONG).show();
    }

    // Chuyển đến màn hình đăng ký
    public void navigateToSignUp() {
        Intent intent = new Intent(activity, SignUpActivity.class);
        activity.startActivity(intent);
    }

    // Chuyển đến màn hình Admin Dashboard
    private void navigateToAdminDashboard(String token, Admin admin) {
        Intent intent = new Intent(activity, AdminDashboardActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);
        activity.startActivity(intent);
        clearInputFields();
    }

    // Chuyển đến màn hình Student Active
    private void navigateToStudentActive(String token, Student student) {
        Intent intent = new Intent(activity, StudentActiveActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", student.getStudentID());
        activity.startActivity(intent);
        clearInputFields();
    }

    // Chuyển đến màn hình Student Ban Status
    private void navigateToStudentBanStatus() {
        Intent intent = new Intent(activity, StudentBanStatusActivity.class);
        activity.startActivity(intent);
        clearInputFields();
    }

    // Chuyển đến màn hình Student Dashboard
    private void navigateToStudentDashboard(String token, Student student) {
        Intent intent = new Intent(activity, StudentDashboardActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", student.getStudentID());
        intent.putExtra("userName", student.getUserName());
        activity.startActivity(intent);
        clearInputFields();
    }

    // Hiển thị dialog thông tin Admin
    public void showAdminInformationPopup() {
        Context context = activity;
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




    // Xóa nội dung trường nhập liệu
    private void clearInputFields() {
        edtUser.setText("");
        edtPass.setText("");
    }
}
