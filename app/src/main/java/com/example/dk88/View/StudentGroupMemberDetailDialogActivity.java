package com.example.dk88.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Controller.StudentGroupMemberDetailController;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentGroupMemberDetailDialogActivity extends Dialog implements View.OnClickListener {

    private Context context;
    private String token;
    private String studentID;
    private String members;

    private TextView tvStudent1, tvStudent2, tvStudent3, tvStudent4, tvStudent5;
    private StudentGroupMemberDetailController mStudentGroupMemberDetailController;

    public StudentGroupMemberDetailDialogActivity(@NonNull Context context, String token, String studentID, String members) {
        super(context);
        this.context = context;
        this.token = token;
        this.studentID = studentID;
        this.members = members;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_group_information_dialog_layout);

        // Initialize views
        initView();
        mStudentGroupMemberDetailController = new StudentGroupMemberDetailController(context,token,studentID,members,tvStudent1,tvStudent2,tvStudent3,tvStudent4,tvStudent5);
        // Fetch student information and update UI
        mStudentGroupMemberDetailController.fetchStudentInfo();
    }

    // Initialize views
    private void initView() {
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
