package com.example.CS430HW3;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class OurMemberDAO {
    public static final String TAG = "OurMemberDAO";
    public static final String TABLE_NAME = "OurMember";

    // DB object
    private SQLiteDatabase db;

    public OurMemberDAO(String dbPath) {
        db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);

        if (db != null) {
            Log.e(TAG, "Got the db instance!");
            this.createOurMemberTable();
        } else {
            Log.e(TAG, "db instance is null!");
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    // Create OurMember table
    private void createOurMemberTable(){
        String queryString = "create table OurMember (Student_ID INT not null, First_Name VARCHAR(255) not null, Last_Name VARCHAR(255) not null);";

        // Working!
        Log.d(TAG, "The queryString = " + queryString);

        // Working!
        db.execSQL(queryString);

    }

    public void insertOneMember(Student s) {

        String values = s.getStudent_ID() + ", '" + s.getFirst_Name() + "', '" + s.getLast_Name() + "'";
        String queryString = "insert into " + TABLE_NAME + " (Student_ID, First_Name, Last_Name) VALUES (" + values + ")";

        // Working!
        Log.d(TAG, "The queryString = " + queryString);

        // Working!
        db.execSQL(queryString);
    }

    public ArrayList<Student> getAllStudents () {
        ArrayList<Student> result = new ArrayList<>();
        String queryString = "";

        queryString = "select * from " + TABLE_NAME;
        Log.d(TAG, "The queryString = " + queryString);
        Cursor cursorGet = db.rawQuery(queryString, null);

        int num = cursorGet.getCount();
        Log.d(TAG, "row count = " + num);

        if (cursorGet!=null && cursorGet.getCount() > 0) {
            if (cursorGet.moveToFirst()) {
                while (!cursorGet.isAfterLast()) {
                    Student student = new Student();
                    student.setStudent_ID(Integer.valueOf(cursorGet.getInt(0)));
                    student.setFirst_Name(cursorGet.getString(1));
                    student.setLast_Name(cursorGet.getString(2));

                    result.add(student);
                    cursorGet.moveToNext();
                }
            }
        }
        cursorGet.close();
        return result;
    }
}