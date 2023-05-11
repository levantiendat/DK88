package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

public class BanRequestDetail extends AppCompatActivity {
    EditText edtTarget;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ban_request_layout);
        String token=getIntent().getStringExtra("token");
        Request request= (Request) getIntent().getSerializableExtra("request");

        edtTarget=(EditText) findViewById(R.id.target);
        edtTarget.setText(edtTarget.getText()+request.getTargetID());


    }
}