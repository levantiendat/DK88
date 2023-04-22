package com.example.dk88;

import java.util.List;

public class QueryStudentClass {

    private Integer idQuery;
    private String targetID;
    private List<String> haveClass;
    private String wantClass;

    public QueryStudentClass() { }

    public QueryStudentClass(Integer idQuery, String targetID, List<String> haveClass, String wantClass) {
        super();
        this.idQuery = idQuery;
        this.targetID = targetID;
        this.haveClass = haveClass;
        this.wantClass = wantClass;
    }

    public Integer getIdQuery() {
        return idQuery;
    }

    public void setIdQuery(Integer idQuery) {
        this.idQuery = idQuery;
    }

    public String getTargetID() {
        return targetID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
    }

    public List<String> getHaveClass() {
        return haveClass;
    }

    public void setHaveClass(List<String> haveClass) {
        this.haveClass = haveClass;
    }

    public String getWantClass() {
        return wantClass;
    }

    public void setWantClass(String wantClass) {
        this.wantClass = wantClass;
    }



}
