package com.example.dk88.Controller;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.View.AdminUserManagementActivity;
import com.example.dk88.View.AdminUserProfileActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserProfileController {
    private EditText edtName,edtPhone,edtFacebook;
    private TextView txtST;
    private Spinner spinner;
    private String studentID;
    private Admin admin;
    private String token;
    private Student student;
    public String selectedItem;
    private AppCompatActivity activity;

    public AdminUserProfileController(EditText edtName, EditText edtPhone, EditText edtFacebook, TextView txtST, Spinner spinner, String studentID, Admin admin, String token, AppCompatActivity activity) {
        this.edtName = edtName;
        this.edtPhone = edtPhone;
        this.edtFacebook = edtFacebook;
        this.txtST = txtST;
        this.spinner = spinner;
        this.studentID = studentID;
        this.admin = admin;
        this.token = token;

        this.activity = activity;
    }
    public void getDataFromServer(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Lỗi", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                student = new Student();
                student.setRoleCode(1);
                student.setName(data.get("name").toString());
                student.setPhoneNumber(data.get("phoneNumber").toString());
                student.setUserName(data.get("userName").toString());
                student.setStatus(Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString()))));
                student.setStudentID(data.get("studentID").toString());
                student.setFacebook(data.get("facebook").toString());

                edtName.setText(student.getName());
                edtPhone.setText(student.getPhoneNumber());
                edtFacebook.setText(student.getFacebook());
                txtST.setText(txtST.getText()+" "+student.getStudentID());
                setUpSpinner(student.getStatus());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error Load Data", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void setUpSpinner(int studentStatus){
        String[] status=new String[2];
        status[0]="ACTIVE";
        status[1]="BANNED";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(studentStatus==2){
            String selectedString = "BANNED"; // Chuỗi bạn muốn thiết lập là lựa chọn sẵn
            int position = Arrays.asList(status).indexOf(selectedString);
            spinner.setSelection(position);

        }
        else{
            String selectedString = "ACTIVE"; // Chuỗi bạn muốn thiết lập là lựa chọn sẵn
            int position = Arrays.asList(status).indexOf(selectedString);
            spinner.setSelection(position);
        }

    }
    public void changeStudentInfo(){
        if (!(edtName.getText().toString().equals(student.getName())
                && (edtPhone.getText().toString().equals(student.getPhoneNumber())))) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);
            Map<String, Object> changeInfo = new HashMap<>();
            changeInfo.put("userName", student.getUserName());
            changeInfo.put("name", edtName.getText().toString());
            changeInfo.put("phoneNumber", edtPhone.getText().toString());
            changeInfo.put("facebook", edtFacebook.getText().toString());
            changeInfo.put("roleCode", student.getRoleCode());

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeProfile(headers, changeInfo);
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                        Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(activity, "Change Data successfully ", Toast.LENGTH_LONG).show();


                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(activity, "Nothing change information", Toast.LENGTH_LONG).show();
        }
    }
    public void changeStatus(){
        int change=0;
        int statusFinal=0;
        if(selectedItem.compareTo("ACTIVE")==0){
            statusFinal=1;
        }
        else{
            statusFinal=2;
        }
        if(statusFinal==2 && student.getStatus()!=2){
            change=1;
        }
        else{
            if(statusFinal==1 &&student.getStatus()%2!=1){
                change=1;
            }
        }
        if(change==1){
            Map<String,Object> headers=new HashMap<>();
            headers.put("token",token);
            Map<String,Object> statusInfo=new HashMap<>();
            statusInfo.put("studentID",student.getStudentID());
            statusInfo.put("status",statusFinal);

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeStudentStatus(headers, statusInfo);

            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                        Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    backToManagement();
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(activity, "Nothing change status information", Toast.LENGTH_LONG).show();
        }
    }
    public void backToManagement(){
        Intent intent = new Intent(activity, AdminUserManagementActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        activity.startActivity(intent);
    }
}
