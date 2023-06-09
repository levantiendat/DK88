package com.example.dk88.View;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.dk88.Controller.StudentMyGroupInfoDialogController;
import com.example.dk88.R;

public class StudentMyGroupInfoDialogActivity extends Dialog implements View.OnClickListener {

    private Context context;
    private String token;
    private String studentID;
    private String groupID;

    private TextView tvStatus, tvWaitingList, tvJoinedList, tvStudent1, tvStudent2, tvStudent3, tvStudent4, tvStudent5;
    private StudentMyGroupInfoDialogController mStudentMyGroupInfoDialogController;

    public StudentMyGroupInfoDialogActivity(@NonNull Context context, String token, String studentID, String groupID) {
        super(context);
        this.context = context;
        this.token = token;
        this.studentID = studentID;
        this.groupID = groupID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_my_group_information_dialog_layout);

        // Initialize views
        initView();
        mStudentMyGroupInfoDialogController = new StudentMyGroupInfoDialogController(context,token,studentID,groupID,tvStatus, tvWaitingList, tvJoinedList, tvStudent1,tvStudent2,tvStudent3,tvStudent4,tvStudent5);
        // Fetch student information and update UI
        mStudentMyGroupInfoDialogController.fetchStudentInfo();
        mStudentMyGroupInfoDialogController.retrieveGroupInformation();
    }

    // Initialize views
    private void initView() {
        tvStatus = findViewById(R.id.status);
        tvWaitingList = findViewById(R.id.waitingList);
        tvJoinedList = findViewById(R.id.joinList);
        tvStudent1 = findViewById(R.id.student1);
        tvStudent2 = findViewById(R.id.student2);
        tvStudent3 = findViewById(R.id.student3);
        tvStudent4 = findViewById(R.id.student4);
        tvStudent5 = findViewById(R.id.student5);
    }



    @Override
    public void onClick(View view) {
        // Handle click events
    }
}
