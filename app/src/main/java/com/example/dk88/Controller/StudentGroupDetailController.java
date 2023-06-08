package com.example.dk88.Controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.GroupInfo;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.StudentGroupDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentGroupDetailController {
    TextView tvLostCourse, tvDetail, tvJoined, tvWaiting, tvPhoneNumber;
    String lostCourse, detail, joined, waiting, phoneNumber;

    Button btnVote;
    String token;
    String studentID;
    HashMap<String, String> needClass;
    GroupInfo groupInfo;

    private AppCompatActivity activity;

    public StudentGroupDetailController(TextView tvLostCourse, TextView tvDetail, TextView tvJoined, TextView tvWaiting, TextView tvPhoneNumber, String lostCourse, String detail, String joined, String waiting, String phoneNumber, Button btnVote, String token, String studentID,  AppCompatActivity activity) {
        this.tvLostCourse = tvLostCourse;
        this.tvDetail = tvDetail;
        this.tvJoined = tvJoined;
        this.tvWaiting = tvWaiting;
        this.tvPhoneNumber = tvPhoneNumber;
        this.lostCourse = lostCourse;
        this.detail = detail;
        this.joined = joined;
        this.waiting = waiting;
        this.phoneNumber = phoneNumber;
        this.btnVote = btnVote;
        this.token = token;
        this.studentID = studentID;
        this.activity = activity;
    }

    public void getDataFromIntent() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            needClass = (HashMap<String, String>) bundle.getSerializable("needClass");
            studentID = bundle.getString("studentID");
            token = bundle.getString("token");
            groupInfo = (GroupInfo) bundle.getSerializable("groupInfo");
            if (needClass != null && token != null && groupInfo != null) {
                String[] members = groupInfo.getGroupID().split("-");
                HashMap<String, String> lost = new HashMap<>();
                for (int i = 0; i < members.length; i++) {
                    if (i == 0) {
                        lost.put(members[0], needClass.get(members[members.length - 1]));
                    } else {
                        lost.put(members[i], needClass.get(members[i - 1]));
                    }
                }
                for (String member : members) {
                    detail += member + " have class " + lost.get(member) + "\n";
                }

                tvLostCourse.setText(lostCourse + groupInfo.getLophp());
                tvDetail.setText(detail);
            }
        }
    }

    public void retrieveStudentPhoneNumbers() {
        for (String member : groupInfo.getGroupID().split("-")) {
            if (TextUtils.isEmpty(member))
                continue;

            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers, member);
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
                    Map<String, Object> data = (Map<String, Object>) tmp.getData();
                    phoneNumber += member + ": " + data.get("phoneNumber") + "\n";
                    tvPhoneNumber.setText(phoneNumber);
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    // Handle failure
                }
            });
        }
    }

    public void retrieveGroupInformation() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers, groupInfo.getGroupID());
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
                ArrayList<String> voteYes = (ArrayList<String>) data.get("voteYes");
                joined += TextUtils.join(",", voteYes);
                tvJoined.setText(joined);

                ArrayList<String> waitList = new ArrayList<>();
                for (String member : groupInfo.getGroupID().split("-")) {
                    if (!voteYes.contains(member)) {
                        waitList.add(member);
                    }
                }
                waiting += TextUtils.join(", ", waitList);
                tvWaiting.setText(waiting);

                if (waiting.contains(studentID)) {
                    btnVote.setText("JOIN");
                } else {
                    btnVote.setText("LEAVE");
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void voteGroup() {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> body = new HashMap<>();

        headers.put("token", token);
        body.put("studentID", studentID);
        body.put("groupID", groupInfo.getGroupID());

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

                if (btnVote.getText().equals("JOIN")) {
                    btnVote.setText("LEAVE");
                } else {
                    btnVote.setText("JOIN");
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
