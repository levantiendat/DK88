package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dk88.Controller.StudentTradeFinishController;
import com.example.dk88.R;

public class StudentTradeFinishActivity extends AppCompatActivity {
    String groupID = null;
    String lostCourse;
    String studentID;
    String userName;

    String token = "";

    TextView tvLostCourse;
    ImageView ivBack, ivReport, ivInfo;
    Button btnFinish;
    private StudentTradeFinishController mStudentTradeFinishController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_trade_group_detail_layout);

        // Khởi tạo và ánh xạ view
        initView();


        // Lấy dữ liệu từ Intent
        getDataFromIntent();

        mStudentTradeFinishController=new StudentTradeFinishController(userName,token,studentID,btnFinish,tvLostCourse,StudentTradeFinishActivity.this);
        // Kiểm tra trạng thái của nhóm
        mStudentTradeFinishController.checkStatusGroup(studentID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentTradeFinishController.navigateToDashboard();
            }
        });

        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentTradeFinishController.navigateToReport();
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentTradeFinishController.showGroupMemberDetailDialog();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentTradeFinishController.voteGroup();
            }
        });

    }
    // Lấy dữ liệu từ Intent
    public void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");
        userName = getIntent().getStringExtra("userName");
        lostCourse = getIntent().getStringExtra("lostCourse");

        tvLostCourse.setText(tvLostCourse.getText() + lostCourse);
    }
    // Khởi tạo và ánh xạ các view
    private void initView() {
        tvLostCourse = findViewById(R.id.giveClass);
        ivBack = findViewById(R.id.back);
        ivReport = findViewById(R.id.report);
        ivInfo = findViewById(R.id.info);
        btnFinish = findViewById(R.id.finish);
    }


}
