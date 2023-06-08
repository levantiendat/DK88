package com.example.dk88.Controller;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.SignInActivity;
import com.example.dk88.View.SignUpActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpController {
    private Button btnOk;
    private TextView back;

    private boolean passwordVisible = false;
    private EditText edtFullName, edtPhone, edtID, edtUser, edtPass, edtFacebook, editPassConfirm;

    private AppCompatActivity activity;

    public SignUpController(Button btnOk, TextView back, boolean passwordVisible, EditText edtFullName, EditText edtPhone, EditText edtID, EditText edtUser, EditText edtPass, EditText edtFacebook, EditText editPassConfirm, AppCompatActivity activity) {
        this.btnOk = btnOk;
        this.back = back;
        this.passwordVisible = passwordVisible;
        this.edtFullName = edtFullName;
        this.edtPhone = edtPhone;
        this.edtID = edtID;
        this.edtUser = edtUser;
        this.edtPass = edtPass;
        this.edtFacebook = edtFacebook;
        this.editPassConfirm = editPassConfirm;
        this.activity = activity;
    }

    // Xử lý việc đăng ký người dùng
    public void registerUser() {
        String userName = edtUser.getText().toString();
        String password = edtPass.getText().toString();
        String confirmPassword = editPassConfirm.getText().toString();
        String studentID = edtID.getText().toString();
        String fullName = edtFullName.getText().toString();
        String phoneNumber = edtPhone.getText().toString();
        String facebook = edtFacebook.getText().toString();

        // Kiểm tra xem mật khẩu và mật khẩu xác nhận có khớp hay không
        if (!password.equals(confirmPassword)) {
            Toast.makeText(activity, "Your password does not match the confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng lưu thông tin đăng ký
        Map<String, Object> registerInfo = new HashMap<>();
        registerInfo.put("userName", userName);
        registerInfo.put("hashPass", password);
        registerInfo.put("studentID", studentID);
        registerInfo.put("name", fullName);
        registerInfo.put("phoneNumber", phoneNumber);
        registerInfo.put("facebook", facebook);

        // Gửi yêu cầu đăng ký đến API
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().signup(registerInfo);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String userRole = response.headers().get("UserRole");
                Toast.makeText(activity, "Register success as " + data.get("name"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Chuyển đến màn hình đăng nhập
    public void goToSignInActivity() {
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
    }
}
