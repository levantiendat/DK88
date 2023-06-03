package com.example.dk88;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.ResponseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTradeFinishActivity extends AppCompatActivity {
    String groupID=null;
    String lostCourse;
    String studentID;
    String userName;

    String token="";

    TextView tvlostCourse;
    ImageView ivBack, ivReport, ivInfo;
    Button btnFinish;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_trade_group_detail_layout);

        token=getIntent().getStringExtra("token");
        studentID=getIntent().getStringExtra("studentID");
        userName=getIntent().getStringExtra("userName");
        lostCourse=getIntent().getStringExtra("lostCourse");

        ivBack = (ImageView) findViewById(R.id.back);
        ivReport = (ImageView) findViewById(R.id.report);
        ivInfo = (ImageView) findViewById(R.id.info);
        tvlostCourse = (TextView) findViewById(R.id.giveClass);
        btnFinish = (Button) findViewById(R.id.finish);


        tvlostCourse.setText(tvlostCourse.getText()+lostCourse);

        checkStatusGroup(studentID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentTradeFinishActivity.this, StudentDashboardActivity.class);
                intent.putExtra("studentID",studentID);
                intent.putExtra("token",token);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> headers = new HashMap<>();
                Map<String, Object> body = new HashMap<>();
                headers.put("token", token);
                body.put("studentID",studentID);
                body.put("groupID", groupID);

                Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().voteGroup(headers,body);
                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(StudentTradeFinishActivity.this, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ResponseObject tmp = response.body();

                        if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                            Toast.makeText(StudentTradeFinishActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (btnFinish.getText().equals("FINISH TRADE")){
                            btnFinish.setText("UNFINISH TRADE");
                        }else{
                            btnFinish.setText("FINISH TRADE");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {

                    }
                });

            }
        });
    }
    private void checkStatusGroup(String studentID){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(StudentTradeFinishActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(StudentTradeFinishActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                groupID = (String) tmp.getData();
                if (groupID==null){
                    Toast.makeText(StudentTradeFinishActivity.this, "Trade Finish!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentTradeFinishActivity.this, StudentAvailableGroupActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("studentID",studentID);
                    intent.putExtra("userName",userName);
                    startActivity(intent);
                }else{
                    setTextButton(groupID);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });

    }
    private void setTextButton(String groupID){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers, groupID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(StudentTradeFinishActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(StudentTradeFinishActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                ArrayList<String> voteYesList = (ArrayList<String>) data.get("voteYes");
                if (voteYesList.contains(studentID)){
                    btnFinish.setText("UNFINISH TRADE");
                }else{
                    btnFinish.setText("FINISH TRADE");
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });

    }
}