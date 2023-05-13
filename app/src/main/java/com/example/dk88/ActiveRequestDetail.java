package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ActiveRequestDetail extends AppCompatActivity {
    TextView edtFullName, edtPhoneNumber, edtStudentId;
    String token;
    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_request_layout);
        token=getIntent().getStringExtra("token");
        request= (Request) getIntent().getSerializableExtra("request");

//        edtFullName   = (TextView) findViewById(R.id.name);




    }
}