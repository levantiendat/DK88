package com.example.dk88;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeProfileDialog extends Dialog implements
        android.view.View.OnClickListener {
    ListClassAdapter adapter;
    ListView listview1;
    ArrayList<GroupInfo> arrayclass;
    Button btnAdd,btnSave,btnCancel;
    EditText edtNeed,edtNotNeed;

    Context context;
    String token="";

    String studentID;

    public TradeProfileDialog(@NonNull Context context,String token,String studentID) {
        super(context);
        this.context = context;
        this.token=token;
        //this.student=new Student(student);
        this.studentID=studentID;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.trade_profile_dialog_layout);
        btnAdd=(Button) findViewById(R.id.add);
        btnSave=(Button) findViewById(R.id.save);
        btnCancel=(Button) findViewById(R.id.cancel);
        edtNeed=(EditText) findViewById(R.id.classNeed);
        edtNotNeed=(EditText) findViewById(R.id.classNo);
        function();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classNo=edtNotNeed.getText().toString();
                adddata(classNo);

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        adapter.setOnDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = listview1.getPositionForView(v);

                // Remove the item from the list
                arrayclass.remove(position);

                // Update the ListView
                adapter.notifyDataSetChanged();
                listview1.setAdapter(adapter);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> headers=new HashMap<>();
                headers.put("token",token);
                Map<String,Object> detail=new HashMap<>();
                detail.put("idQuery",1);
                detail.put("targetID",studentID);
                ArrayList<String> arrayList=new ArrayList<>();
                for(GroupInfo gr :arrayclass){
                    arrayList.add(gr.getLophp());
                }
                detail.put("haveClass",arrayList);
                detail.put("wantClass",edtNeed.getText().toString());
                Call<ResponseObject> call=ApiUserRequester.getJsonPlaceHolderApi().changeClass(headers,detail);
                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(context,response.message().toString(),Toast.LENGTH_LONG).show();
                        dismiss();

                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
    private void function(){
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        adapter=new ListClassAdapter(context, R.layout.list_class_item_layout, arrayclass);

    }
    private void adddata(String classNo){

        arrayclass.add(new GroupInfo(classNo,0,0));
        listview1.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {

    }
}
