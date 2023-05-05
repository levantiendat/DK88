package com.example.dk88;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentIdentity extends AppCompatActivity {
    ImageView imgFront,imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_active_request_layout);

        String token=getIntent().getStringExtra("token");
        Student student=(Student) getIntent().getSerializableExtra("student");

        imgFront=(ImageView) findViewById(R.id.imgFront);
        imgBack=(ImageView) findViewById(R.id.imgBack);

        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });
    }
}
