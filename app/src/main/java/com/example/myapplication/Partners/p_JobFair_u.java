package com.example.myapplication.Partners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class p_JobFair_u extends AppCompatActivity {

    private TextView p_jfDescription, p_jfLocation, p_jfDate, p_jfName, p_jfAttendees,
            p_jfDateStop, p_jfOrganizer;

    String URL = "http://192.168.1.9/dole_php/p_jobfair_u.php";

    private String jfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_job_fair_u);

        Intent intent = getIntent();
        final String jfId = intent.getStringExtra("jobfairId");

        p_jfName = findViewById(R.id.p_jfName);
        p_jfDate = findViewById(R.id.p_jfDate);
        p_jfLocation = findViewById(R.id.p_jfLocation);
        p_jfDescription = findViewById(R.id.p_jfDescription);
        p_jfAttendees = findViewById(R.id.p_jfAttendees);
        p_jfDateStop = findViewById(R.id.p_jfDateStop);
        p_jfOrganizer = findViewById(R.id.p_jfOrganizer);
        Button employerListBtn = findViewById(R.id.employerlistBtn);

        employerListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(p_JobFair_u.this, p_EmployerList.class);
                intent.putExtra("jobfairName", jfName);
                intent.putExtra("jobfairId", jfId);
                startActivity(intent);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobfair");

                            if (success.equals("1")){
                                for (int y = 0; y<jsonArray.length(); y++){
                                    JSONObject object = jsonArray.getJSONObject(y);
                                    jfName = object.getString("jf_name").trim();
                                    setTitle("Upcoming: " + jfName);
                                    p_jfName.setText(object.getString("jf_name").trim());
                                    p_jfDate.setText(object.getString("jf_date").trim());
                                    p_jfDateStop.setText(object.getString("jf_datestop"));
                                    p_jfOrganizer.setText(object.getString("jf_organizer"));
                                    p_jfLocation.setText(object.getString("jf_location").trim());
                                    p_jfDescription.setText(object.getString("jf_description").trim());
                                    p_jfAttendees.setText(object.getString("jf_pattendees").trim());
                                }
                            } else {
                                Toast.makeText(p_JobFair_u.this, "Database error!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(p_JobFair_u.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(p_JobFair_u.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
