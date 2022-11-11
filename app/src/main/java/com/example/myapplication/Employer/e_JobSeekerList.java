package com.example.myapplication.Employer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class e_JobSeekerList extends AppCompatActivity {

    final ArrayList<String> jobseekerList = new ArrayList<String>();
    private ListView jobseekerLv;

    private TextView eOJobseekerTotal, eOJobseekerLocal, eOJobseekerOverseas, eOJobseekerMale, eOJobseekerFemale, eOJobseekerHots,
            eOJobseekerQualified, eOJobseekerNearHire, eOJobseekerNotQualified, eOJobseekerUndefined;

    private String jslistURL = "http://192.168.1.9/dole_php/e_o_jslist.php";
    private String eStatURL = "http://192.168.1.9/dole_php/e_stat_jobfair.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_jobseeker_list);
        Intent intent = getIntent();
        final String eId = intent.getStringExtra("employerId");
        final String jfId = intent.getStringExtra("jobfairId");

        eOJobseekerTotal = findViewById(R.id.eOJobseekerTotal);
        eOJobseekerLocal = findViewById(R.id.eOJobseekerLocal);
        eOJobseekerOverseas = findViewById(R.id.eOJobseekerOverseas);
        eOJobseekerMale = findViewById(R.id.eOJobseekerMale);
        eOJobseekerFemale = findViewById(R.id.eOJobseekerFemale);
        eOJobseekerHots = findViewById(R.id.eOJobseekerHots);
        eOJobseekerQualified = findViewById(R.id.eOJobseekerQualified);
        eOJobseekerNearHire = findViewById(R.id.eOJobseekerNearHire);
        eOJobseekerNotQualified = findViewById(R.id.eOJobseekerNotQualified);
        eOJobseekerUndefined = findViewById(R.id.eOJobseekerUndefined);

        final ProgressBar progressBar = findViewById(R.id.eOJobseekerProgress);

        final ArrayList<String> jobseekerId = new ArrayList<String>();
        final ArrayList<Jobfair> jobseeker = new ArrayList<>();

        jobseekerLv = findViewById(R.id.jobseekerLv);
        setTitle("Job Seekers List");

        StringRequest request = new StringRequest(Request.Method.POST, eStatURL,
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
                                eOJobseekerTotal.setText("Jobseeker Scanned: " + headcount);

                                for (int i = 0; i < jsonArrayLoc.length(); i++) {
                                    JSONObject object = jsonArrayLoc.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String location = object.getString("location").trim();
                                        String count = object.getString("count").trim();
                                        if (location.equals("Local")) {
                                            eOJobseekerLocal.setText("Local: " + count);
                                        } else if (location.equals("Overseas")){
                                            eOJobseekerOverseas.setText("Overseas: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayGender.length(); i++) {
                                    JSONObject object = jsonArrayGender.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("gender").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("Male")) {
                                            eOJobseekerMale.setText("Male: " + count);
                                        } else if (gender.equals("Female")){
                                            eOJobseekerFemale.setText("Female: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayHiring.length(); i++) {
                                    JSONObject object = jsonArrayHiring.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("status").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("HotS (Hired on the Spot)")) {
                                            eOJobseekerHots.setText("HotS: " + count);
                                        } else if (gender.equals("Near-Hire")){
                                            eOJobseekerNearHire.setText("Near-Hire: " + count);
                                        } else if (gender.equals("Not Qualified")){
                                            eOJobseekerNotQualified.setText("Not Qualified: " + count);
                                        } else if (gender.equals("Qualified")){
                                            eOJobseekerQualified.setText("Qualified: " + count);
                                        } else if (gender.equals("Interviewed")){
                                            eOJobseekerUndefined.setText("Interviewed: " + count);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(e_JobSeekerList.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                            eOJobseekerTotal.setText("ERROR!!");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobSeekerList.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                        eOJobseekerTotal.setText("ERROR!!");
                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employerId", eId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(e_JobSeekerList.this);
        requestQueue.add(request);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jslistURL,
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
                                        String fullname = jsfName + " " + jslName;
                                        String hiring = object.getString("hiring_status");
                                        String location = object.getString("jv_location");
                                        Jobfair jobfair = new Jobfair(fullname,location,hiring);
                                        jobseeker.add(jobfair);

                                    }
                                }
                                JobfairListAdapter adapter = new JobfairListAdapter(e_JobSeekerList.this, R.layout.jf_adapter_view_layout,
                                        jobseeker);
                                jobseekerLv.setAdapter(adapter);
                                jobseekerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < jobseeker.size(); i++) {
                                            if (position == i) {
                                                JobseekerDialog jobseekerDialog = new JobseekerDialog();
                                                Bundle bundle = new Bundle();

                                                bundle.putString("js_id", jobseekerId.get(i));
                                                bundle.putString("e_id", eId);
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
                            Toast.makeText(e_JobSeekerList.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobSeekerList.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employerId", eId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(e_JobSeekerList.this);
        requestQueue1.add(stringRequest);

    }
}
