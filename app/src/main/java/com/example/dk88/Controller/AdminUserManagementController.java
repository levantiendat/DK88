package com.example.dk88.Controller;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ListStudentIDAdapter;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.R;
import com.example.dk88.View.AdminDashboardActivity;
import com.example.dk88.View.AdminUserManagementActivity;
import com.example.dk88.View.AdminUserProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserManagementController {
    private EditText edtSearch;
    private ListView listStudent;
    private String token;
    private Admin admin;
    private ArrayList<String> arrayStudentID;
    public List<String> listStudentID;
    private ListStudentIDAdapter adapter;
    private AppCompatActivity activity;

    public AdminUserManagementController(EditText edtSearch, ListView listStudent, String token, Admin admin, ArrayList<String> arrayStudentID, List<String> listStudentID, ListStudentIDAdapter adapter, AppCompatActivity activity) {
        this.edtSearch = edtSearch;
        this.listStudent = listStudent;
        this.token = token;
        this.admin = admin;
        this.arrayStudentID = arrayStudentID;
        this.listStudentID = listStudentID;
        this.adapter = adapter;
        this.activity = activity;
    }

    public void getData(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getAllStudentID(headers);

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

                arrayStudentID = (ArrayList<String>) tmp.getData();
                listStudentID=new ArrayList<>();
                listStudentID.addAll(arrayStudentID);

                fillData(listStudentID);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }
    private void fillData(List<String> arrayStudentID){

        adapter = new ListStudentIDAdapter(activity, R.layout.admin_user_management_item_layout, arrayStudentID);
        listStudent.setAdapter(adapter);
    }
    public void gotoAdminUserProfile(String studentID){
        Intent intent = new Intent(activity, AdminUserProfileActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        intent.putExtra("studentID",studentID);
        activity.startActivity(intent);
    }
    public void backToDashBoard(){
        Intent intent = new Intent(activity, AdminDashboardActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        activity.startActivity(intent);
    }
    public void filter(String searchText){
        // Tạo một danh sách tạm thời để lưu trữ các chuỗi phù hợp với tìm kiếm
        List<String> tempList = new ArrayList<>();

        for (String item : arrayStudentID) {
            if (item.toLowerCase().contains(searchText)) {
                tempList.add(item);
            }
        }

        // Cập nhật dữ liệu cho ListView
        listStudentID.clear();
        listStudentID.addAll(tempList);
        adapter.notifyDataSetChanged();
    }
}
