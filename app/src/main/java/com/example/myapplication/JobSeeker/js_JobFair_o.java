package com.example.myapplication.JobSeeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Jobfair;
import com.example.myapplication.JobfairListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class js_JobFair_o extends AppCompatActivity {

    private TextView jsODescription, jsODescription2;

    String jsOngoingURL = "http://192.168.1.9/dole_php/js_o_employer_list.php";
    String jsOngoingURL2 = "http://192.168.1.9/dole_php/js_o_vacancy_list.php";

    String registeredcheckURL = "http://192.168.1.9/dole_php/js_going_check.php";
    String registeredURL = "http://192.168.1.9/dole_php/js_jobfair_register_o.php";
    private final ArrayList<Jobfair> employerList = new ArrayList();
    private final ArrayList<Jobfair> vacancyList = new ArrayList();
    private final ArrayList<String> vacancyId = new ArrayList();

    private TextView js_goingText_o;
    private Button js_goingBtn_o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_job_fair_o);

        jsODescription = findViewById(R.id.jsODescription);
        jsODescription2 = findViewById(R.id.jsODescription2);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        SwipeRefreshLayout refresh = findViewById(R.id.refresh);

        js_goingText_o = findViewById(R.id.js_goingText_o);
        js_goingBtn_o = findViewById(R.id.js_goingBtn_o);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });

        final ListView jsOList = findViewById(R.id.jsOList);
        final ListView jsOVList = findViewById(R.id.jsOVList);
        Intent intent = getIntent();
        String name = intent.getStringExtra("jobfairName");
        final String jfId = intent.getStringExtra("jobfairId");
        final String jsId = intent.getStringExtra("js_id");

        setTitle("Ongoing: " + name);

        StringRequest checkRequest = new StringRequest(Request.Method.POST, registeredcheckURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")){

                                if (message.equals("not going")){
                                    js_goingBtn_o.setVisibility(View.VISIBLE);
                                    js_goingText_o.setVisibility(View.VISIBLE);
                                } else if (message.equals("going")) {
                                    js_goingBtn_o.setVisibility(View.GONE);
                                    js_goingText_o.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(js_JobFair_o.this,"Error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(js_JobFair_o.this,"Error! " + e, Toast.LENGTH_SHORT).show();
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
                params.put("jfId", jfId);
                params.put("jsId", jsId);
                return params;
            }
        };
        RequestQueue checkQueue = Volley.newRequestQueue(js_JobFair_o.this);
        checkQueue.add(checkRequest);

        js_goingBtn_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, registeredURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");

                                    if (success.equals("1")) {
                                        Toast.makeText(js_JobFair_o.this, "Success!", Toast.LENGTH_SHORT).show();
                                        recreate();
                                    } else {
                                        Toast.makeText(js_JobFair_o.this, "Database Error!", Toast.LENGTH_SHORT).show();
                                        recreate();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(js_JobFair_o.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(js_JobFair_o.this, "Error! er" + error, Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("jfId", jfId);
                        params.put("jsId", jsId);
                        return params;
                    }
                };
                RequestQueue requestQueue1 = Volley.newRequestQueue(js_JobFair_o.this);
                requestQueue1.add(stringRequest3);
            }
        });

        jsODescription.setText("Listed below are the Employers who are present in the ongoing jobfair.");
        jsODescription2.setText("Available vacancies:");

        //for employers

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jsOngoingURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("employers");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String eName = object.getString("e_cname").trim();
                                        String eEmail = object.getString("e_email").trim();
                                        String eNumber = object.getString("e_cnumber").trim();
                                        Jobfair employer = new Jobfair(eName,eEmail,eNumber);
                                        employerList.add(employer);
                                    }
                                }
                                JobfairListAdapter jobfairListAdapter = new JobfairListAdapter(js_JobFair_o.this,R.layout.jf_adapter_view_layout,
                                        employerList);
                                jsOList.setAdapter(jobfairListAdapter);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(js_JobFair_o.this, "No Employers to show.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(js_JobFair_o.this, "Error! " + e,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(js_JobFair_o.this, "Error! " + error,Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(js_JobFair_o.this);
        requestQueue.add(stringRequest);

        //For vacancies

        StringRequest request = new StringRequest(Request.Method.POST, jsOngoingURL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("vacancies");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String title = object.getString("jv_title").trim();
                                        String employer = object.getString("e_cname").trim();
                                        String location = object.getString("jv_location").trim();
                                        Jobfair vacancy = new Jobfair(title,employer,location);
                                        vacancyList.add(vacancy);
                                        String id = object.getString("jv_id").trim();
                                        vacancyId.add(id);
                                    }
                                }
                                JobfairListAdapter adapter = new JobfairListAdapter(js_JobFair_o.this,R.layout.jf_adapter_view_layout,
                                        vacancyList);
                                jsOVList.setAdapter(adapter);
                                jsOVList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        MatchDialog matchDialog = new MatchDialog();
                                        Bundle bundle = new Bundle();
                                        String jvId = vacancyId.get(position);

                                        bundle.putString("jv_id", jvId);
                                        matchDialog.setArguments(bundle);
                                        matchDialog.show(getSupportFragmentManager(),"Vacancy Dialog");
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(js_JobFair_o.this, "No vacancies to show.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(js_JobFair_o.this, "Error! " + e,Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(js_JobFair_o.this, "Error! " + error,Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfId);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(js_JobFair_o.this);
        queue.add(request);


    }
}
