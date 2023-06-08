package com.example.dk88.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dk88.Model.ApiUserRequester;
import com.example.dk88.Model.DatabaseHandler;
import com.example.dk88.Model.Graph;
import com.example.dk88.Model.GroupInfo;
import com.example.dk88.Model.ListGroupAdapter;
import com.example.dk88.Model.ResponseObject;
import com.example.dk88.Model.StudentClassRelation;
import com.example.dk88.Model.StudentDemand;
import com.example.dk88.R;
import com.example.dk88.View.StudentAvailableGroupActivity;
import com.example.dk88.View.StudentTradeFinishActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAvailableGroupController {
    Map<String, List<String>> haveClass = new HashMap<>();
    Map<String, String> needClass = new HashMap<>();
    Map<String, StudentDemand> studentRequestMap = new HashMap<>();
    Graph g = new Graph();
    String token="";

    String studentID;
    String userName;
    public SharedPreferences mPrefs;

    Map<Integer, ArrayList<GroupInfo>> pageContent = new HashMap<>();
    Map<String, Integer> isPage = new HashMap<>();
    public int maxPage=0;
    public int currentPage=1;
    int maxElementPerPage=7;
    String oldGroup = null;
    ListGroupAdapter adapter;
    ListView listView;
    public ArrayList<GroupInfo> listGroupInfo =new ArrayList<>();

    private AppCompatActivity activity;

    public StudentAvailableGroupController(String token, String studentID, String userName , ListView listView, AppCompatActivity activity) {

        this.studentID = studentID;
        this.userName = userName;

        this.adapter = adapter;
        this.listView = listView;

        this.activity = activity;
        this.token=token;
    }

    public void getMyGroup(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, @NonNull Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                assert tmp != null;
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
//                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                oldGroup = ((String) tmp.getData());
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }

    public void updateGroupInfo(){
        ArrayList<String> groupIds = new ArrayList<>();
        for (GroupInfo temp: pageContent.get(currentPage)){
            groupIds.add(temp.getGroupID());
        }

        for (int i=0;i<groupIds.size();i++){
            fillPage(currentPage);
            Map<String,Object> headers=new HashMap<>();
            headers.put("token",token);

            Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers,groupIds.get(i));
            int finalI = i;
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (!response.isSuccessful())
                    {
                        Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ResponseObject tmp = response.body();
                    if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                    {
//                        Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, Object> data = (Map<String, Object>) tmp.getData();
                    ArrayList<String> voteYes = (ArrayList<String>) data.get("voteYes");

                    listGroupInfo.get(finalI).setCurrent(voteYes.size());
                    fillPage(currentPage);
                    adapter.notifyDataSetChanged();
                }


                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {

                }
            });

        }
    }
    public void fillPage(int pageNumber){
        listGroupInfo.clear();
        if (pageContent.get(pageNumber)!=null) {
            for (GroupInfo temp : pageContent.get(pageNumber)) {
                listGroupInfo.add(temp);
            }
            adapter = new ListGroupAdapter(activity, R.layout.student_list_group_item_layout, listGroupInfo);
            listView.setAdapter(adapter);
        }
    }
    private void prepareAllData(ArrayList<ArrayList<String>> listClass){
        listGroupInfo.clear();

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

    private String findLostCourse (String groupID, String studentID){
        String[] members = groupID.split("-");

        List<String> memberList = new ArrayList<>(Arrays.asList(members));
        memberList.addAll(Arrays.asList(members));
        for (int i=memberList.size()-1;i>0;i--){
            if (memberList.get(i).equals(studentID)){
                return needClass.get(memberList.get(i-1));
            }
        }
        return "";
    }

    public void checkMyGroup(){
        String beforeUpdate = oldGroup;
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupOfStudent(headers, studentID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
//                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                oldGroup = ((String) tmp.getData());
                if (beforeUpdate==null){
                    return;
                }else{
                    if (oldGroup==null){
                        Toast.makeText(activity, "You lost group "+ beforeUpdate, Toast.LENGTH_SHORT).show();
                    }else{
                        checkStatusGroup(oldGroup);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }

        });
    }


    private void checkStatusGroup(String groupID){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getGroupInfo(headers, groupID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
//                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) tmp.getData();
                Integer status = Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString())));
                if (status==1){
                    Toast.makeText(activity, "Your group is ready!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, StudentTradeFinishActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("studentID",studentID);
                    intent.putExtra("userName",userName);
                    intent.putExtra("lostCourse",findLostCourse(groupID,studentID));
                    activity.startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });


    }

    public void getData(int id){
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", token);

        Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().getNewQueryClass(headers,id);

        call.enqueue(new Callback<ResponseObject>() {

            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(activity, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResponseObject tmp = response.body();
                if (tmp.getRespCode()!=ResponseObject.RESPONSE_OK)
                {
                    Toast.makeText(activity, tmp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Map<String, Object>> data = (List<Map<String, Object>>) tmp.getData();

                DatabaseHandler db = new DatabaseHandler(activity);
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
                        StudentClassRelation temp = new StudentClassRelation(targetID,wantClass,0);
                        db.addStudentClass(temp);

                        for (String have: haveClass){
                            StudentClassRelation tp = new StudentClassRelation(targetID,have, 1);
                            db.addStudentClass(tp);
                        }


                    }else{
                        StudentClassRelation temp = new StudentClassRelation(targetID,wantClass,0);
                        db.addStudentClass(temp);

                        for (String have: haveClass){
                            StudentClassRelation tp = new StudentClassRelation(targetID,have, 1);
                            db.addStudentClass(tp);
                        }
                    }
                }
                ArrayList<StudentClassRelation> rows= (ArrayList<StudentClassRelation>) db.getAllStudentClass();
                for (StudentClassRelation row: rows){
                    String targetID = row.getStudentId();
                    String classID = row.getClassId();
                    Integer have = row.getHave();
                    if (studentRequestMap.containsKey(targetID)==false){
                        studentRequestMap.put(targetID, new StudentDemand(targetID, "", new ArrayList<>()));
                    }

                    if (have==0){
                        studentRequestMap.get(targetID).setWant(row.getClassId());
                    }else{
                        studentRequestMap.get(targetID).getHave().add(classID);
                    }
                }

                for (Map.Entry<String, StudentDemand> entry : studentRequestMap.entrySet()) {
                    String key = entry.getKey();
                    StudentDemand value = entry.getValue();
                    needClass.put(key,value.getWant());
                    for (String classID: value.getHave()){
                        haveClass.putIfAbsent(classID, new ArrayList<>());
                        haveClass.get(classID).add(key);
                    }
                }

                for (Map.Entry<String, StudentDemand> entry : studentRequestMap.entrySet()) {
                    String key = entry.getKey();
                    StudentDemand value = entry.getValue();
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
                    updateGroupInfo();
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(activity,"Error load class available",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void clearOldData(){
        listGroupInfo.clear();
        pageContent.clear();
        isPage.clear();
        studentRequestMap.clear();
        haveClass.clear();
        needClass.clear();
    }
}
