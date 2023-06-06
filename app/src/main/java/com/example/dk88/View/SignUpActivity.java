package com.example.dk88.View;

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

import com.example.dk88.Controller.SignUpController;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;

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
    private SignUpController mSignUpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        // Khởi tạo các view
        initView();

        mSignUpController=new SignUpController(btnOk,back,passwordVisible,edtFullName,edtPhone,edtID,edtUser,edtPass,edtFacebook,editPassConfirm,SignUpActivity.this);
        // Xử lý sự kiện khi nhấn nút "Ok"
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignUpController.registerUser();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Back"
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignUpController.goToSignInActivity();
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
