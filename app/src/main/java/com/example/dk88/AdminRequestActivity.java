package com.example.dk88;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.Admin;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentStateInfo;
import com.example.dk88.Model.ListUserRequestAdapter;
import com.example.dk88.View.AdminActiveRequestDetailActivity;
import com.example.dk88.View.AdminBanRequestDetailActivity;
import com.example.dk88.View.AdminDashboardActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRequestActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list_request_item_layout);

        // Lấy thông tin từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        // Lấy dữ liệu và cập nhật giao diện
        getData(page);

        // Thiết lập sự kiện click cho nút Back, Next, Previous và danh sách danh sách yêu cầu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến AdminDashboardActivity
                Intent intent = new Intent(AdminRequestActivity.this, AdminDashboardActivity.class);
                intent.putExtra("admin", admin);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng số trang lên 1 và lấy dữ liệu mới
                page += 1;
                getData(page);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm số trang xuống 1 (nếu số trang lớn hơn 1) và lấy dữ liệu mới
                if (page > 1) {
                    page -= 1;
                    getData(page);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý sự kiện khi click vào một yêu cầu trong danh sách
                Request request = listRequest.get(position);
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(AdminRequestActivity.this, "MSSV: " + request.getTargetID(), Toast.LENGTH_LONG);
                mToast.show();

                if (request.getRequestCode() == 0) {
                    // Nếu mã yêu cầu là 0 (ACTIVE), chuyển đến AdminActiveRequestDetailActivity
                    Intent intent = new Intent(AdminRequestActivity.this, AdminActiveRequestDetailActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("request", request);
                    startActivity(intent);
                } else {
                    // Ngược lại, chuyển đến AdminBanRequestDetailActivity
                    Intent intent = new Intent(AdminRequestActivity.this, AdminBanRequestDetailActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("request", request);
                    startActivity(intent);
                }
            }
        });
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
            mToast = Toast.makeText(AdminRequestActivity.this, "Page: " + String.valueOf(page), Toast.LENGTH_SHORT);
            mToast.show();
        }
        adapter = new ListUserRequestAdapter(this, R.layout.student_list_group_item_layout, listData);
        listView.setAdapter(adapter);
    }

    // Hàm để lấy dữ liệu từ API
    private void getData(int page) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readRequestPage(headers, page);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminRequestActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminRequestActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
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

    // Nhận dữ liệu từ Intent
    private void getDataFromIntent(){
        token = getIntent().getStringExtra("token");
        admin = (Admin) getIntent().getSerializableExtra("admin");
    }

    // Khởi tạo các view
    private void initView(){
        listView = findViewById(R.id.lwclass);
        listData = new ArrayList<>();
        btnNext = findViewById(R.id.next);
        btnPrevious = findViewById(R.id.previous);
        btnBack = findViewById(R.id.back);
    }

    // Ghi đè phương thức onBackPressed() để vô hiệu hóa nút Back
    @Override
    public void onBackPressed() {
        // Không làm gì (vô hiệu hóa nút Back)
    }
}