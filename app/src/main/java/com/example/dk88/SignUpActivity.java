package com.example.dk88;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Button btnOk;
    TextView back;
    EditText edtFullName, edtPhone, edtID,edtUser,edtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        btnOk=(Button) findViewById(R.id.register);
        edtFullName=(EditText) findViewById(R.id.fullname);
        edtPhone=(EditText) findViewById(R.id.phone);
        edtID=(EditText) findViewById(R.id.studentid);
        edtUser=(EditText) findViewById(R.id.Username);
        edtPass=(EditText) findViewById(R.id.Password);
        back=(TextView) findViewById(R.id.back);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> registerInfo = new HashMap<>();
                registerInfo.put("userName", edtUser.getText().toString());
                registerInfo.put("hashPass", edtPass.getText().toString());
                registerInfo.put("studentID",edtID.getText().toString());
                registerInfo.put("name",edtFullName.getText().toString());
                registerInfo.put("phoneNumber",edtPhone.getText().toString());
                Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().signup(registerInfo);
                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ResponseObject tmp = response.body();
                        if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                            Toast.makeText(SignUpActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Map<String, Object> data = (Map<String, Object>) tmp.getData();
                        String userRole = response.headers().get("UserRole");
                        Toast.makeText(SignUpActivity.this, "Register success as " + data.get("name"), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
