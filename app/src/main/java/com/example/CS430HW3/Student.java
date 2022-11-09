package com.example.CS430HW3;

public class Student {
    private Integer Student_ID;
    private String First_Name;
    private String Last_Name;

    public Student(Integer student_ID, String first_Name, String last_Name) {
        Student_ID = student_ID;
        First_Name = first_Name;
        Last_Name = last_Name;
    }

    public Student(){
        Student_ID = null;
        First_Name = null;
        Last_Name = null;
    }

    public Integer getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(Integer student_ID) {
        Student_ID = student_ID;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }
}
