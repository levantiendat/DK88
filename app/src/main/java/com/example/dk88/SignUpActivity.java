package com.example.dk88;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Button btnOk;
    private TextView back;

    private boolean passwordVisible = false;
    private EditText edtFullName, edtPhone, edtID, edtUser, edtPass, edtFacebook, editPassConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        // Khởi tạo các view
        initView();

        // Xử lý sự kiện khi nhấn nút "Ok"
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Back"
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignInActivity();
            }
        });

        // Xử lý sự kiện khi chạm vào biểu tượng mắt trong ô Password
        setEyeIconTouchListener(edtPass);
        setEyeIconTouchListener(editPassConfirm);
    }

    // Khởi tạo các view và icon mắt
    private void initView() {
        btnOk = findViewById(R.id.register);
        edtFullName = findViewById(R.id.fullname);
        edtPhone = findViewById(R.id.phone);
        edtID = findViewById(R.id.studentid);
        edtUser = findViewById(R.id.Username);
        edtPass = findViewById(R.id.Password);
        back = findViewById(R.id.back);
        edtFacebook = findViewById(R.id.facebookLink);
        editPassConfirm = findViewById(R.id.ConfirmPassword);

        Drawable eyeDrawable = getResources().getDrawable(R.drawable.eye);
        eyeDrawable.setBounds(0, 0, eyeDrawable.getIntrinsicWidth(), eyeDrawable.getIntrinsicHeight());
        edtPass.setCompoundDrawables(null, null, eyeDrawable, null);
        editPassConfirm.setCompoundDrawables(null, null, eyeDrawable, null);
    }

    // Xử lý việc đăng ký người dùng
    private void registerUser() {
        String userName = edtUser.getText().toString();
        String password = edtPass.getText().toString();
        String confirmPassword = editPassConfirm.getText().toString();
        String studentID = edtID.getText().toString();
        String fullName = edtFullName.getText().toString();
        String phoneNumber = edtPhone.getText().toString();
        String facebook = edtFacebook.getText().toString();

        // Kiểm tra xem mật khẩu và mật khẩu xác nhận có khớp hay không
        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Your password does not match the confirm password", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(SignUpActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String userRole = response.headers().get("UserRole");
                Toast.makeText(SignUpActivity.this, "Register success as " + data.get("name"), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Chuyển đến màn hình đăng nhập
    private void goToSignInActivity() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    // Xử lý sự kiện khi chạm vào biểu tượng mắt
    private void setEyeIconTouchListener(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
                        passwordVisible = !passwordVisible;
                        if (passwordVisible) {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
