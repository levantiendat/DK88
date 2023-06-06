package com.example.dk88.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.StudentBanStatusController;
import com.example.dk88.R;

public class StudentBanStatusActivity extends AppCompatActivity {
    private Button btnOK;
    private TextView btnShow;
    private StudentBanStatusController mStudentBanStatusController;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_banned_status_layout);

        initView();
        mStudentBanStatusController=new StudentBanStatusController(StudentBanStatusActivity.this);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentBanStatusController.navigateToSignInActivity();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentBanStatusController.showAdminInformationPopup();
            }
        });
    }

    private void initView() {
        btnOK = findViewById(R.id.ok);
        btnShow = findViewById(R.id.show);
    }



}
