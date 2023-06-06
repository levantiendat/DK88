package com.example.dk88;

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

import com.example.dk88.View.SignInActivity;

public class StudentBanStatusActivity extends AppCompatActivity {
    private Button btnOK;
    private TextView btnShow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_banned_status_layout);

        initView();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignInActivity();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminInformationPopup();
            }
        });
    }

    private void initView() {
        btnOK = findViewById(R.id.ok);
        btnShow = findViewById(R.id.show);
    }


    private void navigateToSignInActivity() {
        Intent intent = new Intent(StudentBanStatusActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void showAdminInformationPopup() {
        Context context = StudentBanStatusActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.admin_information_dialog, null);

        AlertDialog dialog = buildAlertDialog(context, dialogView);
        setDialogSize(dialog, context);

        // Display the dialog
        dialog.show();
    }

    private AlertDialog buildAlertDialog(Context context, View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        return builder.create();
    }

    private void setDialogSize(AlertDialog dialog, Context context) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.9);

        layoutParams.width = width;
        layoutParams.height = height;

        dialog.getWindow().setAttributes(layoutParams);
    }
}
