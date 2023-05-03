package com.example.dk88;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_request_layout);
        token=getIntent().getStringExtra("token");


        function();
        getData();
        addData();
        listview1.setAdapter(adapter);
    }
    private void function(){
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        adapter=new UserRequestAdapter(this, R.layout.list_group_item_layout, arrayclass);

    }
    private void addData(){
        for (Request temp: listRequest)
        {
            StudentStateInfo std = new StudentStateInfo();
            std.setStudentID(temp.getTargetID());
            if (temp.getRequestCode()==0)
            {
                std.setState("ACTIVE");
            }
            else
            {
                std.setState("BAN");
            }
            arrayclass.add(std);
        }
        listview1.setAdapter(adapter);
        System.out.println(token);
    }

    private void getData()
    {
        Map<String,Object> headers=new HashMap<>();
        headers.put("token",token);


        Toast.makeText(UserRequest.this, token, Toast.LENGTH_LONG).show();
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readRequestPage(headers,1);

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
                List<Map<String, Object>> data = (List<Map<String, Object>>) tmp.getData();
                for (Map<String, Object> student: data)
                {
                    Request temp = new Request();
                    temp.setRequestID(Math.toIntExact(Math.round(Double.parseDouble(student.get("requestID").toString()))));
                    temp.setTargetID(student.get("targetID").toString());
                    temp.setRequestCode( Math.toIntExact(Math.round(Double.parseDouble(student.get("requestCode").toString()))));
                    listRequest.add(temp);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });

    }

}
