package com.example.CS430HW3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class StudentLocationDAO {
    public static final String TAG = "StudentLocationDAO";
    public static final String TABLE_NAME = "QueryResult";

    public static final String NAME = "NAME";
    public static final String INFO = "JOB";

    // Name of DB
    public static final String DATABASE_NAME = "TeamMember.db";

    // DB object
    private SQLiteDatabase db;

    public StudentLocationDAO(String dbPath) {
        db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);

        if (db != null) {
            Log.e(TAG, "Got the db instance!");
        } else {
            Log.e(TAG, "db instance is null!");
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void insertOneStudentLocation(StudentLocation s) {

        String values = s.getStudent_ID() + ", '" + s.getFirst_Name() + "', '" + s.getLast_Name() + "', " + s.getLat() + ", " + s.getLon();
        String queryString = "insert into " + TABLE_NAME + " (Student_ID, First_Name, Last_Name, Lat, Lon) VALUES (" + values + ")";

        // Working!
        Log.d(TAG, "The queryString = " + queryString);

        // Working!
        db.execSQL(queryString);
    }

    public ArrayList<StudentLocation> getAllStudentLocations () {
        ArrayList<StudentLocation> result = new ArrayList<>();
        String queryString = "";

        queryString = "select * from " + TABLE_NAME;
        Log.d(TAG, "The queryString = " + queryString);
        Cursor cursorGet = db.rawQuery(queryString, null);

        int num = cursorGet.getCount();
        Log.d(TAG, "row count = " + num);

        if (cursorGet!=null && cursorGet.getCount() > 0) {
            if (cursorGet.moveToFirst()) {
                while (!cursorGet.isAfterLast()) {
                    StudentLocation studentLocation = new StudentLocation();
                    studentLocation.setStudent_ID(Integer.valueOf(cursorGet.getInt(0)));
                    studentLocation.setFirst_Name(cursorGet.getString(1));
                    studentLocation.setLast_Name(cursorGet.getString(2));
                    studentLocation.setLat(Float.valueOf(cursorGet.getFloat(3)));
                    studentLocation.setLon(Float.valueOf(cursorGet.getFloat(4)));

                    result.add(studentLocation);
                    cursorGet.moveToNext();
                }
            }
        }
        cursorGet.close();
        return result;
    }
}
