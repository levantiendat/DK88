package com.example.dk88;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        Call<String> call = ApiUserRequester.getJsonPlaceHolderApi().test();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("login", "ok");
                                if (!response.isSuccessful()) {
                    Log.e("login", "Error1");
                    return;
                }
                String resp = response.body();
                Log.e("login", resp);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("login", "!ok: " + call.request().url().toString());
            }
        });

//        Log.e("login", "TEST");
//        Map<String, Object> body = new HashMap<>();
//        body.put("userName", "levantiendat");
//        body.put("hashPass", "12345678");
//        Log.e("login", body.toString());
//        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().login(body);
//        call.enqueue(new Callback<ResponseObject>() {
//            @Override
//            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
//                if (!response.isSuccessful()) {
//                    Log.e("login", "Error1");
//                    return;
//                }
//                ResponseObject resp = response.body();
//                if (resp.getRespCode() != ResponseObject.RESPONSE_OK) {
////                    Toast.makeText(SignInActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.e("login", resp.getMessage());
//                    return;
//                }
//                User user = (User) resp.getData();
////                Toast.makeText(SignInActivity.this, user.getName(), Toast.LENGTH_LONG).show();
//                Log.e("login", user.getName());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseObject> call, Throwable t) {
//                Log.e("login", "Error2");
//
//            }
//        });

    }
}
