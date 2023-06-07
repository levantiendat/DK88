package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserProfileActivity extends AppCompatActivity {
    private EditText edtName,edtPhone,edtFacebook;
    private TextView txtST;
    private Spinner spinner;
    private Button btnOK,btnBack;
    private String studentID;
    private Admin admin;
    private String token;
    private Student student;
    private String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_profile_management);

        initView();
        getDataFromIntent();
        getDataFromServer();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItem = (String) adapterView.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedItem="";
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToManagement();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStudentInfo();
                changeStatus();
            }
        });

    }
    private void initView(){
        edtName=findViewById(R.id.fullname);
        edtPhone=findViewById(R.id.phone);
        edtFacebook=findViewById(R.id.facebookLink);
        btnOK=findViewById(R.id.ok);
        btnBack=findViewById(R.id.back);
        spinner=findViewById(R.id.cbbStatus);
        txtST=findViewById(R.id.studentID);
    }
    private void getDataFromIntent(){
        token = getIntent().getStringExtra("token");
        admin = (Admin) getIntent().getSerializableExtra("admin");
        studentID = getIntent().getStringExtra("studentID");

    }
    private void getDataFromServer(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminUserProfileActivity.this, "Lỗi", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminUserProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                student = new Student();
                student.setRoleCode(1);
                student.setName(data.get("name").toString());
                student.setPhoneNumber(data.get("phoneNumber").toString());
                student.setUserName(data.get("userName").toString());
                student.setStatus(Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString()))));
                student.setStudentID(data.get("studentID").toString());
                student.setFacebook(data.get("facebook").toString());

                edtName.setText(student.getName());
                edtPhone.setText(student.getPhoneNumber());
                edtFacebook.setText(student.getFacebook());
                txtST.setText(txtST.getText()+" "+student.getStudentID());
                setUpSpinner(student.getStatus());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminUserProfileActivity.this, "Error Load Data", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void setUpSpinner(int studentStatus){
        String[] status=new String[2];
        status[0]="ACTIVE";
        status[1]="BANNED";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(studentStatus==2){
            String selectedString = "BANNED"; // Chuỗi bạn muốn thiết lập là lựa chọn sẵn
            int position = Arrays.asList(status).indexOf(selectedString);
            spinner.setSelection(position);

        }
        else{
            String selectedString = "ACTIVE"; // Chuỗi bạn muốn thiết lập là lựa chọn sẵn
            int position = Arrays.asList(status).indexOf(selectedString);
            spinner.setSelection(position);
        }

    }
    private void changeStudentInfo(){
        if (!(edtName.getText().toString().equals(student.getName())
                && (edtPhone.getText().toString().equals(student.getPhoneNumber())))) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);
            Map<String, Object> changeInfo = new HashMap<>();
            changeInfo.put("userName", student.getUserName());
            changeInfo.put("name", edtName.getText().toString());
            changeInfo.put("phoneNumber", edtPhone.getText().toString());
            changeInfo.put("facebook", edtFacebook.getText().toString());
            changeInfo.put("roleCode", student.getRoleCode());

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers, changeInfo);
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(AdminUserProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                        Toast.makeText(AdminUserProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(AdminUserProfileActivity.this, "Change Data successfully ", Toast.LENGTH_LONG).show();


                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toast.makeText(AdminUserProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(AdminUserProfileActivity.this, "Nothing change information", Toast.LENGTH_LONG).show();
        }
    }
    private void changeStatus(){
        int change=0;
        int statusFinal=0;
        if(selectedItem.compareTo("ACTIVE")==0){
            statusFinal=1;
        }
        else{
            statusFinal=2;
        }
        if(statusFinal==2 && student.getStatus()!=2){
            change=1;
        }
        else{
            if(statusFinal==1 &&student.getStatus()%2!=1){
                change=1;
            }
        }
        if(change==1){
            Map<String,Object> headers=new HashMap<>();
            headers.put("token",token);
            Map<String,Object> statusInfo=new HashMap<>();
            statusInfo.put("studentID",student.getStudentID());
            statusInfo.put("status",statusFinal);

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeStudentStatus(headers, statusInfo);

            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(AdminUserProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                        Toast.makeText(AdminUserProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(AdminUserProfileActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    backToManagement();
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toast.makeText(AdminUserProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(AdminUserProfileActivity.this, "Nothing change status information", Toast.LENGTH_LONG).show();
        }
    }
    private void backToManagement(){
        Intent intent = new Intent(AdminUserProfileActivity.this, AdminUserManagementActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

        backToManagement();
    }
}