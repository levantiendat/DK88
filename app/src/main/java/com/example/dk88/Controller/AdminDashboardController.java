package com.example.dk88.Controller;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dk88.View.AdminProfileActivity;
import com.example.dk88.View.AdminRequestActivity;
import com.example.dk88.Model.Admin;
import com.example.dk88.View.SignInActivity;

public class AdminDashboardController {

    private String token;
    private Admin admin;
    private AppCompatActivity activity;

    public AdminDashboardController( String token, Admin admin, AppCompatActivity activity) {

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
