package com.example.dk88.Controller;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ListUserRequestAdapter;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentStateInfo;
import com.example.dk88.R;
import com.example.dk88.View.AdminRequestActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRequestController {
    private Toast mToast;
    Button btnPrevious, btnNext;
    ImageView btnBack;
    String token = "";
    Admin admin;
    ListUserRequestAdapter adapter;
    ListView listView;
    ArrayList<StudentStateInfo> listData;

    ArrayList<Request> listRequest = new ArrayList<>();
    int page = 1;
    private AppCompatActivity activity;

    public AdminRequestController(Toast mToast, Button btnPrevious, Button btnNext, ImageView btnBack, String token, Admin admin, ListUserRequestAdapter adapter, ListView listView, ArrayList<StudentStateInfo> listData, ArrayList<Request> listRequest, int page, AppCompatActivity activity) {
        this.mToast = mToast;
        this.btnPrevious = btnPrevious;
        this.btnNext = btnNext;
        this.btnBack = btnBack;
        this.token = token;
        this.admin = admin;
        this.adapter = adapter;
        this.listView = listView;
        this.listData = listData;
        this.listRequest = listRequest;
        this.page = page;
        this.activity = activity;
    }

    // Hàm để điền dữ liệu vào danh sách yêu cầu
    private void fillData(ArrayList<Request> listRequest) {
        listData.clear();
        for (int i = 0; i < listRequest.size(); i++) {
            StudentStateInfo std = new StudentStateInfo();
            std.setStudentID(listRequest.get(i).getTargetID());
            if (listRequest.get(i).getRequestCode() == 0) {
                std.setState("ACTIVE");
            } else {
                std.setState("BAN");
            }
            listData.add(std);

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(activity, "Page: " + String.valueOf(page), Toast.LENGTH_SHORT);
            mToast.show();
        }
        adapter = new ListUserRequestAdapter(activity, R.layout.student_list_group_item_layout, listData);
        listView.setAdapter(adapter);
    }

    // Hàm để lấy dữ liệu từ API
    public void getData(int page) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readRequestPage(headers, page);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                listRequest.clear();
                List<Map<String, Object>> data = (List<Map<String, Object>>) tmp.getData();
                for (Map<String, Object> student : data) {
                    Request temp = new Request();
                    temp.setRequestID(Math.toIntExact(Math.round(Double.parseDouble(student.get("requestID").toString()))));
                    temp.setTargetID(student.get("targetID").toString());
                    temp.setRequestCode(Math.toIntExact(Math.round(Double.parseDouble(student.get("requestCode").toString()))));
                    listRequest.add(temp);
                }
                fillData(listRequest);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Xử lý khi có lỗi
            }
        });
    }
}
