package com.example.dk88.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.SignInController;
import com.example.dk88.R;

public class SignInActivity extends AppCompatActivity {
    private Button btnSignin, btnSignup;
    private EditText edtUser, edtPass;
    private ImageView btnEye;
    private CheckBox cbRemember;
    private TextView showAdmin;
    private boolean passwordVisible = false;
    private SharedPreferences mPrefs;
    public SignInController mSignInController;
    private static final String PREFS_NAME = "PASSWORD_PREFS_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        // Khởi tạo SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        // Ánh xạ các phần tử UI
        initView();

        mSignInController=new SignInController(btnSignin,btnSignup,edtUser,edtPass,btnEye,cbRemember,showAdmin,passwordVisible,mPrefs,SignInActivity.this);
        // Lấy dữ liệu từ SharedPreferences và hiển thị lên giao diện
        getPreferencesData();

        // Xử lý sự kiện click button "Sign In"
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInController.performSignIn();
            }
        });

        // Xử lý sự kiện click button "Sign Up"
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInController.navigateToSignUp();
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
                mSignInController.showAdminInformationPopup();
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
    public void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_name")){
            String u = sp.getString("pref_name","not found");
            edtUser.setText(u.toString());
        }
        if (sp.contains("pref_pass")){
            String p = sp.getString("pref_pass","not found");
            edtPass.setText(p.toString());
        }
        if (sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check",false);
            cbRemember.setChecked(b);
        }
    }
    public void togglePasswordVisibility() {
        if (passwordVisible) {
            edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }

        passwordVisible = !passwordVisible;
        edtPass.setSelection(edtPass.length());
    }

}
