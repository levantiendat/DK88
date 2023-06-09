package com.example.dk88.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dk88.Controller.StudentGroupDetailController;
import com.example.dk88.R;

public class StudentGroupDetailActivity extends AppCompatActivity {

    TextView tvLostCourse, tvDetail, tvJoined, tvWaiting, tvPhoneNumber;
    String lostCourse, detail, joined, waiting, phoneNumber;

    Button btnVote;
    ImageView ivBack;

    String token;
    String studentID;


    private StudentGroupDetailController mStudentGroupDetailController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_group_detail_layout);

        initView();
        mStudentGroupDetailController=new StudentGroupDetailController(tvLostCourse,tvDetail,tvJoined,tvWaiting,tvPhoneNumber,lostCourse,detail,joined,waiting,phoneNumber,btnVote,token,studentID,StudentGroupDetailActivity.this);
        mStudentGroupDetailController.getDataFromIntent();
        mStudentGroupDetailController.retrieveStudentPhoneNumbers();
        mStudentGroupDetailController.retrieveGroupInformation();

        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStudentGroupDetailController.voteGroup();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        tvLostCourse = findViewById(R.id.giveClass);
        tvDetail = findViewById(R.id.detail);
        tvJoined = findViewById(R.id.joinList);
        tvWaiting = findViewById(R.id.waitingList);
        tvPhoneNumber = findViewById(R.id.phoneNumber);
        btnVote = findViewById(R.id.vote_button);
        ivBack = findViewById(R.id.back);

        lostCourse = "Summary: To get the class you want, you will have to give a class ";
        detail = "Detail: \n";
        joined = "Joined: ";
        waiting = "Waiting: ";
        phoneNumber = "Phone number: \n";
    }


}
