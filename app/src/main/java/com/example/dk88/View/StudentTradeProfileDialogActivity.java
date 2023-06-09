package com.example.dk88.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.dk88.Controller.StudentTradeProfileController;
import com.example.dk88.Model.ListClassAdapter;
import com.example.dk88.R;

public class StudentTradeProfileDialogActivity extends Dialog implements
        android.view.View.OnClickListener {
    ListClassAdapter adapter;
    ListView listView;

    Button btnAdd, btnSave, btnCancel;
    EditText edtNeed, edtNotNeed;

    Context context;
    String token = "";
    String studentID;
    private StudentTradeProfileController mStudentTradeProfileController;

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

        mStudentTradeProfileController=new StudentTradeProfileController(listView,edtNeed,edtNotNeed,context,token,studentID);

        // Lấy danh sách lớp học từ SQLite và hiển thị lên ListView
        mStudentTradeProfileController.getFromSQL(mStudentTradeProfileController.studentID);


        // Thiết lập sự kiện click cho các nút
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện khi click nút Add
                String classNo = edtNotNeed.getText().toString();
                mStudentTradeProfileController.addData(classNo);
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
//                mStudentTradeProfileController.deleteOldGroup();
//                mStudentTradeProfileController.addToSQL();
                mStudentTradeProfileController.saveClassChanges();
                dismiss();
            }
        });
        mStudentTradeProfileController.adapter.setOnDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = listView.getPositionForView(v);
                mStudentTradeProfileController.removeToList(position);

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


    }

    @Override
    public void onClick(View view) {

    }
}
