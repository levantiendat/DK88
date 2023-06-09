package com.example.dk88.Controller;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiRequester;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.Student;
import com.example.dk88.R;
import com.example.dk88.View.StudentDashboardActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileController {
    private EditText edtOld, edtNew, edtName, edtPhone, edtFacebook;
    private Button btnOK, btnBack;
    private TextView txtGetAdmin;
    private String token, studentID;
    private Student student;
    private TextView getAdmin;
    private String userName;

    private AppCompatActivity activity;

    public StudentProfileController(EditText edtOld, EditText edtNew, EditText edtName, EditText edtPhone, EditText edtFacebook, Button btnOK, Button btnBack, TextView txtGetAdmin, String token, String studentID, TextView getAdmin, String userName, AppCompatActivity activity) {
        this.edtOld = edtOld;
        this.edtNew = edtNew;
        this.edtName = edtName;
        this.edtPhone = edtPhone;
        this.edtFacebook = edtFacebook;
        this.btnOK = btnOK;
        this.btnBack = btnBack;
        this.txtGetAdmin = txtGetAdmin;
        this.token = token;
        this.studentID = studentID;
        this.getAdmin = getAdmin;
        this.userName = userName;
        this.activity = activity;
    }

    // Hiển thị popup thông tin admin
    public void showAdminInformationPopup() {
        Context context = activity;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.admin_information_dialog, null);

        // Xây dựng AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        // Thiết lập kích thước cho dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.9);
        layoutParams.width = width;
        layoutParams.height = height;
        dialog.getWindow().setAttributes(layoutParams);

        // Hiển thị dialog dưới dạng dialog
        dialog.show();
    }

    // Trở về màn hình Dashboard của sinh viên
    public void navigateToStudentDashboard() {
        Intent intent = new Intent(activity, StudentDashboardActivity.class);
        intent.putExtra("studentID", studentID);
        intent.putExtra("token", token);
        intent.putExtra("userName", userName);
        // Bắt đầu Activity tiếp theo với hiệu ứng chuyển động
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
    }

    // Thay đổi mật khẩu
    public void changePassword() {
        if (!edtOld.getText().toString().isEmpty()) {
            if (edtOld.getText().toString().equals(edtNew.getText().toString())) {
                Toast.makeText(activity, "The new password is duplicated than old password", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> headers = new HashMap<>();
                headers.put("token", token);
                Map<String, Object> passInfo = new HashMap<>();
                passInfo.put("userName", userName);
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

                        Toast.makeText(activity, "Change Password successfully ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    // Thay đổi thông tin sinh viên
    public void changeStudentInfo() {
        if (!(edtName.getText().toString().equals(student.getName())
                && (edtPhone.getText().toString().equals(student.getPhoneNumber())))) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);
            Map<String, Object> changeInfo = new HashMap<>();
            changeInfo.put("userName", userName);
            changeInfo.put("name", edtName.getText().toString());
            changeInfo.put("phoneNumber", edtPhone.getText().toString());
            changeInfo.put("facebook", edtFacebook.getText().toString());
            changeInfo.put("roleCode", student.getRoleCode());

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
                    Toast.makeText(activity, "Change Data successfully ", Toast.LENGTH_SHORT).show();

                    navigateToStudentDashboard();
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

    // Load dữ liệu sinh viên từ server
    public void loadDataFromServer(String studentID) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getStudentInfo(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "Lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                student = new Student();
                student.setRoleCode(1);
                student.setName(data.get("name").toString());
                student.setPhoneNumber(data.get("phoneNumber").toString());
                student.setUserName(userName);
                student.setStatus(1);
                student.setStudentID(data.get("studentID").toString());
                student.setFacebook(data.get("facebook").toString());

                edtName.setText(student.getName());
                edtPhone.setText(student.getPhoneNumber());
                edtFacebook.setText(student.getFacebook());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity, "Error Load Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
