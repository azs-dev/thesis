package com.example.myapplication.JobSeeker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class js_JobFair_u extends AppCompatActivity {

    Button goingBtn, notgBtn, eListBtn;
    String decision;
    String jobfairURL = "http://192.168.1.9/dole_php/js_going_count.php";
    String attendancecheckURL = "http://192.168.1.9/dole_php/js_going_check.php";
    String jfDataURL = "http://192.168.1.9/dole_php/js_jobfair_u.php";

    private TextView jfName,jfDate,jfLocation,jfDesc,jfDateStop,jfOrganizer;
    private String jfName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_jobfair_u);

        jfName = findViewById(R.id.js_jfName);
        jfDate = findViewById(R.id.js_jfDate);
        jfLocation = findViewById(R.id.js_jfLocation);
        jfDesc = findViewById(R.id.js_jfDescription);
        jfDateStop = findViewById(R.id.js_jfDateStop);
        jfOrganizer = findViewById(R.id.js_jfOrganizer);
        goingBtn = findViewById(R.id.goingBtn);
        notgBtn = findViewById(R.id.notgBtn);
        eListBtn = findViewById(R.id.eListBtn);

        Intent intent = getIntent();
        final String intentName = intent.getStringExtra("jobfairName");
        final String jobfairId = intent.getStringExtra("jobfairId");
        final String jsId = intent.getStringExtra("js_id");

        setTitle("Upcoming: " + intentName);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jfDataURL,
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
                                    jfName1 = object.getString("jf_name").trim();
                                    setTitle("Upcoming: " + jfName1);
                                    jfName.setText(object.getString("jf_name").trim());
                                    jfDate.setText(object.getString("jf_date").trim());
                                    jfDateStop.setText(object.getString("jf_datestop"));
                                    jfOrganizer.setText(object.getString("jf_organizer"));
                                    jfLocation.setText(object.getString("jf_location").trim());
                                    jfDesc.setText(object.getString("jf_description").trim());

                                }
                            } else {
                                Toast.makeText(js_JobFair_u.this, "Database error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(js_JobFair_u.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jobfairId);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(js_JobFair_u.this);
        queue.add(stringRequest);

        StringRequest request = new StringRequest(Request.Method.POST, attendancecheckURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")){

                                if (message.equals("not going")){
                                    goingBtn.setVisibility(View.VISIBLE);
                                    notgBtn.setVisibility(View.GONE);
                                } else if (message.equals("going")) {
                                    goingBtn.setVisibility(View.GONE);
                                    notgBtn.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(js_JobFair_u.this,"Error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(js_JobFair_u.this,"Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(js_JobFair_u.this,"Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jfId", jobfairId);
                params.put("jsId", jsId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(js_JobFair_u.this);
        requestQueue.add(request);

        eListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(js_JobFair_u.this, js_EmployerList.class);
                intent.putExtra("jobfairId", jobfairId);
                intent.putExtra("jobfairName", intentName);
                startActivity(intent);
            }
        });


        goingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingBtn.setVisibility(View.GONE);
                notgBtn.setVisibility(View.VISIBLE);
                decision = "plus";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    if (success.equals("1")) {
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(js_JobFair_u.this, "Registration error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(js_JobFair_u.this, "Registration error! " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("jfId", jobfairId);
                        params.put("decision", decision);
                        params.put("jsId", jsId);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(js_JobFair_u.this);
                requestQueue.add(stringRequest);
            }
        });

        notgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(js_JobFair_u.this);
                builder.setMessage("Are you sure to cancel your attendance?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goingBtn.setVisibility(View.VISIBLE);
                                notgBtn.setVisibility(View.GONE);
                                decision = "minus";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String success = jsonObject.getString("success");

                                                    if (success.equals("1")) {
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(js_JobFair_u.this, "Registration error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(js_JobFair_u.this, "Registration error! " + error.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("jfId", jobfairId);
                                        params.put("decision", decision);
                                        params.put("jsId", jsId);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(js_JobFair_u.this);
                                requestQueue.add(stringRequest);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Confirm Action");
                alert.show();
            }
        });
    }

}

