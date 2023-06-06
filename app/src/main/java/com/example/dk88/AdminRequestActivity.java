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
    String token="";
    Admin admin;
    ListUserRequestAdapter adapter;
    ListView listview1;
    ArrayList<StudentStateInfo> arrayclass;

    ArrayList<Request> listRequest = new ArrayList<Request>();
    int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list_request_item_layout);
        token=getIntent().getStringExtra("token");
        admin=(Admin) getIntent().getSerializableExtra("admin");

        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        btnNext=(Button) findViewById(R.id.next);
        btnPrevious=(Button) findViewById(R.id.previous);
        btnBack=(ImageView) findViewById(R.id.back);
        getData(page);

        listview1.setAdapter(adapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminRequestActivity.this, AdminDashboardActivity.class);
                intent.putExtra("admin",admin);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page+=1;
                getData(page);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page>1){
                    page-=1;
                    getData(page);
                }

            }
        });
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request request = listRequest.get(position);
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(AdminRequestActivity.this,"MSSV: " +request.getTargetID(),Toast.LENGTH_LONG);
                mToast.show();

                if (request.getRequestCode()==0){
                    Intent intent=new Intent(AdminRequestActivity.this, AdminActiveRequestDetailActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("request", request);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(AdminRequestActivity.this, AdminBanRequestDetailActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("request",request);
                    startActivity(intent);
                }

            }
        });
    }
    private void fillData(ArrayList<Request> listRequest){
        arrayclass.clear();
        for (int i=0; i < listRequest.size();i++)
        {
            StudentStateInfo std = new StudentStateInfo();
            std.setStudentID(listRequest.get(i).getTargetID());
            if (listRequest.get(i).getRequestCode()==0)
            {
                std.setState("ACTIVE");
            }
            else
            {
                std.setState("BAN");
            }
            arrayclass.add(std);

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(AdminRequestActivity.this,"Page: "+ String.valueOf(page), Toast.LENGTH_SHORT);
            mToast.show();
        }
        adapter=new ListUserRequestAdapter(this, R.layout.student_list_group_item_layout, arrayclass);
        listview1.setAdapter(adapter);
    }

    private void getData(int page)
    {
        Map<String,Object> headers=new HashMap<>();
        headers.put("token",token);


        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readRequestPage(headers,page);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(AdminRequestActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(AdminRequestActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                listRequest.clear();
                List<Map<String, Object>> data = (List<Map<String, Object>>) tmp.getData();
                for (Map<String, Object> student: data)
                {
                    Request temp = new Request();
                    temp.setRequestID(Math.toIntExact(Math.round(Double.parseDouble(student.get("requestID").toString()))));
                    temp.setTargetID(student.get("targetID").toString());
                    temp.setRequestCode( Math.toIntExact(Math.round(Double.parseDouble(student.get("requestCode").toString()))));
                    listRequest.add(temp);
                }
                fillData(listRequest);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }

        });



    }
    @Override
    public void onBackPressed() {


    }




}