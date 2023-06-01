package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StudentDashboard extends AppCompatActivity {
    CardView cvTrade, cvProfile, cvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard_layout);
        String token=getIntent().getStringExtra("token");
        Student student=(Student) getIntent().getSerializableExtra("student");

        cvTrade=(CardView) findViewById(R.id.tradeCourse);
        cvProfile=(CardView) findViewById(R.id.userProfile);
        cvLogout=(CardView) findViewById(R.id.logout);


        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, ProfileActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("student",student);
                startActivity(intent);
                finish();
            }
        });

        cvTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, AvailableClassActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("student",student);
                startActivity(intent);
                finish();
            }
        });
    }
}