package com.example.CS430HW3;

public class StudentLocation{
    private Student student;
    private Float Lat;
    private Float Lon;

    public StudentLocation(int student_ID, String first_Name, String last_Name, float lat, float lon) {
        this.student = new Student(student_ID, first_Name, last_Name);
        this.Lat = lat;
        this.Lon = lon;
    }

    public StudentLocation(){
        this.student = new Student();
        this.Lat = null;
        this.Lon = null;
    }

    public String getFirst_Name() {
        return student.getFirst_Name();
    }

    public void setFirst_Name(String first_Name) {
        student.setFirst_Name(first_Name);
    }

    public void setLast_Name(String last_Name) {
        student.setLast_Name(last_Name);
    }

    public String getLast_Name() {
        return student.getLast_Name();
    }

    public int getStudent_ID() {
        return student.getStudent_ID();
    }

    public void setStudent_ID(int student_ID) {
        student.setStudent_ID(student_ID);
    }

    public float getLat() {
        return Lat;
    }

    public void setLat(float lat) {
        Lat = lat;
    }

    public float getLon() {
        return Lon;
    }

    public void setLon(float lon) {
        Lon = lon;
    }
}
