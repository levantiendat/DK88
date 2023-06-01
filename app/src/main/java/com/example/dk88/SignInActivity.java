package com.example.dk88;

import static com.example.dk88.Student.STATUS_BAN_USER;
import static com.example.dk88.Student.STATUS_NEW_USER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    Button btnSignin,btnSignup;
    EditText edtUser, edtPass;
    ImageView btnEye;
    CheckBox cbRemember;
    TextView showAdmin;
    boolean passwordVisible = false;
    SharedPreferences mPrefs;
    static final String PREFS_NAME="PASSWORD_PREFS_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        btnSignin = (Button) findViewById(R.id.signin1);
        edtUser = (EditText) findViewById(R.id.Username);
        edtPass = (EditText) findViewById(R.id.Password);
        btnSignup=(Button) findViewById(R.id.signup);
        cbRemember = (CheckBox) findViewById(R.id.checkBox);
        btnEye=(ImageView) findViewById(R.id.eyeIcon);
        showAdmin=(TextView) findViewById(R.id.show) ;

        getPreferencesData();
        showAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminInformationPopup();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> loginInfo = new HashMap<>();
                loginInfo.put("userName", edtUser.getText().toString());
                loginInfo.put("hashPass", edtPass.getText().toString());
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

                        if (cbRemember.isChecked()){
                            Boolean boolIsChecked = cbRemember.isChecked();
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("pref_name",edtUser.getText().toString());
                            editor.putString("pref_pass",edtPass.getText().toString());
                            editor.putBoolean("pref_check",boolIsChecked);
                            editor.apply();
                            Toast.makeText(SignInActivity.this, "Settings have been saved",Toast.LENGTH_LONG).show();
                        }else{
                            mPrefs.edit().clear().apply();
                        }



                        Map<String, Object> data = (Map<String, Object>) tmp.getData();

                        Integer userRole = Math.toIntExact(Math.round(Double.parseDouble(data.get("roleCode").toString())));
                        if(userRole.equals(User.ROLE_CODE_ADMIN)){
                            Admin admin=new Admin();
                            admin.setUserName(data.get("userName").toString());
                            admin.setName(data.get("name").toString());
                            admin.setEmail(data.get("email").toString());
                            admin.setPhoneNumber(data.get("phoneNumber").toString());
                            admin.setRoleCode(userRole);

                            Intent intent = new Intent(SignInActivity.this, AdminDashboard.class);
                            intent.putExtra("token",token);
                            intent.putExtra("admin",admin);
                            startActivity(intent);
                            edtUser.getText().clear();
                            edtPass.getText().clear();
                        }
                        else{
                            Student student=new Student();
                            student.setRoleCode(userRole);
                            student.setUserName(data.get("userName").toString());
                            student.setName(data.get("name").toString());
                            student.setPhoneNumber(data.get("phoneNumber").toString());
                            student.setStatus(Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString()))));
                            student.setStudentID(data.get("studentID").toString());

                            if(student.getStatus()==STATUS_NEW_USER){
                                Intent intent = new Intent(SignInActivity.this, StudentIdentity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("student",student);
                                startActivity(intent);
                                edtUser.getText().clear();
                                edtPass.getText().clear();
                            }
                            else if(student.getStatus()==STATUS_BAN_USER){
                                Intent intent=new Intent(SignInActivity.this,StudentBanStatus.class);
                                startActivity(intent);
                                edtUser.getText().clear();
                                edtPass.getText().clear();
                            }
                            else{
                                Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("student",student);
                                startActivity(intent);
                                edtUser.getText().clear();
                                edtPass.getText().clear();
                            }

                        }
                        Toast.makeText(SignInActivity.this, "Login success as " + data.get("name"), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordVisible = !passwordVisible;

                if (passwordVisible) {
                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }

            }
        });

    }

    private void getPreferencesData(){
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
    private void showAdminInformationPopup() {
        Context context = SignInActivity.this;
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
}
