package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ListStudentIDAdapter;
import com.example.dk88.Model.ListUserRequestAdapter;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentStateInfo;
import com.example.dk88.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserManagementActivity extends AppCompatActivity {


    private ImageButton btnBack;
    private EditText edtSearch;
    private ListView listStudent;
    private String token;
    private Admin admin;
    private ArrayList<String> arrayStudentID;
    private ListStudentIDAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_management_layout);
        initView();

        getDataFromIntent();

        getData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUserManagementActivity.this, AdminDashboardActivity.class);
                intent.putExtra("admin", admin);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });


    }
    private void initView(){
        btnBack=(ImageButton) findViewById(R.id.back);
        edtSearch=(EditText) findViewById(R.id.search);
        listStudent=(ListView) findViewById(R.id.listView);
    }
    private void getDataFromIntent(){
        token = getIntent().getStringExtra("token");
        admin = (Admin) getIntent().getSerializableExtra("admin");
    }
    private void getData(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getAllStudentID(headers);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminUserManagementActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminUserManagementActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                arrayStudentID = (ArrayList<String>) tmp.getData();

                fillData(arrayStudentID);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }
    private void fillData(ArrayList<String> arrayStudentID){

        adapter = new ListStudentIDAdapter(this, R.layout.admin_user_management_item_layout, arrayStudentID);
        listStudent.setAdapter(adapter);
    }

    public void onBackPressed() {

        Intent intent = new Intent(AdminUserManagementActivity.this, AdminDashboardActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}