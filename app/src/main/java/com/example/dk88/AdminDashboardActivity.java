package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dk88.Model.Admin;

public class AdminDashboardActivity extends AppCompatActivity {
    CardView cvUsers, cvRequest, cvProfile, cvLogout;
    int swipe=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_layout);
        String token=getIntent().getStringExtra("token");
        Admin admin=(Admin) getIntent().getSerializableExtra("admin");

        cvUsers=(CardView) findViewById(R.id.userManagement);
        cvRequest=(CardView) findViewById(R.id.requestManagement);
        cvProfile=(CardView) findViewById(R.id.adminProfile);
        cvLogout=(CardView) findViewById(R.id.logout);


        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this, AdminProfileActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("admin",admin);
                startActivity(intent);
                finish();
            }
        });

        cvRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this, AdminRequestActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("admin",admin);
                startActivity(intent);
                finish();
            }
        });

        cvUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


}