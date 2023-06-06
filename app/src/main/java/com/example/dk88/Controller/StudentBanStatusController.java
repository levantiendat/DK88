package com.example.dk88.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.R;
import com.example.dk88.View.SignInActivity;
import com.example.dk88.View.StudentBanStatusActivity;

public class StudentBanStatusController {
    private AppCompatActivity activity;

    public StudentBanStatusController(AppCompatActivity activity) {
        this.activity = activity;
    }
    public void navigateToSignInActivity() {
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
    }

    public void showAdminInformationPopup() {
        Context context = activity;
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
