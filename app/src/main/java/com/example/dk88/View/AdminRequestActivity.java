package com.example.dk88.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.AdminRequestController;
import com.example.dk88.Model.Admin;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.StudentStateInfo;
import com.example.dk88.Model.ListUserRequestAdapter;
import com.example.dk88.R;

import java.util.ArrayList;

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

    private AdminRequestController mAdminRequestController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list_request_item_layout);

        // Lấy thông tin từ Intent
        getDataFromIntent();

        // Khởi tạo các view
        initView();

        mAdminRequestController=new AdminRequestController(mToast,btnPrevious,btnNext,btnBack,token,admin,adapter,listView,listData,listRequest,page,AdminRequestActivity.this);
        // Lấy dữ liệu và cập nhật giao diện
        mAdminRequestController.getData(page);

        // Thiết lập sự kiện click cho nút Back, Next, Previous và danh sách danh sách yêu cầu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến AdminDashboardActivity
                Intent intent = new Intent(AdminRequestActivity.this, AdminMenuActivity.class);
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
                mAdminRequestController.getData(page);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm số trang xuống 1 (nếu số trang lớn hơn 1) và lấy dữ liệu mới
                if (page > 1) {
                    page -= 1;
                    mAdminRequestController.getData(page);
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
                mToast = Toast.makeText(AdminRequestActivity.this, "MSSV: " + request.getTargetID(), Toast.LENGTH_SHORT);
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