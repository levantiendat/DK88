package com.example.dk88;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TradeProfileDialog extends Dialog implements
        android.view.View.OnClickListener {
    ListClassAdapter adapter;
    ListView listview1;
    ArrayList<GroupInfo> arrayclass;

    Context context;

    public TradeProfileDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.trade_profile_dialog_layout);
        function();
        adddata();
        listview1.setAdapter(adapter);
    }
    private void function(){
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        adapter=new ListClassAdapter(context, R.layout.list_class_item_layout, arrayclass);

    }
    private void adddata(){
//        arrayclass.add(new GroupInfo("1020252.2220.21.14",2,5));
//        arrayclass.add(new GroupInfo("1023713.2220.21.14",1,3));
//        arrayclass.add(new GroupInfo("1022853.2220.21.14A",1,3));
//        arrayclass.add(new GroupInfo("1023453.2220.21.13A",0,2));
//        arrayclass.add(new GroupInfo("1022654.2220.21.13A",1,3));
        listview1.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {

    }
}
