package com.example.dk88;

import java.util.List;

public class StudentRequest {
    String studentID;
    String want;
    List<String> have;

    public StudentRequest(String studentID, String want, List<String> have) {
        this.studentID = studentID;
        this.want = want;
        this.have = have;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getWant() {
        return want;
    }

    public void setWant(String want) {
        this.want = want;
    }

    public List<String> getHave() {
        return have;
    }

    public void setHave(List<String> have) {
        this.have = have;
    }
}
