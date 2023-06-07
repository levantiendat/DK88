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

import com.example.dk88.Controller.AdminUserProfileController;
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

    private AdminUserProfileController mAdminUserProfileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_profile_management);

        initView();
        getDataFromIntent();
        mAdminUserProfileController=new AdminUserProfileController(edtName,edtPhone,edtFacebook,txtST,spinner,studentID,admin,token,AdminUserProfileActivity.this);
        mAdminUserProfileController.getDataFromServer();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mAdminUserProfileController.selectedItem = (String) adapterView.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAdminUserProfileController.selectedItem="";
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdminUserProfileController.backToManagement();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdminUserProfileController.changeStudentInfo();
                mAdminUserProfileController.changeStatus();
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

    @Override
    public void onBackPressed() {

        mAdminUserProfileController.backToManagement();
    }
}