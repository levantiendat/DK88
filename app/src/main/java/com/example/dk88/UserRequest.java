package com.example.dk88;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRequest extends AppCompatActivity {

    Button btnPrevious, btnNext;

    String token="";
    UserRequestAdapter adapter;
    ListView listview1;
    ArrayList<StudentStateInfo> arrayclass;

    ArrayList<Request> listRequest = new ArrayList<Request>();
    int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_request_layout);
        token=getIntent().getStringExtra("token");
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        btnNext=(Button) findViewById(R.id.next);
        btnPrevious=(Button) findViewById(R.id.previous);

        getData(page);

        listview1.setAdapter(adapter);
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
            Toast.makeText(UserRequest.this, std.getStudentID(), Toast.LENGTH_LONG).show();
        }
        adapter=new UserRequestAdapter(this, R.layout.list_group_item_layout, arrayclass);
        listview1.setAdapter(adapter);
        System.out.println(token);
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
                    Toast.makeText(UserRequest.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(UserRequest.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
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




}