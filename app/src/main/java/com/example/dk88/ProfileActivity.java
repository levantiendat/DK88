package com.example.dk88;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    EditText edtOld,edtNew,edtName,edtPhone,edtFacebook;
    Button btnOK, btnBack;
    TextView txtGetAdmin;
    String token,studentID;
    Student student;
    TextView getAdmin;
    String userName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_layout);

        token=getIntent().getStringExtra("token");
        studentID=getIntent().getStringExtra("studentID");
        userName=getIntent().getStringExtra("userName");
//        ivBack=(ImageView) findViewById(R.id.back);
        edtOld=(EditText) findViewById(R.id.Password);
        edtNew=(EditText) findViewById(R.id.Password1);
        edtName=(EditText) findViewById(R.id.fullname);
        edtPhone=(EditText) findViewById(R.id.phone);
        btnOK=(Button) findViewById(R.id.ok);
        btnBack=(Button) findViewById(R.id.back);
        txtGetAdmin=(TextView) findViewById(R.id.getAdmin1);
        edtFacebook=(EditText) findViewById(R.id.facebookLink);
        getAdmin=(TextView) findViewById(R.id.getAdmin1);

        LoadDataFromServer(studentID);


        getAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminInformationPopup();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, StudentDashboard.class);
                intent.putExtra("studentID",studentID);
                intent.putExtra("token",token);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });



        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOld.getText().toString().compareTo("") != 0) {
                    if (edtOld.getText().toString().compareTo(edtNew.getText().toString()) == 0) {
                        Toast.makeText(ProfileActivity.this, "The new password is duplicated than old password", Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    ResponseObject tmp = response.body();
                                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                                        Toast.makeText(ProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    Toast.makeText(ProfileActivity.this, "Change Password successfully ", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onFailure(Call<ResponseObject> call, Throwable t) {
                                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    if (!(edtName.getText().toString().compareTo(student.getName()) == 0
                            && (edtPhone.getText().toString().compareTo(student.getPhoneNumber()) == 0))) {
                        Map<String, Object> headers = new HashMap<>();
                        headers.put("token", token);
                        Map<String, Object> changeInfo = new HashMap<>();
                        changeInfo.put("userName", userName);
                        Toast.makeText(ProfileActivity.this,edtName.getText().toString()+"--"+edtPhone.getText().toString()+"--"+edtFacebook.getText().toString(),Toast.LENGTH_LONG).show();
                        changeInfo.put("name", edtName.getText().toString());
                        changeInfo.put("phoneNumber", edtPhone.getText().toString());
                        changeInfo.put("facebook", edtFacebook.getText().toString());
                        changeInfo.put("roleCode", student.getRoleCode());

                        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers, changeInfo);
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

                                Toast.makeText(ProfileActivity.this, "Change Data successfully ", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ProfileActivity.this, StudentDashboard.class);
                                intent.putExtra("studentID", studentID);
                                intent.putExtra("token", token);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<ResponseObject> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Nothing change information", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProfileActivity.this, StudentDashboard.class);
                        intent.putExtra("studentID", studentID);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                }


        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, StudentDashboard.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName",userName);
        // Bắt đầu Activity tiếp theo với hiệu ứng chuyển động
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }
    private void showAdminInformationPopup() {
        Context context = ProfileActivity.this;
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
    private void LoadDataFromServer(String studentID){

        Map<String,Object> headers=new HashMap<>();
        headers.put("token",token);
        Call<ResponseObject> call=ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers,studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(ProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                student=new Student();
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
                Toast.makeText(ProfileActivity.this,"Error load data",Toast.LENGTH_LONG).show();
            }
        });
    }
}