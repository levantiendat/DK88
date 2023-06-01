package com.example.dk88;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
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

    boolean passwordVisible = false;
    EditText edtFullName, edtPhone, edtID,edtUser,edtPass,edtFacebook,edtPass1;
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
        edtFacebook= (EditText) findViewById(R.id.facebookLink);
        edtPass1=(EditText) findViewById(R.id.ConfirmPassword);

        final Drawable eyeDrawable = getResources().getDrawable(R.drawable.eye);
        eyeDrawable.setBounds(0, 0, eyeDrawable.getIntrinsicWidth(), eyeDrawable.getIntrinsicHeight());
        edtPass.setCompoundDrawables(null, null, eyeDrawable, null);
        edtPass1.setCompoundDrawables(null, null, eyeDrawable, null);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> registerInfo = new HashMap<>();
                registerInfo.put("userName", edtUser.getText().toString());
                registerInfo.put("hashPass", edtPass.getText().toString());
                registerInfo.put("studentID",edtID.getText().toString());
                registerInfo.put("name",edtFullName.getText().toString());
                registerInfo.put("phoneNumber",edtPhone.getText().toString());
                registerInfo.put("facebook",edtFacebook.getText().toString());
                if(edtPass.getText().toString().compareTo(edtPass1.getText().toString())==0) {


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
                else{
                    Toast.makeText(SignUpActivity.this, "Your password is not compare to confirm password", Toast.LENGTH_LONG).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        edtPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPass.getRight() - edtPass.getCompoundDrawables()[2].getBounds().width())) {
                        // Xử lý sự kiện bấm vào biểu tượng con mắt ở đây
                        passwordVisible = !passwordVisible;

                        if (passwordVisible) {
                            edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        edtPass1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPass1.getRight() - edtPass1.getCompoundDrawables()[2].getBounds().width())) {
                        // Xử lý sự kiện bấm vào biểu tượng con mắt ở đây
                        passwordVisible = !passwordVisible;

                        if (passwordVisible) {
                            edtPass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            edtPass1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
