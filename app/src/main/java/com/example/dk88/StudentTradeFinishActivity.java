package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.StudentDashboardActivity;
import com.example.dk88.View.StudentGroupMemberDetailDialogActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTradeFinishActivity extends AppCompatActivity {
    String groupID = null;
    String lostCourse;
    String studentID;
    String userName;

    String token = "";

    TextView tvLostCourse;
    ImageView ivBack, ivReport, ivInfo;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_trade_group_detail_layout);

        // Khởi tạo và ánh xạ view
        initView();

        // Lấy dữ liệu từ Intent
        getDataFromIntent();

        // Kiểm tra trạng thái của nhóm
        checkStatusGroup(studentID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToDashboard();
            }
        });

        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToReport();
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGroupMemberDetailDialog();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voteGroup();
            }
        });

    }

    // Khởi tạo và ánh xạ các view
    private void initView() {
        tvLostCourse = findViewById(R.id.giveClass);
        ivBack = findViewById(R.id.back);
        ivReport = findViewById(R.id.report);
        ivInfo = findViewById(R.id.info);
        btnFinish = findViewById(R.id.finish);
    }

    // Lấy dữ liệu từ Intent
    private void getDataFromIntent() {
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");
        userName = getIntent().getStringExtra("userName");
        lostCourse = getIntent().getStringExtra("lostCourse");

        tvLostCourse.setText(tvLostCourse.getText() + lostCourse);
    }

    // Chuyển đến màn hình StudentDashboardActivity
    private void navigateToDashboard() {
        Intent intent = new Intent(StudentTradeFinishActivity.this, StudentDashboardActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    // Chuyển đến màn hình StudentReportActivity
    private void navigateToReport() {
        Intent intent = new Intent(StudentTradeFinishActivity.this, StudentReportActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    // Hiển thị dialog thông tin thành viên nhóm
    private void showGroupMemberDetailDialog() {
        StudentGroupMemberDetailDialogActivity dialog = new StudentGroupMemberDetailDialogActivity(StudentTradeFinishActivity.this, token, studentID, groupID);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();
    }

    // Gửi request đánh giá nhóm
    private void voteGroup() {
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
                    Toast.makeText(StudentTradeFinishActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentTradeFinishActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
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

    private void checkStatusGroup(String studentID) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(StudentTradeFinishActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentTradeFinishActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                groupID = (String) tmp.getData();
                if (groupID == null) {
                    Toast.makeText(StudentTradeFinishActivity.this, "Trade Finish!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(StudentTradeFinishActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentTradeFinishActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
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
