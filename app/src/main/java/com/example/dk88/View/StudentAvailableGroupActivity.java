package com.example.dk88.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiRequester;
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

    final Integer STATUS_TRADE_GROUP = 1;
    volatile Integer groupStatus = 0;

    volatile boolean isRunning = true;

    ImageView ivBack;
    ListGroupAdapter adapter;
    ListView listView;
    ArrayList<GroupInfo> arrayclass;
    Button btnPrevious, btnNext;

    String token = "";
    SharedPreferences mPrefs;
    static final String PREFS_NAME = "idQUERY_PREFS_NAME";

    Map<String, List<String>> haveClass = new HashMap<>();
    Map<String, String> needClass = new HashMap<>();
    Map<String, StudentDemand> studentRequestMap = new HashMap<>();

    String studentID;
    String userName;

    Map<Integer, ArrayList<GroupInfo>> pageContent;
    Map<String, Integer> isPage;
    volatile int maxPage = 0;
    volatile int currentPage = 1;
    final int maxElementPerPage = 7;
    volatile String oldGroup = null;
    volatile String beforeUpdate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list_group_layout);
        token = getIntent().getStringExtra("token");
        studentID = getIntent().getStringExtra("studentID");
        userName = getIntent().getStringExtra("userName");
        btnNext = (Button) findViewById(R.id.next);
        btnPrevious = (Button) findViewById(R.id.previous);
        ivBack = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.lwclass);
        arrayclass = new ArrayList<>();
        pageContent = new HashMap<>();
        isPage = new HashMap<>();

        adapter = new ListGroupAdapter(this, R.layout.student_list_group_item_layout, arrayclass);
        listView.setAdapter(adapter);

        getMyGroup();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage + 1 <= maxPage) {
                    currentPage += 1;
                    Toast.makeText(StudentAvailableGroupActivity.this, "Page " + currentPage + " " + "out of " + maxPage, Toast.LENGTH_SHORT).show();
                    updateGroupInfo();
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage -= 1;
                    Toast.makeText(StudentAvailableGroupActivity.this, "Page " + currentPage + " " + " out of " + maxPage, Toast.LENGTH_SHORT).show();
                    updateGroupInfo();
                }

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentDashboardActivity.class);
                intent.putExtra("studentID", studentID);
                intent.putExtra("token", token);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GroupInfo groupInfo = arrayclass.get(position);
                Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentGroupDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("studentID", studentID);
                bundle.putSerializable("needClass", (Serializable) needClass);
                bundle.putString("token", token);
                bundle.putSerializable("groupInfo", groupInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getMyGroup() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(StudentAvailableGroupActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                    Toast.makeText(StudentAvailableGroupActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                oldGroup = ((String) tmp.getData());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }

    private void updateGroupInfo() {

        ArrayList<String> groupIds = new ArrayList<>();
        for (GroupInfo temp : pageContent.get(currentPage)) {
            groupIds.add(temp.getGroupID());
        }

        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);
        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getListGroupInfo(headers, groupIds);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Map<String, Object>> listGroupInfo = (List<Map<String, Object>>) tmp.getData();
                fillPage(currentPage);
                for (GroupInfo groupInfo : arrayclass)
                    for (Map<String, Object> group : listGroupInfo)
                        if (groupInfo.getGroupID().equals((String) group.get("groupID")))
                            groupInfo.setCurrent(((List<String>) group.get("voteYes")).size());

//                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });

    }

    private void fillPage(int pageNumber) {
        arrayclass.clear();
        if (pageContent.get(pageNumber) != null) {
            arrayclass.addAll(pageContent.get(pageNumber));
        }
    }

    private void prepareAllData(ArrayList<ArrayList<String>> listClass) {

        pageContent.clear();
        isPage.clear();
        int i = 0;
        int page = 1;
        for (ArrayList<String> group : listClass) {
            GroupInfo temp = new GroupInfo();
            temp.setLophp(needClass.get(group.get(group.size() - 2)));
            temp.setCurrent(0);
            temp.setMax(group.size() - 1);
            temp.setGroupID(findGroupId(group));

            if (i == maxElementPerPage) {
                page += 1;
                i = 0;
            }
            isPage.put(temp.getGroupID(), page);
            pageContent.putIfAbsent(page, new ArrayList<>());
            pageContent.get(page).add(temp);
            maxPage = Math.max(maxPage, page);

            i += 1;
        }
    }

    private String findGroupId(ArrayList<String> cycle) {
        cycle.remove(cycle.size() - 1);
        ArrayList<String> mergedCycle = new ArrayList<>(cycle);
        mergedCycle.addAll(cycle);

        String smallestElement = Collections.min(mergedCycle);
        String groupID = "";
        int startPosition = mergedCycle.indexOf(smallestElement);

        ArrayList<String> res = new ArrayList<>();
        res.add(cycle.get(startPosition));
        for (int i = startPosition + 1; i < mergedCycle.size(); i++) {
            if (mergedCycle.get(i).equals(smallestElement)) {
                break;
            }
            res.add(mergedCycle.get(i));
        }

        String delimiter = "-";
        groupID = String.join(delimiter, res);
        return groupID;
    }

    private String findLostCourse(String groupID, String studentID) {
        String[] members = groupID.split("-");

        List<String> memberList = new ArrayList<>(Arrays.asList(members));
        memberList.addAll(Arrays.asList(members));
        for (int i = memberList.size() - 1; i > 0; i--) {
            if (memberList.get(i).equals(studentID)) {
                return needClass.get(memberList.get(i - 1));
            }
        }
        return "";
    }

    private void checkMyGroup() {
        beforeUpdate = oldGroup;
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                oldGroup = ((String) tmp.getData());

                if (beforeUpdate == null) {
                    if (oldGroup == null) {
                        return;
                    } else {
                        checkStatusGroup();
                    }
                } else {
                    if (oldGroup == null) {
//                        Toast.makeText(StudentAvailableGroupActivity.this, "You lost group " + beforeUpdate, Toast.LENGTH_SHORT).show();
                    } else {
                        checkStatusGroup();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }

        });
    }


    private void checkStatusGroup() {

        if (oldGroup == null) return;

        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getGroupInfo(headers, oldGroup);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                Integer status = Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString())));
//                if (status == STATUS_TRADE_GROUP) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, "Your group is ready!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentTradeFinishActivity.class);
//                    intent.putExtra("token", token);
//                    intent.putExtra("studentID", studentID);
//                    intent.putExtra("userName", userName);
//                    intent.putExtra("lostCourse", findLostCourse(oldGroup, studentID));
//                    startActivity(intent);
//                }
                groupStatus = status;

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });


    }

    private void getData(int id) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiRequester.getJsonPlaceHolderApi().getNewQueryClass(headers, id);

        call.enqueue(new Callback<ResponseObject>() {

            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
//                    Toast.makeText(StudentAvailableGroupActivity.this, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Map<String, Object>> data = (List<Map<String, Object>>) tmp.getData();

                DatabaseHandler db = new DatabaseHandler(StudentAvailableGroupActivity.this);
                for (Map<String, Object> query : data) {
                    int latestId = mPrefs.getInt("latest_id", 0);

                    Integer id = Integer.valueOf("" + Math.round(Double.parseDouble(query.get("idQuery").toString())));
                    latestId = Math.max(latestId, id);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("latest_id", latestId);
                    editor.apply();

                    String wantClass = "";
                    String targetID = query.get("targetID").toString();
                    if (query.get("wantClass") != null) {
                        wantClass = query.get("wantClass").toString();
                    }
                    ArrayList<String> haveClass = (ArrayList<String>) query.get("haveClass");

                    if (db.isStudentClassExists(targetID)) {
                        db.deleteStudentClass(targetID);
                        StudentClassRelation temp = new StudentClassRelation(targetID, wantClass, 0);
                        db.addStudentClass(temp);

                        for (String have : haveClass) {
                            StudentClassRelation tp = new StudentClassRelation(targetID, have, 1);
                            db.addStudentClass(tp);
                        }


                    } else {
                        StudentClassRelation temp = new StudentClassRelation(targetID, wantClass, 0);
                        db.addStudentClass(temp);

                        for (String have : haveClass) {
                            StudentClassRelation tp = new StudentClassRelation(targetID, have, 1);
                            db.addStudentClass(tp);
                        }
                    }
                }

                studentRequestMap.clear();
                haveClass.clear();
                needClass.clear();

                ArrayList<StudentClassRelation> rows = (ArrayList<StudentClassRelation>) db.getAllStudentClass();
                for (StudentClassRelation row : rows) {
                    String targetID = row.getStudentId();
                    String classID = row.getClassId();
                    Integer have = row.getHave();
                    if (!studentRequestMap.containsKey(targetID)) {
                        studentRequestMap.put(targetID, new StudentDemand(targetID, "", new ArrayList<>()));
                    }

                    if (have == 0) {
                        studentRequestMap.get(targetID).setWant(row.getClassId());
                    } else {
                        studentRequestMap.get(targetID).getHave().add(classID);
                    }
                }

                for (Map.Entry<String, StudentDemand> entry : studentRequestMap.entrySet()) {
                    String key = entry.getKey();
                    StudentDemand value = entry.getValue();
                    needClass.put(key, value.getWant());
                    for (String classID : value.getHave()) {
                        haveClass.putIfAbsent(classID, new ArrayList<>());
                        haveClass.get(classID).add(key);
                    }
                }

                Graph g = new Graph();
                for (Map.Entry<String, StudentDemand> entry : studentRequestMap.entrySet()) {
                    String key = entry.getKey();
                    StudentDemand value = entry.getValue();
                    g.addVertex(key);
                    List<String> listAvailable = haveClass.get(value.getWant());
                    if (listAvailable != null) {
                        for (String availableStudent : haveClass.get(value.getWant())) {
                            g.addEdge(key, availableStudent);
                        }
                    }
                }


                ArrayList<ArrayList<String>> res = new ArrayList<>();

                try {
                    res = g.printAllCycles(studentID);
                    prepareAllData(res);
                    updateGroupInfo();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
//                Toast.makeText(StudentAvailableGroupActivity.this, "Error load class available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentDashboardActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studentID", studentID);
        intent.putExtra("userName", userName);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());


    }

    class MainControlThread implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (beforeUpdate != null && oldGroup == null)
                            Toast.makeText(StudentAvailableGroupActivity.this, "Your group " + beforeUpdate + " has been deleted due to demand changing!", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        if (groupStatus.equals(STATUS_TRADE_GROUP)) {
                            Toast.makeText(StudentAvailableGroupActivity.this, "Your group is ready!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StudentAvailableGroupActivity.this, StudentTradeFinishActivity.class);
                            intent.putExtra("token", token);
                            intent.putExtra("studentID", studentID);
                            intent.putExtra("userName", userName);
                            intent.putExtra("lostCourse", findLostCourse(oldGroup, studentID));
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    class GroupControlThread implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int latestId = mPrefs.getInt("latest_id", 0);
                getData(latestId);
                checkMyGroup();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        Thread mainThread = new Thread(new MainControlThread());
        mainThread.start();
        Thread groupThread = new Thread(new GroupControlThread());
        groupThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }
}
