package com.example.dk88;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.http2.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    Button btnSignin,btnSignup;
    EditText edtUser, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        btnSignin = (Button) findViewById(R.id.signin1);
        edtUser = (EditText) findViewById(R.id.Username);
        edtPass = (EditText) findViewById(R.id.Password);
        btnSignup=(Button) findViewById(R.id.signup);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> loginInfo = new HashMap<>();
                loginInfo.put("userName", edtUser.getText().toString());
                loginInfo.put("hashPass", edtPass.getText().toString());
                Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().login(loginInfo);
                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ResponseObject tmp = response.body();
                        String token = response.headers().get("token");
                        if (token==null)
                        {
                            Toast.makeText(SignInActivity.this, "NULL", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this, token, Toast.LENGTH_LONG).show();
                        }




                        if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                            Toast.makeText(SignInActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Map<String, Object> data = (Map<String, Object>) tmp.getData();
                        String userRole = response.headers().get("UserRole");
                        Toast.makeText(SignInActivity.this, "Login success as " + data.get("name"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
                        intent.putExtra("token",token);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
