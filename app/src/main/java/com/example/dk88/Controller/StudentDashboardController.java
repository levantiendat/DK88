package com.example.dk88.Controller;

import android.content.Intent;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.StudentAvailableGroupActivity;
import com.example.dk88.View.StudentGroupMemberDetailDialogActivity;
import com.example.dk88.View.StudentMyGroupInfoDialogActivity;
import com.example.dk88.View.StudentProfileActivity;
import com.example.dk88.View.SignInActivity;
import com.example.dk88.View.StudentTradeProfileDialogActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboardController {
    public String getMyGroup() {
        return myGroup;
    }
    String myGroup = null;
    private String token;
    private String studentID;
    private String userName;
    private AppCompatActivity activity;

    public StudentDashboardController(String token, String studentID, String userName, AppCompatActivity activity) {
        this.token = token;
        this.studentID = studentID;
        this.userName = userName;
        this.activity = activity;
    }

    // Chuyển đến màn hình Student Profile
    public void showMyProfile() {
        Intent intent = new Intent(activity, StudentProfileActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        activity.startActivity(intent);
        activity.finish();
    }

    // Chuyển đến màn hình Available Groups
    public void showMyAvailableGroup() {
        Intent intent = new Intent(activity, StudentAvailableGroupActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        activity.startActivity(intent);
        activity.finish();
    }

    // Kiểm tra xem sinh viên có group chưa
    public void checkInGroup(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
//                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                myGroup = ((String) tmp.getData());
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }

    // Chuyển đến màn hình Trade Profile
    public void showMyTradeProfile(){
        StudentTradeProfileDialogActivity dialog = new StudentTradeProfileDialogActivity(activity,token, studentID);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();
    }

    // Chuyển đến màn hình Trade Profile
    public void showMyGroup(){
        StudentMyGroupInfoDialogActivity dialog = new StudentMyGroupInfoDialogActivity(activity, token, studentID, myGroup);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();
    }

    public void logout(){
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }



}
