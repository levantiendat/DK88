package com.example.dk88;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentGroupMemberDetailDialogActivity extends Dialog implements
        android.view.View.OnClickListener {

    Context context;
    String token="";

    String studentID;
    String members;

    TextView tvStudent1, tvStudent2, tvStudent3, tvStudent4, tvStudent5;


    public StudentGroupMemberDetailDialogActivity(@NonNull Context context, String token, String studentID, String members) {
        super(context);
        this.context = context;
        this.token = token;
        this.studentID = studentID;
        this.members = members;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_group_information_dialog_layout);

        tvStudent1 = (TextView) findViewById(R.id.student1);
        tvStudent2 = (TextView) findViewById(R.id.student2);
        tvStudent3 = (TextView) findViewById(R.id.student3);
        tvStudent4 = (TextView) findViewById(R.id.student4);
        tvStudent5 = (TextView) findViewById(R.id.student5);



        String [] memberList = members.split("-");
        for (int i=0; i<memberList.length;i++){
            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getStudentInfo(headers,memberList[i]);
            int finalI = i;
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ResponseObject tmp = response.body();

                    if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                        Toast.makeText(getContext(), tmp.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Map<String, Object> data = (Map<String, Object>) tmp.getData();
                    String studentID = "Student ID: "+(String) data.get("studentID");
                    String phoneNumber = "Phone Number: "+(String) data.get("phoneNumber");
                    String facebook = "Facebook: "+(String) data.get("facebook");
                    String name = "Name: "+(String) data.get("name");
                    switch (finalI){
                        case 0:
                            tvStudent1.setText(studentID+"\n"+name+"\n"+facebook+"\n"+phoneNumber);
                            break;
                        case 1:
                            tvStudent2.setText(studentID+"\n"+name+"\n"+facebook+"\n"+phoneNumber);
                            break;
                        case 2:
                            tvStudent3.setText(studentID+"\n"+name+"\n"+facebook+"\n"+phoneNumber);
                            break;
                        case 3:
                            tvStudent4.setText(studentID+"\n"+name+"\n"+facebook+"\n"+phoneNumber);
                            break;
                        case 4:
                            tvStudent5.setText(studentID+"\n"+name+"\n"+facebook+"\n"+phoneNumber);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {

                }
            });

        }

    }

    @Override
    public void onClick(View view) {

    }
}