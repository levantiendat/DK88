package com.example.dk88;

import static android.content.ContentValues.TAG;
import static com.example.dk88.Student.STATUS_BAN_USER;
import static com.example.dk88.Student.STATUS_NEW_USER;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanRequestDetail extends AppCompatActivity {
    EditText edtTarget,edtDetail;
    String token;
    Request request;

    ImageView image;
    ArrayList<Bitmap> listBitmap=new ArrayList<Bitmap>();

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
        image=(ImageView) findViewById(R.id.image);

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
                token = response.headers().get("token");

                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(BanRequestDetail.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                String moreDetail=data.get("moreDetail").toString();
                edtDetail.setText(moreDetail);
                List<String> listUrl= (List<String>) data.get("imageProof");

                for(String url:listUrl){
                    Log.e(TAG,url);
                    loadImage(url);
                    Toast.makeText(BanRequestDetail.this,url,Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(BanRequestDetail.this, "Error", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(BanRequestDetail.this, "Error", Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.headers().get("token");

                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(BanRequestDetail.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                byte[] imageBytes = (byte[]) tmp.getData();
                // Chuyển đổi byte array thành Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                // Hiển thị ảnh lên ImageView
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(BanRequestDetail.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

}