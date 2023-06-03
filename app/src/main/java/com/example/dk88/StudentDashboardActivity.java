package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboardActivity extends AppCompatActivity {
    CardView cvTrade, cvProfile, cvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard_layout);
        String token=getIntent().getStringExtra("token");
        String studentID=getIntent().getStringExtra("studentID");
        String userName=getIntent().getStringExtra("userName");
        cvTrade=(CardView) findViewById(R.id.tradeCourse);
        cvProfile=(CardView) findViewById(R.id.userProfile);
        cvLogout=(CardView) findViewById(R.id.logout);


        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("studentID",studentID);
                intent.putExtra("userName",userName);
                startActivity(intent);
                finish();
            }
        });

        cvTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, StudentAvailableGroupActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("studentID",studentID);
                intent.putExtra("userName",userName);
                startActivity(intent);
                finish();
            }
        });
    }
}