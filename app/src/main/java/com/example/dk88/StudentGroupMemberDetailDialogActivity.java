package com.example.dk88;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;

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

        // Fetch student information and update UI
        fetchStudentInfo();
    }

    // Initialize views
    private void initView() {
        tvStudent1 = findViewById(R.id.student1);
        tvStudent2 = findViewById(R.id.student2);
        tvStudent3 = findViewById(R.id.student3);
        tvStudent4 = findViewById(R.id.student4);
        tvStudent5 = findViewById(R.id.student5);
    }

    // Fetch student information and update UI
    private void fetchStudentInfo() {
        String[] memberList = members.split("-");
        for (int i = 0; i < memberList.length; i++) {
            String memberId = memberList[i];
            fetchStudentInfoForMember(memberId, i);
        }
    }

    // Fetch student information for a specific member and update UI
    private void fetchStudentInfoForMember(String memberId, int index) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers, memberId);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(getContext(), tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String studentID = "Student ID: " + (String) data.get("studentID");
                String phoneNumber = "Phone Number: " + (String) data.get("phoneNumber");
                String facebook = "Facebook: " + (String) data.get("facebook");
                String name = "Name: " + (String) data.get("name");

                // Update the UI based on the index
                updateUI(index, studentID, name, facebook, phoneNumber);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Update the UI based on the index
    private void updateUI(int index, String studentID, String name, String facebook, String phoneNumber) {
        switch (index) {
            case 0:
                tvStudent1.setText(studentID + "\n" + name + "\n" + facebook + "\n" + phoneNumber);
                break;
            case 1:
                tvStudent2.setText(studentID + "\n" + name + "\n" + facebook + "\n" + phoneNumber);
                break;
            case 2:
                tvStudent3.setText(studentID + "\n" + name + "\n" + facebook + "\n" + phoneNumber);
                break;
            case 3:
                tvStudent4.setText(studentID + "\n" + name + "\n" + facebook + "\n" + phoneNumber);
                break;
            case 4:
                tvStudent5.setText(studentID + "\n" + name + "\n" + facebook + "\n" + phoneNumber);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        // Handle click events
    }
}
