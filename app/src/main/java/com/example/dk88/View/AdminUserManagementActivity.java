package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dk88.Controller.AdminUserManagementController;
import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ListStudentIDAdapter;
import com.example.dk88.R;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagementActivity extends AppCompatActivity {


    private ImageButton btnBack;
    private EditText edtSearch;
    private ListView listStudent;
    private String token;
    private Admin admin;
    private ArrayList<String> arrayStudentID;
    private List<String> listStudentID;
    private ListStudentIDAdapter adapter;
    private AdminUserManagementController mAdminUserManagementController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_management_layout);
        initView();

        getDataFromIntent();
        mAdminUserManagementController=new AdminUserManagementController(edtSearch,listStudent,token,admin,arrayStudentID,listStudentID,adapter,AdminUserManagementActivity.this);
        mAdminUserManagementController.getData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdminUserManagementController.backToDashBoard();
            }
        });
        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String studentID=mAdminUserManagementController.listStudentID.get(position);
                mAdminUserManagementController.gotoAdminUserProfile(studentID);
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().toLowerCase();
                mAdminUserManagementController.filter(searchText);
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

    public void onBackPressed() {

        mAdminUserManagementController.backToDashBoard();
    }
}