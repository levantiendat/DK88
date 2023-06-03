package com.example.dk88.Model;

public class StudentClassRelation {
    String studentId;
    String classId;
    Integer have;

    public StudentClassRelation(String studentId, String classId, Integer have){
        this.studentId = studentId;
        this.classId = classId;
        this.have = have;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getHave() {
        return have;
    }

    public void setHave(Integer have) {
        this.have = have;
    }
}
