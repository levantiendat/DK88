package com.example.dk88;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileActivity extends AppCompatActivity {
    EditText edtOld,edtNew,edtName,edtPhone,edtEmail;
    Button btnOK, btnBack;
    String token;
    Admin admin;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_layout);
        token=getIntent().getStringExtra("token");
        admin=(Admin) getIntent().getSerializableExtra("admin");

        edtOld=(EditText) findViewById(R.id.Password);
        edtNew=(EditText) findViewById(R.id.Password1);
        edtName=(EditText) findViewById(R.id.fullname);
        edtPhone=(EditText) findViewById(R.id.phone);
        edtEmail=(EditText) findViewById(R.id.email);
        btnOK=(Button) findViewById(R.id.ok);
        btnBack=(Button) findViewById(R.id.back);
        edtName.setText(admin.getName());
        edtPhone.setText(admin.getPhoneNumber());
        edtEmail.setText(admin.getEmail());


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminProfileActivity.this, AdminDashboard.class);
                intent.putExtra("admin",admin);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((edtOld.getText().toString().compareTo("")==0&&edtName.getText().toString().compareTo(admin.getName())==0)
                        &&(edtEmail.getText().toString().compareTo(admin.getEmail())==0 &&edtPhone.getText().toString().compareTo(admin.getPhoneNumber())==0)){
                    Toast.makeText(AdminProfileActivity.this,"Nothing change information",Toast.LENGTH_LONG).show();

                }
                if(edtOld.getText().toString().compareTo("")!=0){
                    if(edtOld.getText().toString().compareTo(edtNew.getText().toString())==0){
                        Toast.makeText(AdminProfileActivity.this,"The new password is duplicated than old password",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Map<String,Object> headers=new HashMap<>();
                        headers.put("token",token);
                        Map<String,Object> passInfo=new HashMap<>();
                        passInfo.put("userName",admin.getUserName());
                        passInfo.put("oldHashPass",edtOld.getText().toString().trim());
                        passInfo.put("newHashPass",edtNew.getText().toString().trim());

                        Call<ResponseObject> call1 = ApiUserRequester.getJsonPlaceHolderApi().changePass(headers,passInfo);
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
                                Intent intent = new Intent(AdminProfileActivity.this, AdminDashboard.class);
                                intent.putExtra("admin",admin);
                                intent.putExtra("token",token);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<ResponseObject> call, Throwable t) {
                                Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                if(!(edtName.getText().toString().compareTo(admin.getName())==0
                        &&(edtEmail.getText().toString().compareTo(admin.getEmail())==0 &&edtPhone.getText().toString().compareTo(admin.getPhoneNumber())==0))) {
                    Map<String,Object> headers=new HashMap<>();
                    headers.put("token",token);
                    Map<String, Object> changeInfo = new HashMap<>();
                    changeInfo.put("userName", admin.getUserName());
                    changeInfo.put("name", edtName.getText().toString());
                    changeInfo.put("phoneNumber", edtPhone.getText().toString());
                    changeInfo.put("roleCode", admin.getRoleCode());

                    Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers,changeInfo);
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
                            Intent intent = new Intent(AdminProfileActivity.this, AdminDashboard.class);
                            intent.putExtra("admin",admin);
                            intent.putExtra("token",token);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(AdminProfileActivity.this,"Nothing change information",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminProfileActivity.this, AdminDashboard.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);

        // Bắt đầu Activity tiếp theo với hiệu ứng chuyển động
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }
}