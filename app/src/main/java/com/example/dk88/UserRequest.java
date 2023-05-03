package com.example.dk88;

import android.os.Bundle;
import android.widget.BaseAdapter;
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
    ArrayList<StudentActiveInfo> arrayclass;

    ArrayList<Request> listRequest = new ArrayList<Request>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_request_layout);
        token=getIntent().getStringExtra("token");


        function();
        adddata();
        getData();
        listview1.setAdapter(adapter);
    }
    private void function(){
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        adapter=new UserRequestAdapter(this, R.layout.list_group_item_layout, arrayclass);

    }
    private void adddata(){
        arrayclass.add(new StudentActiveInfo("102210001","ACTIVE"));
        arrayclass.add(new StudentActiveInfo("122112111","BAN"));
        arrayclass.add(new StudentActiveInfo("123212321","ACTIVE"));
        arrayclass.add(new StudentActiveInfo("102210010","ACTiVE"));
        arrayclass.add(new StudentActiveInfo("107656544","BAN"));
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
                    Toast.makeText(UserRequest.this,student.get("requestID").toString()+student.get("targetID")+student.get("requestCode") , Toast.LENGTH_SHORT).show();
                    temp.setRequestID(Integer.parseInt(student.get("requestID").toString()));
//                    temp.setTargetID(student.get("targetID").toString());
//                    temp.setRequestCode( Integer.parseInt(student.get("requestCode").toString()));
                    listRequest.add(temp);
                }
//                                Toast.makeText(UserRequest.this, Integer.toString(listRequest.size()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });

    }

}
