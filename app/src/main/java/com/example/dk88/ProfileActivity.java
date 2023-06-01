package com.example.dk88;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    EditText edtOld,edtNew,edtName,edtPhone,edtFacebook;
    Button btnOK;
    TextView txtGetAdmin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_layout);

        String token=getIntent().getStringExtra("token");
        Student student=(Student) getIntent().getSerializableExtra("student");

        edtOld=(EditText) findViewById(R.id.Password);
        edtNew=(EditText) findViewById(R.id.Password1);
        edtName=(EditText) findViewById(R.id.fullname);
        edtPhone=(EditText) findViewById(R.id.phone);
        btnOK=(Button) findViewById(R.id.ok);
        txtGetAdmin=(TextView) findViewById(R.id.getAdmin1);
        edtFacebook=(EditText) findViewById(R.id.facebookLink);

        edtName.setText(student.getName());
        edtPhone.setText(student.getPhoneNumber());
        edtFacebook.setText(student.getFacebook());

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((edtOld.getText().toString().compareTo("")==0&&edtName.getText().toString().compareTo(student.getName())==0)
                        &&(edtPhone.getText().toString().compareTo(student.getPhoneNumber())==0)){

                }
                if(edtOld.getText().toString().compareTo("")!=0){
                    if(edtOld.getText().toString().compareTo(edtNew.getText().toString())==0){
                        Toast.makeText(ProfileActivity.this,"The new password is duplicated than old password",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Map<String,Object> headers=new HashMap<>();
                        headers.put("token",token);
                        Map<String,Object> passInfo=new HashMap<>();
                        passInfo.put("userName",student.getUserName());
                        passInfo.put("oldHashPass",edtOld.getText().toString().trim());
                        passInfo.put("newHashPass",edtNew.getText().toString().trim());

                        Call<ResponseObject> call1 = ApiUserRequester.getJsonPlaceHolderApi().changePass(headers,passInfo);
                        call1.enqueue(new Callback<ResponseObject>() {
                            @Override
                            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                ResponseObject tmp = response.body();
                                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                                    Toast.makeText(ProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                                String userRole = response.headers().get("UserRole");
                                Toast.makeText(ProfileActivity.this, "Change Password successfully ", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseObject> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                if(!(edtName.getText().toString().compareTo(student.getName())==0
                        &&(edtPhone.getText().toString().compareTo(student.getPhoneNumber())==0))) {
                    Map<String,Object> headers=new HashMap<>();
                    headers.put("token",token);
                    Map<String, Object> changeInfo = new HashMap<>();
                    changeInfo.put("userName", student.getUserName());
                    changeInfo.put("name", edtName.getText().toString());
                    changeInfo.put("phoneNumber", edtPhone.getText().toString());
                    changeInfo.put("facebook",edtFacebook.getText().toString());
                    changeInfo.put("roleCode", student.getRoleCode());

                    Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers,changeInfo);
                    call.enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                                return;
                            }
                            ResponseObject tmp = response.body();
                            if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                                Toast.makeText(ProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Map<String, Object> data = (Map<String, Object>) tmp.getData();
                            String userRole = response.headers().get("UserRole");
                            Toast.makeText(ProfileActivity.this, "Change Data successfully ", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ProfileActivity.this, AvailableClassActivity.class);
                            intent.putExtra("student",student);
                            intent.putExtra("token",token);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Nothing change information", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProfileActivity.this, AvailableClassActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("student",student);
                    startActivity(intent);
                }

            }
        });

    }

}