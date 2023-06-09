package com.example.dk88.Controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Model.ApiRequester;
import com.example.dk88.Model.ResponseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentMyGroupInfoDialogController {

    private Context context;
    private String token;
    private String studentID;
    private String groupID;

    private TextView tvStatus, tvWaiting, tvJoined, tvStudent1, tvStudent2, tvStudent3, tvStudent4, tvStudent5;
    private ArrayList<Map<String, String>> listInfoStudents = new ArrayList<>();

    public StudentMyGroupInfoDialogController(Context context, String token, String studentID, String groupID, TextView tvStatus, TextView tvWaiting, TextView tvJoined, TextView tvStudent1, TextView tvStudent2, TextView tvStudent3, TextView tvStudent4, TextView tvStudent5) {
        this.context = context;
        this.token = token;
        this.studentID = studentID;
        this.groupID = groupID;
        this.tvStatus =  tvStatus;
        this.tvWaiting = tvWaiting;
        this.tvJoined = tvJoined;
        this.tvStudent1 = tvStudent1;
        this.tvStudent2 = tvStudent2;
        this.tvStudent3 = tvStudent3;
        this.tvStudent4 = tvStudent4;
        this.tvStudent5 = tvStudent5;
    }

    // Fetch student information and update UI
    public void fetchStudentInfo() {
        String[] memberList = groupID.split("-");
//        for (int i = 0; i < memberList.length; i++) {
//            String memberId = memberList[i];
//            fetchStudentInfoForMember(memberId, i);
//        }

        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getListGroupInfo(headers, Arrays.asList(memberList));
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(context, tmp.getMessage(), Toast.LENGTH_SHORT).show();
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

            }
        });




    }


    // Fetch student information for a specific member and update UI
    private void fetchStudentInfoForMember(String memberId, int index) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getStudentInfo(headers, memberId);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(context, tmp.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void retrieveGroupInformation() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getGroupInfo(headers, groupID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(context, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                Integer status = Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString())));

                if (status==0){
                    tvStatus.setText(tvStatus.getText()+"Waiting for other students to join group");
                }else{
                    tvStatus.setText(tvStatus.getText()+"Waiting for other student to finish trade");
                }

                ArrayList<String> voteYes = (ArrayList<String>) data.get("voteYes");
                String joined = TextUtils.join(",", voteYes);
                tvJoined.setText(tvJoined.getText()+joined);

                ArrayList<String> waitList = new ArrayList<>();
                for (String member : groupID.split("-")) {
                    if (!voteYes.contains(member)) {
                        waitList.add(member);
                    }
                }
                String waiting = TextUtils.join(", ", waitList);
                tvWaiting.setText(tvWaiting.getText()+waiting);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
