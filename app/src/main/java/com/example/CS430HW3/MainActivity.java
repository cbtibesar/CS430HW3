package com.example.CS430HW3;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

// Please see https://github.com/google/volley
import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String url = "http://10.0.2.2:8080";
    Context mContext = null;
    File personnelData = null;
    String dbPath = null;
    StudentLocationDAO mStudentLocationDAO = null;
    ArrayList<StudentLocation> retrievedStudentLocations = new ArrayList<>();
    OurMemberDAO mOurMemberDAO = null;
    ArrayList<Student> retrievedStudents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this.getApplicationContext();

        dbPath = getCacheDir() + "/TeamMember.sqlite";
        installData(dbPath);

        // After the installation of DB, we can use that!
        mStudentLocationDAO = new StudentLocationDAO(dbPath);
        mOurMemberDAO = new OurMemberDAO(dbPath);


        // Manually add to Our Member table
        mOurMemberDAO.insertOneMember(new Student(1, "tyler", "wagner"));
        mOurMemberDAO.insertOneMember(new Student(2, "conrad", "tibesar"));
        mOurMemberDAO.insertOneMember(new Student(3, "alan", "garvey"));

        /*
        * This RequestQueue needs to be initialized in onCreate. Otherwise, you will get this:
        * java.lang.NullPointerException: Attempt to invoke virtual method 'java.io.File android.content.Context.getCacheDir()' on a null object reference
        */
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final TextView txt = (TextView) findViewById(R.id.txt);
        txt.append("--- Start of Data Retrieved by Volley from PHP Server ---\n");

        /* Prepare the Request
        *  Please refer to this tutorial for further info.
        *  https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
        *  Or Google's document
        *  https://developer.android.com/training/volley/simple
        */

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response: ", response.toString());
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jasondata = data.getJSONObject(i);

                                // These are mapping to column names
                                String id = jasondata.getString("Student_ID");
                                String firstName = jasondata.getString("First_Name");
                                String lastName = jasondata.getString("Last_Name");
                                String latitude = jasondata.getString("Lat");
                                String longitude = jasondata.getString("Lon");
                                txt.append(id + " " + firstName + " " + lastName + " " +
                                        latitude + " " + longitude + " " + "\n");
                                // txt is the TextView

                                StudentLocation studentLocation = new StudentLocation(
                                        Integer.valueOf(id),
                                        firstName,
                                        lastName,
                                        Float.valueOf(latitude),
                                        Float.valueOf(longitude));

                                Log.d("Added StudentLocation", studentLocation.toString());
                                mStudentLocationDAO.insertOneStudentLocation(studentLocation);

                            }
                            txt.append("--- End of Data ---\n"); // put the data to TextView

                            retrievedStudentLocations = mStudentLocationDAO.getAllStudentLocations();
                            retrievedStudents = mOurMemberDAO.getAllStudents();

                            Toast.makeText(mContext, "Retrieved data from OurMember {Student_ID, First_Name, Last_Name} is as follows: ", Toast.LENGTH_LONG).show();
                            for (Student student : retrievedStudents) {
                                Toast.makeText(mContext, student.getStudent_ID() + ": " +
                                        student.getFirst_Name() + " " + student.getLast_Name(), Toast.LENGTH_LONG).show();
                            }

                            txt.append("\n--- Start of QueryResult from SQLITE DB ---\n");
                            for (StudentLocation studentLocation : retrievedStudentLocations) {
                                String output = studentLocation.getStudent_ID() + ": " + studentLocation.getFirst_Name() +
                                        " " + studentLocation.getLast_Name() + " " + studentLocation.getLat() +
                                        " " + studentLocation.getLon();
                                txt.append(output + "\n");
                            }
                            txt.append("--- End of Data ---\n\n");

                            txt.append("You can see OurMember results from SQLITE DB \n on the Toast");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }finally{
                            mStudentLocationDAO.getDb().close();
                            mOurMemberDAO.getDb().close();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response", error.getMessage());
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    public void installData (String dbPath){
        personnelData = new File(dbPath);

        try {
            // Install SQLite DB
            // Step1: Read file into buffer
            InputStream isDb = getAssets().open("TeamMember.sqlite");
            int sizeDb = isDb.available();
            byte[] bufferDb = new byte[sizeDb];
            isDb.read(bufferDb);
            isDb.close();

            // Step2: Write into Android hard drive
            FileOutputStream fosDb = new FileOutputStream(personnelData);
            fosDb.write(bufferDb);
            fosDb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}