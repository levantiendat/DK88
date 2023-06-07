package com.example.dk88;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.DatabaseHandler;
import com.example.dk88.Model.GroupInfo;
import com.example.dk88.Model.ListClassAdapter;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentClassRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTradeProfileDialogActivity extends Dialog implements
        android.view.View.OnClickListener {
    ListClassAdapter adapter;
    ListView listView;
    ArrayList<GroupInfo> arrayCourse;
    Button btnAdd, btnSave, btnCancel;
    EditText edtNeed, edtNotNeed;

    Context context;
    String token = "";
    String studentID;

    public StudentTradeProfileDialogActivity(@NonNull Context context, String token, String studentID) {
        super(context);
        this.context = context;
        this.token = token;
        this.studentID = studentID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_trade_profile_dialog_layout);


        initView();

        // Lấy danh sách lớp học từ SQLite và hiển thị lên ListView
        getFromSQL(studentID);
        listView.setAdapter(adapter);

        // Thiết lập sự kiện click cho các nút
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện khi click nút Add
                String classNo = edtNotNeed.getText().toString();
                addData(classNo);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện khi click nút Cancel
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện khi click nút Save
                addToSQL();
                saveClassChanges();
            }
        });

    }

    private void addData(String classNo) {
        // Thêm lớp học vào danh sách và cập nhật ListView
        arrayCourse.add(new GroupInfo(classNo, 0, 0, ""));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private void addToSQL() {
        // Thêm thông tin lớp học vào SQLite
        DatabaseHandler db = new DatabaseHandler(context);
        ArrayList<StudentClassRelation> list = new ArrayList<>();
        for (GroupInfo info : arrayCourse) {
            list.add(new StudentClassRelation(studentID, info.getLophp(), 1));
        }
        list.add(new StudentClassRelation(studentID, edtNeed.getText().toString(), 0));
        for (StudentClassRelation studentClass : list) {
            db.addStudentClass(studentClass);
        }
    }

    private void getFromSQL(String id) {
        // Lấy thông tin lớp học từ SQLite và cập nhật danh sách và ListView
        DatabaseHandler db = new DatabaseHandler(context);
        ArrayList<StudentClassRelation> list = (ArrayList<StudentClassRelation>) db.getStudentClass(id);
        for (StudentClassRelation info : list) {
            if (info.getHave() == 1) {
                arrayCourse.add(new GroupInfo(info.getClassId(), 0, 0, ""));
            } else {
                edtNeed.setText(info.getClassId());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void saveClassChanges() {
        // Gửi yêu cầu thay đổi thông tin lớp học lên server
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Map<String, Object> detail = new HashMap<>();
        detail.put("idQuery", 1);
        detail.put("targetID", studentID);
        ArrayList<String> arrayList = new ArrayList<>();
        for (GroupInfo gr : arrayCourse) {
            arrayList.add(gr.getLophp());
        }
        detail.put("haveClass", arrayList);
        detail.put("wantClass", edtNeed.getText().toString());
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().changeClass(headers, detail);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG).show();
                dismiss();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Xử lý sự kiện khi nhấn nút Back
        // Không làm gì cả để không đóng dialog khi nhấn nút Back
    }

    private void initView(){
        // Khởi tạo các view và thiết lập sự kiện click
        btnAdd = findViewById(R.id.add);
        btnSave = findViewById(R.id.save);
        btnCancel = findViewById(R.id.cancel);
        edtNeed = findViewById(R.id.classNeed);
        edtNotNeed = findViewById(R.id.classNo);

        // Khởi tạo danh sách lớp học và adapter
        listView = findViewById(R.id.lwclass);
        arrayCourse = new ArrayList<>();
        adapter = new ListClassAdapter(context, R.layout.student_list_class_item_layout, arrayCourse);
    }

    @Override
    public void onClick(View view) {

    }
}
