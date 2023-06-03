package com.example.dk88;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.Request;
import com.example.dk88.Model.ResponseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBanRequestDetailActivity extends AppCompatActivity {
    EditText edtTarget,edtDetail;
    String token;
    Request request;

    ImageView image;

    Button btnAccept,btnDecline;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_ban_request_layout);
        token=getIntent().getStringExtra("token");
        request= (Request) getIntent().getSerializableExtra("request");

        edtTarget=(EditText) findViewById(R.id.target);
        edtDetail=(EditText) findViewById(R.id.content);
        edtTarget.setText(edtTarget.getText()+request.getTargetID());
        image=(ImageView) findViewById(R.id.image);
        btnAccept=(Button) findViewById(R.id.accept);
        btnDecline=(Button) findViewById(R.id.decline);

        getData();
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAccept(true);
            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAccept(false);
            }
        });

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
                    Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ResponseObject tmp = response.body();
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String moreDetail=data.get("moreDetail").toString();
                edtDetail.setText(moreDetail);
                List<String> listUrl= (List<String>) data.get("imageProof");

                for(String url:listUrl){
                    Log.e(TAG,url);
                    loadImage(url);
                    Toast.makeText(AdminBanRequestDetailActivity.this,url,Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loadImage(String url){
        Map<String,Object> header=new HashMap<>();
        header.put("token",token);
        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().readImage(header,url);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.headers().get("token");

                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(AdminBanRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
//                Log.d("MyApp", "The data type of tmp is: " + tmp.getClass().getSimpleName());
                String base64 = tmp.getData().toString();


                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT); // Giải mã chuỗi Base64 thành mảng byte
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); // Tạo đối tượng Bitmap từ mảng byte đã giải mã
                image.setImageBitmap(decodedByte); // Hiển thị đối tượng Bitmap trong ImageView
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void isAccept(Boolean accept){
        Map<String,Object> header=new HashMap<>();
        header.put("token",token);
        Map<String,Object> handle =new HashMap<>();
        handle.put("requestID",request.getRequestID());
        handle.put("targetID",request.getTargetID());
        handle.put("requestCode",request.getRequestCode());
        handle.put("isAccepted",accept);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().handleRequest(header,handle);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }
                token=response.headers().get("token");
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(AdminBanRequestDetailActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if(accept){
                    Toast.makeText(AdminBanRequestDetailActivity.this,"BAN SUCCESSFULLY",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(AdminBanRequestDetailActivity.this,"DECLINED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                }
                Intent intent=new Intent(AdminBanRequestDetailActivity.this, AdminRequestActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AdminBanRequestDetailActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

}