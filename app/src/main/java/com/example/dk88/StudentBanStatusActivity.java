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

public class StudentBanStatusActivity extends AppCompatActivity {
    Button btnOK;
    TextView btnShow;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_banned_status_layout);
        btnOK=(Button) findViewById(R.id.ok);
        btnShow=(TextView) findViewById(R.id.show);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentBanStatusActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = StudentBanStatusActivity.this;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.admin_information_dialog, null);

                // Xây dựng AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                builder.setCancelable(true);

                AlertDialog dialog = builder.create();
                // Thiết lập kích thước cho dialog
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
                int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.9);
                layoutParams.width = width;
                layoutParams.height = height;
                dialog.getWindow().setAttributes(layoutParams);

                // Hiển thị dialog dưới dạng dialog
                dialog.show();
            }
        });
    }
}
