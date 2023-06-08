package com.example.dk88.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Controller.StudentAvailableGroupController;
import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.DatabaseHandler;
import com.example.dk88.Model.Graph;
import com.example.dk88.Model.GroupInfo;
import com.example.dk88.Model.ListGroupAdapter;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentClassRelation;
import com.example.dk88.Model.StudentDemand;
import com.example.dk88.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class StudentAvailableGroupActivity extends AppCompatActivity {
    ImageView ivBack;

    ListView listView;

    Button btnPrevious, btnNext;


    ImageView imgReload;
    String token="";

    static final String PREFS_NAME="idQUERY_PREFS_NAME";


    Map<String, String> needClass = new HashMap<>();

    Graph g = new Graph();

    String studentID;
    String userName;


    int maxPage=0;
    int currentPage=1;

    String oldGroup = null;

    private StudentAvailableGroupController mStudentAvailableGroupController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list_group_layout);

        initView();
        mStudentAvailableGroupController=new StudentAvailableGroupController(token,studentID,userName,listView,StudentAvailableGroupActivity.this);
        mStudentAvailableGroupController.getMyGroup();

        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mStudentAvailableGroupController.mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int latestId = mStudentAvailableGroupController.mPrefs.getInt("latest_id", 0);
                mStudentAvailableGroupController.clearOldData();
                mStudentAvailableGroupController.getData(latestId);
                mStudentAvailableGroupController.checkMyGroup();
            }

        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentAvailableGroupActivity.this,"Page " + currentPage +" "+"out of "+maxPage,Toast.LENGTH_SHORT).show();
                if (mStudentAvailableGroupController.currentPage+1<=mStudentAvailableGroupController.maxPage) {
                    mStudentAvailableGroupController.currentPage += 1;
                    mStudentAvailableGroupController.fillPage(currentPage);
                    mStudentAvailableGroupController.updateGroupInfo();
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentAvailableGroupActivity.this,"Page " + currentPage +" "+" out of "+maxPage,Toast.LENGTH_SHORT).show();
                if(mStudentAvailableGroupController.currentPage>1){
                    mStudentAvailableGroupController.currentPage-=1;
                    mStudentAvailableGroupController.fillPage(currentPage);
                    mStudentAvailableGroupController.updateGroupInfo();
                }

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentDashboardActivity.class);
                intent.putExtra("studentID",studentID);
                intent.putExtra("token",token);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GroupInfo groupInfo = mStudentAvailableGroupController.listGroupInfo.get(position);
                Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentGroupDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("studentID", studentID);
                bundle.putSerializable("needClass", (Serializable) needClass);
                bundle.putString("token",token);
                bundle.putSerializable("groupInfo", groupInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentDashboardActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName",userName);
        // Bắt đầu Activity tiếp theo với hiệu ứng chuyển động
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());


    }

    private void initView(){
        token=getIntent().getStringExtra("token");
        studentID=getIntent().getStringExtra("studentID");
        userName=getIntent().getStringExtra("userName");
        btnNext=(Button) findViewById(R.id.next);
        btnPrevious=(Button) findViewById(R.id.previous);
        ivBack = (ImageView) findViewById(R.id.back);

        imgReload = (ImageView) findViewById(R.id.reload);
        listView=(ListView) findViewById(R.id.lwclass);
    }


}