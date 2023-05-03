package com.example.dk88;

public class StudentStateInfo {
    private String StudentID;
    private String State;

    public StudentStateInfo() {
    }

    public StudentStateInfo(String studentID, String state) {
        StudentID = studentID;
        State = state;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
