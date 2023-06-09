package com.example.dk88.Controller;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.View.AdminDashboardActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileController {
    private EditText edtOld, edtNew, edtName, edtPhone, edtEmail;
    private Button btnOK, btnBack;
    private String token;
    private Admin admin;
    private AppCompatActivity activity;

    public AdminProfileController(EditText edtOld, EditText edtNew, EditText edtName, EditText edtPhone, EditText edtEmail, Button btnOK, Button btnBack, String token, Admin admin, AppCompatActivity activity) {
        this.edtOld = edtOld;
        this.edtNew = edtNew;
        this.edtName = edtName;
        this.edtPhone = edtPhone;
        this.edtEmail = edtEmail;
        this.btnOK = btnOK;
        this.btnBack = btnBack;
        this.token = token;
        this.admin = admin;
        this.activity = activity;
    }
    // Chuyển đến AdminDashboardActivity
    public void goToAdminDashboard() {
        Intent intent = new Intent(activity, AdminDashboardActivity.class);
        intent.putExtra("admin", admin);
        intent.putExtra("token", token);
        activity.startActivity(intent);
    }

    // Kiểm tra và thực hiện các thay đổi thông tin
    public void checkAndPerformChanges() {
        if (edtOld.getText().toString().isEmpty() && edtName.getText().toString().equals(admin.getName())
                && edtEmail.getText().toString().equals(admin.getEmail()) && edtPhone.getText().toString().equals(admin.getPhoneNumber())) {
            Toast.makeText(activity, "Nothing change information", Toast.LENGTH_SHORT).show();
        }else{
            if (!edtOld.getText().toString().isEmpty()) {
                if (edtOld.getText().toString().equals(edtNew.getText().toString())) {
                    Toast.makeText(activity, "The new password is duplicated than old password", Toast.LENGTH_SHORT).show();
                } else {
                    // Thay đổi mật khẩu
                    changePassword();
                }
            }
            if (!edtName.getText().toString().equals(admin.getName())
                    || !edtEmail.getText().toString().equals(admin.getEmail())
                    || !edtPhone.getText().toString().equals(admin.getPhoneNumber())) {
                // Thay đổi thông tin
                changeProfile();
            } else {
                Toast.makeText(activity, "Nothing change information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Thay đổi mật khẩu
    private void changePassword() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Map<String, Object> passInfo = new HashMap<>();
        passInfo.put("userName", admin.getUserName());
        passInfo.put("oldHashPass", edtOld.getText().toString().trim());
        passInfo.put("newHashPass", edtNew.getText().toString().trim());

        Call<ResponseObject> call1 = ApiRequester.getJsonPlaceHolderApi().changePass(headers, passInfo);
        call1.enqueue(new Callback<ResponseObject>() {
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
                String userRole = response.headers().get("UserRole");
                Toast.makeText(activity, "Change Password successfully ", Toast.LENGTH_SHORT).show();
                goToAdminDashboard();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Thay đổi thông tin
    private void changeProfile() {
        if (!(edtName.getText().toString().equals(admin.getName())
                && (edtPhone.getText().toString().equals(admin.getPhoneNumber()) && edtEmail.getText().toString().equals(admin.getEmail())))) {


            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);

            Map<String, Object> changeInfo = new HashMap<>();
            changeInfo.put("userName", admin.getUserName());
            changeInfo.put("name", edtName.getText().toString());
            changeInfo.put("phoneNumber", edtPhone.getText().toString());
            changeInfo.put("email", edtEmail.getText().toString());
            changeInfo.put("roleCode", admin.getRoleCode());

            Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().changeProfile(headers, changeInfo);
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
                    String userRole = response.headers().get("UserRole");
                    Toast.makeText(activity, "Change Data successfully ", Toast.LENGTH_SHORT).show();
                    goToAdminDashboard();
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Nothing change information", Toast.LENGTH_SHORT).show();
        }
    }
}
