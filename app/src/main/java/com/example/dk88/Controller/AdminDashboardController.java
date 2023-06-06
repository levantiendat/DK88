package com.example.dk88.Controller;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dk88.AdminProfileActivity;
import com.example.dk88.AdminRequestActivity;
import com.example.dk88.Model.Admin;
import com.example.dk88.View.AdminDashboardActivity;
import com.example.dk88.View.SignInActivity;

public class AdminDashboardController {
    private CardView cvUsers, cvRequest, cvProfile, cvLogout;
    private String token;
    private Admin admin;
    private AppCompatActivity activity;

    public AdminDashboardController(CardView cvUsers, CardView cvRequest, CardView cvProfile, CardView cvLogout, String token, Admin admin, AppCompatActivity activity) {
        this.cvUsers = cvUsers;
        this.cvRequest = cvRequest;
        this.cvProfile = cvProfile;
        this.cvLogout = cvLogout;
        this.token = token;
        this.admin = admin;
        this.activity = activity;
    }

    // Xử lý đăng xuất
    public void logout() {
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    // Chuyển đến màn hình Admin Profile
    public void goToAdminProfile() {
        Intent intent = new Intent(activity, AdminProfileActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);
        activity.startActivity(intent);
        activity.finish();
    }

    // Chuyển đến màn hình Admin Request
    public void goToAdminRequest() {
        Intent intent = new Intent(activity, AdminRequestActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("admin", admin);
        activity.startActivity(intent);
        activity.finish();
    }
}
