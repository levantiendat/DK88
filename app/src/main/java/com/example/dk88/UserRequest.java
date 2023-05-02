package com.example.dk88;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserRequest extends AppCompatActivity {
    UserRequestAdapter adapter;
    ListView listview1;
    ArrayList<StudentActiveInfo> arrayclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_request_layout);
        function();
        adddata();
        listview1.setAdapter(adapter);
    }
    private void function(){
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        adapter=new UserRequestAdapter(this, R.layout.list_group_item_layout, arrayclass);

    }
    private void adddata(){
        arrayclass.add(new StudentActiveInfo("102210001","ACTIVE"));
        arrayclass.add(new StudentActiveInfo("122112111","BAN"));
        arrayclass.add(new StudentActiveInfo("123212321","ACTIVE"));
        arrayclass.add(new StudentActiveInfo("102210010","ACTiVE"));
        arrayclass.add(new StudentActiveInfo("107656544","BAN"));
        listview1.setAdapter(adapter);
    }
}
