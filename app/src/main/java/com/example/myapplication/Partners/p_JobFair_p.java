package com.example.myapplication.Partners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.myapplication.EmployerListAdapter;
import com.example.myapplication.Employers;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class p_JobFair_p extends AppCompatActivity {

    private String statsURL = "http://192.168.1.9/dole_php/p_stat_jobfair.php"; //for stats
    private String listviewURL = "http://192.168.1.9/dole_php/p_stat_listview.php"; //for listview

    TextView pjTotalJs, pjLocal, pjOverseas, pjFemale, pjMale, pjHotS
            ,pjQualified, pjNotQualified, pjNearHire, pjUndefined;

    ListView pjJobseekerList;

    final ArrayList<String> jobseekerId = new ArrayList<String>();
    final ArrayList<Employers> jobseeker = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_jobfair_p);
        Intent intent = getIntent();
        final String jfName = intent.getStringExtra("jobfairName");
        final String jfId = intent.getStringExtra("jobfairId");
        final String pId = intent.getStringExtra("p_id");

        setTitle("Previous: " + jfName);

        pjTotalJs = findViewById(R.id.pjTotal);
        pjLocal = findViewById(R.id.pjLocal);
        pjOverseas = findViewById(R.id.pjOverseas);
        pjFemale = findViewById(R.id.pjFemale);
        pjMale = findViewById(R.id.pjMale);
        pjHotS = findViewById(R.id.pjHotS);
        pjQualified = findViewById(R.id.pjQualified);
        pjNotQualified = findViewById(R.id.pjNotQualified);
        pjNearHire = findViewById(R.id.pjNearHire);
        pjUndefined = findViewById(R.id.pjUndefined);

        final ProgressBar progressBar = findViewById(R.id.progressBar6);

        pjJobseekerList = findViewById(R.id.pjJobseekerList);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, statsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String headcount = jsonObject.getString("headcount");
                            JSONArray jsonArrayLoc = jsonObject.getJSONArray("location");
                            JSONArray jsonArrayGender = jsonObject.getJSONArray("gender");
                            JSONArray jsonArrayHiring = jsonObject.getJSONArray("hiring");

                            if (success.equals("1")){
                                pjTotalJs.setText("Total Headcount: " + headcount);

                                for (int i = 0; i < jsonArrayLoc.length(); i++) {
                                    JSONObject object = jsonArrayLoc.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String location = object.getString("location").trim();
                                        String count = object.getString("count").trim();
                                        if (location.equals("Local")) {
                                            pjLocal.setText("Local: " + count);
                                        } else if (location.equals("Overseas")){
                                            pjOverseas.setText("Overseas: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayGender.length(); i++) {
                                    JSONObject object = jsonArrayGender.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("gender").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("Male")) {
                                            pjMale.setText("Male: " + count);
                                        } else if (gender.equals("Female")){
                                            pjFemale.setText("Female: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayHiring.length(); i++) {
                                    JSONObject object = jsonArrayHiring.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("status").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("HotS (Hired on the Spot)")) {
                                            pjHotS.setText("HotS: " + count);
                                        } else if (gender.equals("Near-Hire")){
                                            pjNearHire.setText("Near-Hire: " + count);
                                        } else if (gender.equals("Not Qualified")){
                                            pjNotQualified.setText("Not Qualified: " + count);
                                        } else if (gender.equals("Qualified")){
                                            pjQualified.setText("Qualified: " + count);
                                        } else if (gender.equals("Interviewed")){
                                            pjUndefined.setText("Interviewed: " + count);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(p_JobFair_p.this, "Error! " + e , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(p_JobFair_p.this, "Error! " + error , Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("partnerId", pId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(p_JobFair_p.this);
        requestQueue.add(stringRequest);

        StringRequest request = new StringRequest(Request.Method.POST, listviewURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobseeker");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String jsId = object.getString("js_id").trim();
                                        jobseekerId.add(jsId);

                                        String jsfName = object.getString("js_first_name").trim();
                                        String jslName = object.getString("js_last_name").trim();
                                        String gender = object.getString("js_gender").trim();
                                        String fullname = jsfName + " " + jslName;

                                        Employers jobseekers = new Employers(fullname, gender);
                                        jobseeker.add(jobseekers);

                                    }
                                }
                                EmployerListAdapter employerListAdapter = new EmployerListAdapter(p_JobFair_p.this, R.layout.a_adapter_employerlist,
                                        jobseeker);
                                pjJobseekerList.setAdapter(employerListAdapter);
                                pjJobseekerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < jobseeker.size(); i++) {
                                            if (position == i) {
                                                pJobseekerDialog jobseekerDialog = new pJobseekerDialog();
                                                Bundle bundle = new Bundle();

                                                bundle.putString("js_id", jobseekerId.get(i));
                                                bundle.putString("p_id", pId);
                                                bundle.putString("jf_id", jfId);
                                                jobseekerDialog.setArguments(bundle);
                                                jobseekerDialog.show(getSupportFragmentManager(),"Jobseeker Dialog");
                                            }
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(p_JobFair_p.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(p_JobFair_p.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("partnerId", pId);
                params.put("jobfairId", jfId);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(p_JobFair_p.this);
        queue.add(request);

    }
}
