package com.example.dk88;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;




public class AvailableClassActivity extends AppCompatActivity {
    ListGroupAdapter adapter;
    ListView listview1;
    ArrayList<GroupInfo> arrayclass;

    ImageView imgSetting;
    String token="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_available_layout);
        token=getIntent().getStringExtra("token");
        Student student=(Student) getIntent().getSerializableExtra("student");
        function();
        adddata();
        listview1.setAdapter(adapter);
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TradeProfileDialog dialog = new TradeProfileDialog(AvailableClassActivity.this,token, student.getStudentID());

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);

                dialog.show();

            }
        });
    }
    private void function(){
        imgSetting = (ImageView) findViewById(R.id.set001);
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        adapter=new ListGroupAdapter(this, R.layout.list_group_item_layout, arrayclass);

    }
    private void adddata(){
        arrayclass.add(new GroupInfo("1020252.2220.21.14",2,5));
        arrayclass.add(new GroupInfo("1023713.2220.21.14",1,3));
        arrayclass.add(new GroupInfo("1022853.2220.21.14A",1,3));
        arrayclass.add(new GroupInfo("1023453.2220.21.13A",0,2));
        arrayclass.add(new GroupInfo("1022654.2220.21.13A",1,3));
        listview1.setAdapter(adapter);
    }

}
