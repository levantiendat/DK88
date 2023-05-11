package com.example.dk88;

import static com.example.dk88.Student.STATUS_BAN_USER;
import static com.example.dk88.Student.STATUS_NEW_USER;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanRequestDetail extends AppCompatActivity {
    EditText edtTarget,edtDetail;
    String token;
    Request request;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ban_request_layout);
        token=getIntent().getStringExtra("token");
        request= (Request) getIntent().getSerializableExtra("request");

        edtTarget=(EditText) findViewById(R.id.target);
        edtDetail=(EditText) findViewById(R.id.content);
        edtTarget.setText(edtTarget.getText()+request.getTargetID());

        getData();

    }
    private void getData(){
        Map<String,Object> header=new HashMap<>();
        header.put("token",token);
        Map<String,Object> detail =new HashMap<>();
        detail.put("requestID",request.getRequestID());
        detail.put("targetID",request.getTargetID());
        detail.put("requestCode",request.getRequestCode());
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readDetailRequest(header,detail);
        call.enqueue(new Callback<ResponseObject>() {
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(BanRequestDetail.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                String token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(BanRequestDetail.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String moreDetail=data.get("moreDetail").toString();
                edtDetail.setText(moreDetail);
                List<String> listUrl= (List<String>) data.get("imageProof");

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(BanRequestDetail.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}