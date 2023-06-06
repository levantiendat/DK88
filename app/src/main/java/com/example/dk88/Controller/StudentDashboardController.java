package com.example.dk88.Controller;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dk88.StudentAvailableGroupActivity;
import com.example.dk88.StudentProfileActivity;
import com.example.dk88.View.SignInActivity;
import com.example.dk88.View.StudentDashboardActivity;

public class StudentDashboardController {
    private CardView cvTrade, cvProfile, cvLogout;
    private String token;
    private String studentID;
    private String userName;
    private AppCompatActivity activity;

    public StudentDashboardController(CardView cvTrade, CardView cvProfile, CardView cvLogout, String token, String studentID, String userName, AppCompatActivity activity) {
        this.cvTrade = cvTrade;
        this.cvProfile = cvProfile;
        this.cvLogout = cvLogout;
        this.token = token;
        this.studentID = studentID;
        this.userName = userName;
        this.activity = activity;
    }
    // Xử lý đăng xuất
    public void logout() {
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    // Chuyển đến màn hình Student Profile
    public void goToStudentProfile() {
        Intent intent = new Intent(activity, StudentProfileActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        activity.startActivity(intent);
        activity.finish();
    }

    // Chuyển đến màn hình Available Groups
    public void goToAvailableGroups() {
        Intent intent = new Intent(activity, StudentAvailableGroupActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        activity.startActivity(intent);
        activity.finish();
    }
}
