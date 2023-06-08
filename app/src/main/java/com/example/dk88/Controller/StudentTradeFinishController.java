package com.example.dk88.Controller;

import android.content.Intent;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.StudentReportActivity;
import com.example.dk88.View.StudentDashboardActivity;
import com.example.dk88.View.StudentGroupMemberDetailDialogActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTradeFinishController {
    String groupID = null;
    String lostCourse;
    String studentID;
    String userName;

    String token = "";

    TextView tvLostCourse;

    Button btnFinish;

    private AppCompatActivity activity;

    public StudentTradeFinishController(String userName, String token, String studentID,Button btnFinish,TextView tvLostCourse, AppCompatActivity activity) {

        this.tvLostCourse = tvLostCourse;
        this.activity = activity;
        this.studentID=studentID;
        this.token=token;
        this.userName=userName;
        this.btnFinish=btnFinish;
    }



    // Chuyển đến màn hình StudentDashboardActivity
    public void navigateToDashboard() {
        Intent intent = new Intent(activity, StudentDashboardActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        activity.startActivity(intent);
    }

    // Chuyển đến màn hình StudentReportActivity
    public void navigateToReport() {
        Intent intent = new Intent(activity, StudentReportActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        activity.startActivity(intent);
    }

    // Hiển thị dialog thông tin thành viên nhóm
    public void showGroupMemberDetailDialog() {
        StudentGroupMemberDetailDialogActivity dialog = new StudentGroupMemberDetailDialogActivity(activity, token, studentID, groupID);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();
    }

    // Gửi request đánh giá nhóm
    public void voteGroup() {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        headers.put("token", token);
        body.put("studentID", studentID);
        body.put("groupID", groupID);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().voteGroup(headers, body);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (btnFinish.getText().equals("FINISH TRADE")) {
                    btnFinish.setText("UNFINISH TRADE");
                } else {
                    btnFinish.setText("FINISH TRADE");
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }

    public void checkStatusGroup(String studentID) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                groupID = (String) tmp.getData();
                if (groupID == null) {
                    Toast.makeText(activity, "Trade Finish!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                } else {
                    setTextButton(groupID);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }

    private void setTextButton(String groupID) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers, groupID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                ArrayList<String> voteYesList = (ArrayList<String>) data.get("voteYes");
                if (voteYesList.contains(studentID)) {
                    btnFinish.setText("UNFINISH TRADE");
                } else {
                    btnFinish.setText("FINISH TRADE");
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }
}
