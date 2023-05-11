package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class BanRequestDetail extends AppCompatActivity {
    EditText edtTarget;
    String token;
    Request request;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ban_request_layout);
        token=getIntent().getStringExtra("token");
        request= (Request) getIntent().getSerializableExtra("request");

        edtTarget=(EditText) findViewById(R.id.target);
        edtTarget.setText(edtTarget.getText()+request.getTargetID());

        getData();

    }
    private void getData(){
        Map<String,Object> header=new HashMap<>();
        header.put("token",token);
        Map<String,Object> detail =new HashMap<>();

    }
}