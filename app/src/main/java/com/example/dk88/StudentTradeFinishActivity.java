package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentTradeFinishActivity extends AppCompatActivity {
    String studentID;
    String userName;

    String token="";

    TextView tvlostCourse;
    ImageView ivBack, ivReport, ivInfo;
    Button btnFinish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_trade_group_detail_layout);

        token=getIntent().getStringExtra("token");
        studentID=getIntent().getStringExtra("studentID");
        userName=getIntent().getStringExtra("userName");

        ivBack = (ImageView) findViewById(R.id.back);
        ivReport = (ImageView) findViewById(R.id.report);
        ivInfo = (ImageView) findViewById(R.id.info);
        btnFinish = (Button) findViewById(R.id.finish);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentTradeFinishActivity.this, StudentDashboardActivity.class);
                intent.putExtra("studentID",studentID);
                intent.putExtra("token",token);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });


    }
}