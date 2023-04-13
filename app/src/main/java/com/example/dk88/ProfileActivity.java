package com.example.dk88;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity {
    EditText edtOld,edtNew,edtName,edtPhone;
    Button btnOK;
    TextView txtGetAdmin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        String token=getIntent().getStringExtra("token");
        Toast.makeText(ProfileActivity.this,token,Toast.LENGTH_LONG).show();
        edtOld=(EditText) findViewById(R.id.Password);
        edtNew=(EditText) findViewById(R.id.Password1);
        edtName=(EditText) findViewById(R.id.fullname);
        edtPhone=(EditText) findViewById(R.id.phone);
        btnOK=(Button) findViewById(R.id.ok);
        txtGetAdmin=(TextView) findViewById(R.id.getAdmin1);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        public static Map<String, Object> getDataFromToken(String token) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        public static Boolean isValidToken(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
                return true;
            } catch (Exception e) {

            }
            return false;
        }
    }

}
