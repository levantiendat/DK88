package com.example.dk88;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;




public class AvailableClassActivity extends AppCompatActivity {
    ListGroupAdapter adapter;
    ListView listview1;
    ArrayList<GroupInfo> arrayclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_available_layout);
        function();
        adddata();
        listview1.setAdapter(adapter);
    }
    private void function(){
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
