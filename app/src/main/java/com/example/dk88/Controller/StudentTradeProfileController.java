package com.example.dk88.Controller;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.DatabaseHandler;
import com.example.dk88.Model.GroupInfo;
import com.example.dk88.Model.ListClassAdapter;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentClassRelation;
import com.example.dk88.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTradeProfileController {
    public ListClassAdapter adapter;
    ListView listView;
    public ArrayList<GroupInfo> arrayCourse=new ArrayList<>();

    EditText edtNeed, edtNotNeed;

    Context context;
    String token = "";
    public String studentID;

    public StudentTradeProfileController(ListView listView, EditText edtNeed, EditText edtNotNeed, Context context, String token, String studentID) {

        this.listView = listView;
        this.edtNeed = edtNeed;
        this.edtNotNeed = edtNotNeed;
        this.context = context;
        this.token = token;
        this.studentID = studentID;
        adapter = new ListClassAdapter(context, R.layout.student_list_class_item_layout, arrayCourse);
    }

    public void addData(String classNo) {
        // Thêm lớp học vào danh sách và cập nhật ListView
        arrayCourse.add(new GroupInfo(classNo, 0, 0, ""));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
    public void deleteOldGroup(){
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteStudentClass(studentID);
    }
    public void addToSQL() {
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

    public void getFromSQL(String id) {
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
        listView.setAdapter(adapter);
    }

    public void saveClassChanges() {
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
                Toast.makeText(context, "Update Trade Profile Successfully!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void removeToList(int position){
        // Remove the item from the list
        arrayCourse.remove(position);

        // Update the ListView
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
