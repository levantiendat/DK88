package com.example.dk88;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class AvailableClassActivity extends AppCompatActivity {
    int time =10000;

    ImageView ivBack;
    ListGroupAdapter adapter;
    ListView listview1;
    ArrayList<GroupInfo> arrayclass;
    Button btnPrevious, btnNext;

    ImageView imgSetting;
    ImageView imgReload;
    String token="";
    SharedPreferences mPrefs;
    static final String PREFS_NAME="idQUERY_PREFS_NAME";

    Map<String, List<String>> haveClass = new HashMap<>();
    Map<String, String> needClass = new HashMap<>();
    Map<String, StudentRequest> studentRequestMap = new HashMap<>();
    Graph g = new Graph();

    String studentID;
    String userName;

    Map<Integer, ArrayList<GroupInfo>> pageContent;
    Map<String, Integer> isPage;
    int maxPage=0;
    int currentPage=1;
    int maxElementPerPage=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list_group_layout);
        token=getIntent().getStringExtra("token");
        studentID=getIntent().getStringExtra("studentID");
        userName=getIntent().getStringExtra("userName");
        btnNext=(Button) findViewById(R.id.next);
        btnPrevious=(Button) findViewById(R.id.previous);
        ivBack = (ImageView) findViewById(R.id.back);
        imgSetting = (ImageView) findViewById(R.id.set001);
        imgReload = (ImageView) findViewById(R.id.reload);
        listview1=(ListView) findViewById(R.id.lwclass);
        arrayclass =new ArrayList<>();
        pageContent = new HashMap<>();
        isPage = new HashMap<>();

//
//        int latestId = mPrefs.getInt("latest_id", 0);
//        getData(latestId);

//        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        int latestId = mPrefs.getInt("latest_id", 0);
//        getData(latestId);


//        QueryThread queryThread = new QueryThread();
//        queryThread.start();
//
//        GroupInfoThread groupInfoThread = new GroupInfoThread();
//        groupInfoThread.start();

        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int latestId = mPrefs.getInt("latest_id", 0);
                arrayclass.clear();
                pageContent.clear();
                isPage.clear();
                studentRequestMap.clear();
                haveClass.clear();
                needClass.clear();
                getData(latestId);
            }

        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AvailableClassActivity.this,"Page " + currentPage +" "+"out of "+maxPage,Toast.LENGTH_SHORT).show();
                if (currentPage+1<=maxPage) {
                    currentPage += 1;
                    fillPage(currentPage);
                    updateGroupInfo();
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AvailableClassActivity.this,"Page " + currentPage +" "+" out of "+maxPage,Toast.LENGTH_SHORT).show();
                if(currentPage>1){
                    currentPage-=1;
                    fillPage(currentPage);
                    updateGroupInfo();
                }

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvailableClassActivity.this, StudentDashboard.class);
                intent.putExtra("studentID",studentID);
                intent.putExtra("token",token);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TradeProfileDialog dialog = new TradeProfileDialog(AvailableClassActivity.this,token, studentID);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);

                dialog.show();

            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void updateGroupInfo(){
        ArrayList<String> groupIds = new ArrayList<>();
        Log.e("ERROR FUCK FUCK", String.valueOf(currentPage));
        for (GroupInfo temp: pageContent.get(currentPage)){
            groupIds.add(temp.getGroupID());
        }

        for (int i=0;i<groupIds.size();i++){
            Map<String,Object> headers=new HashMap<>();
            headers.put("token",token);

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers,groupIds.get(i));
            int finalI = i;
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful())
                    {
                        Toast.makeText(AvailableClassActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                    {
                        Toast.makeText(AvailableClassActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, Object> data = (Map<String, Object>) tmp.getData();
                    ArrayList<String> voteYes = (ArrayList<String>) data.get("voteYes");

                    arrayclass.get(finalI).setCurrent(voteYes.size());
                }


                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {

                }
            });

        }
        adapter.notifyDataSetChanged();
    }
    private void fillPage(int pageNumber){
//        Log.e("ERRROR FUCK", String.valueOf(pageNumber));
//        Log.e("ERRROR FUCK", String.valueOf(pageContent.get(pageNumber).size()));
        arrayclass.clear();
        if (pageContent.get(pageNumber)!=null) {
            for (GroupInfo temp : pageContent.get(pageNumber)) {
                arrayclass.add(temp);
            }
            adapter = new ListGroupAdapter(this, R.layout.student_list_group_item_layout, arrayclass);
            listview1.setAdapter(adapter);
        }

    }
    private void prepareAllData(ArrayList<ArrayList<String>> listClass){
        arrayclass.clear();

        int i=0;
        int page=1;
        for (ArrayList<String> group: listClass){
            GroupInfo temp= new GroupInfo();
            temp.setLophp(needClass.get(group.get(group.size()-2)));
            temp.setCurrent(0);
            temp.setMax(group.size()-1);
            temp.setGroupID(findGroupId(group));

            if (i==maxElementPerPage){
                page+=1;
                i=0;
            }
            isPage.put(temp.getGroupID(),page);
            pageContent.putIfAbsent(page, new ArrayList<>());
            pageContent.get(page).add(temp);
            maxPage = Math.max(maxPage,page);

            i+=1;
        }
    }
    private String findGroupId (ArrayList<String> cycle){
        cycle.remove(cycle.size()-1);
        ArrayList<String> mergedCycle = new ArrayList<>(cycle);
        mergedCycle.addAll(cycle);

        String smallestElement = Collections.min(mergedCycle);
        String groupID = "";
        int startPosition = mergedCycle.indexOf(smallestElement);

        ArrayList<String> res= new ArrayList<>();
        res.add(cycle.get(startPosition));
        for (int i=startPosition+1;i<mergedCycle.size();i++){
            if (mergedCycle.get(i)==smallestElement){
                break;
            }
            res.add(mergedCycle.get(i));
        }

        String delimiter = "-";
        groupID=String.join(delimiter,res);
        return  groupID;
    }

    private void getData(int id){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getNewQueryClass(headers,id);

        call.enqueue(new Callback<ResponseObject>() {

            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(AvailableClassActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(AvailableClassActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Map<String, Object>> data = (List<Map<String, Object>>) tmp.getData();

                DatabaseHandler db = new DatabaseHandler(AvailableClassActivity.this);
                for (Map<String, Object> query: data)
                {
                    int latestId = mPrefs.getInt("latest_id", 0);

                    Integer id = Integer.valueOf("" + Math.round(Double.parseDouble(query.get("idQuery").toString())));
                    latestId = Math.max(latestId, id);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("latest_id", latestId);
                    editor.apply();


                    String wantClass="";
                    String targetID = query.get("targetID").toString();
                    if (query.get("wantClass")!=null){
                        wantClass = query.get("wantClass").toString();
                    }
                    ArrayList<String> haveClass = (ArrayList<String>) query.get("haveClass");

                    if (db.isStudentClassExists(targetID)){
                        db.deleteStudentClass(targetID);
                        StudentClass temp = new StudentClass(targetID,wantClass,0);
                        db.addStudentClass(temp);

                        for (String have: haveClass){
                            StudentClass tp = new StudentClass(targetID,have, 1);
                            db.addStudentClass(tp);
                        }


                    }else{
                        StudentClass temp = new StudentClass(targetID,wantClass,0);
                        db.addStudentClass(temp);

                        for (String have: haveClass){
                            StudentClass tp = new StudentClass(targetID,have, 1);
                            db.addStudentClass(tp);
                        }
                    }
                }
                ArrayList<StudentClass> rows= (ArrayList<StudentClass>) db.getAllStudentClass();
                for (StudentClass row: rows){
                    String targetID = row.getStudentId();
                    String classID = row.getClassId();
                    Integer have = row.getHave();
                    if (studentRequestMap.containsKey(targetID)==false){
                        studentRequestMap.put(targetID, new StudentRequest(targetID, "", new ArrayList<>()));
                    }

                    if (have==0){
                        studentRequestMap.get(targetID).setWant(row.getClassId());

                    }else{
                        studentRequestMap.get(targetID).getHave().add(classID);
                    }
                }

                for (Map.Entry<String, StudentRequest> entry : studentRequestMap.entrySet()) {
                    String key = entry.getKey();
                    StudentRequest value = entry.getValue();
                    needClass.put(key,value.getWant());
                    for (String classID: value.getHave()){
                        haveClass.putIfAbsent(classID, new ArrayList<>());
                        haveClass.get(classID).add(key);
                    }
                }

                Graph g = new Graph();
                for (Map.Entry<String, StudentRequest> entry : studentRequestMap.entrySet()) {
                    String key = entry.getKey();
                    StudentRequest value = entry.getValue();
                    g.addVertex(key);
                    List<String> listAvailable = haveClass.get(value.getWant());
                    if (listAvailable!=null){
                        for (String availableStudent : haveClass.get(value.getWant())) {
                            g.addEdge(key, availableStudent);
                        }
                    }
                }


                ArrayList<ArrayList<String>> res = new ArrayList<>();

                try {
                    res = g.printAllCycles(studentID);
                    prepareAllData(res);
                    fillPage(currentPage);
                    updateGroupInfo();
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(AvailableClassActivity.this,"Error load class available",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class QueryThread extends Thread{
        @Override
        public void run() {
            while (true){
                try {
                    mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    int latestId = mPrefs.getInt("latest_id", 0);
                    arrayclass.clear();
                    pageContent.clear();
                    isPage.clear();
                    studentRequestMap.clear();
                    haveClass.clear();
                    needClass.clear();
                    getData(latestId);
                    fillPage(currentPage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AvailableClassActivity.this,"API CALLING "+"VER REQUEST:"+String.valueOf(latestId),Toast.LENGTH_SHORT).show();
                        }
                    });
                    Thread.sleep(time);


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    class GroupInfoThread extends Thread{
        @Override
        public void run() {
            while (true){
                try {
                    ArrayList<String> groupIds = new ArrayList<>();
                    for (GroupInfo temp: pageContent.get(currentPage)){
                        groupIds.add(temp.getGroupID());
                    }

                    for (int i=0;i<groupIds.size();i++){
                        Map<String,Object> headers=new HashMap<>();
                        headers.put("token",token);

                        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers,groupIds.get(i));
                        int finalI = i;
                        call.enqueue(new Callback<ResponseObject>() {
                            @Override
                            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                                if (!response.isSuccessful())
                                {
                                    Toast.makeText(AvailableClassActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ResponseObject tmp = response.body();
                                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                                {
                                    Toast.makeText(AvailableClassActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                                ArrayList<String> voteYes = (ArrayList<String>) data.get("voteYes");

                                arrayclass.get(finalI).setCurrent(voteYes.size());

                            }

                            @Override
                            public void onFailure(Call<ResponseObject> call, Throwable t) {

                            }
                        });

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }
}
