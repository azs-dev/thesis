package com.example.myapplication.Employer;

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
import com.example.myapplication.Jobfair;
import com.example.myapplication.JobfairListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class e_JobFairP extends AppCompatActivity {

    private String eStatURL = "http://192.168.1.9/dole_php/e_stat_jobfair.php";
    private String eStatURL2 = "http://192.168.1.9/dole_php/e_stat_listview.php";
    TextView pTotalJs, pLocal, pOverseas, pFemale, pMale, pHotS
            ,pQualified, pNotQualified, pNearHire, pUndefined;

    final ArrayList<String> jobseekerId = new ArrayList<String>();
    final ArrayList<Jobfair> jobseeker = new ArrayList<>();

    ListView jobseekeerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_job_fair_p);
        Intent intent = getIntent();
        final String jfId = intent.getStringExtra("jobfairId");
        final String eId = intent.getStringExtra("e_id");

        setTitle("Previous: " +intent.getStringExtra("jobfairName"));

        pTotalJs = findViewById(R.id.pTotalJs);
        pLocal = findViewById(R.id.pLocal);
        pOverseas = findViewById(R.id.pOverseas);
        pFemale = findViewById(R.id.pFemale);
        pMale = findViewById(R.id.pMale);
        pHotS = findViewById(R.id.pHotS);
        pQualified = findViewById(R.id.pQualified);
        pNotQualified = findViewById(R.id.pNotQualified);
        pNearHire = findViewById(R.id.pNearHire);
        pUndefined = findViewById(R.id.pUndefined);

        final ProgressBar progressBar = findViewById(R.id.progressBar3);

        jobseekeerList = findViewById(R.id.jobseekerList);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, eStatURL,
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
                                pTotalJs.setText("Jobseeker Scanned: " + headcount);

                                for (int i = 0; i < jsonArrayLoc.length(); i++) {
                                    JSONObject object = jsonArrayLoc.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String location = object.getString("location").trim();
                                        String count = object.getString("count").trim();
                                        if (location.equals("Local")) {
                                            pLocal.setText("Local: " + count);
                                        } else if (location.equals("Overseas")){
                                            pOverseas.setText("Overseas: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayGender.length(); i++) {
                                    JSONObject object = jsonArrayGender.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("gender").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("Male")) {
                                            pMale.setText("Male: " + count);
                                        } else if (gender.equals("Female")){
                                            pFemale.setText("Female: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayHiring.length(); i++) {
                                    JSONObject object = jsonArrayHiring.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("status").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("HotS")) {
                                            pHotS.setText("HotS: " + count);
                                        } else if (gender.equals("Near-Hire")){
                                            pNearHire.setText("Near-Hire: " + count);
                                        } else if (gender.equals("Not Qualified")){
                                            pNotQualified.setText("Not Qualified: " + count);
                                        } else if (gender.equals("Qualified")){
                                            pQualified.setText("Qualified: " + count);
                                        } else if (gender.equals("Interviewed")){
                                            pUndefined.setText("Interviewed: " + count);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(e_JobFairP.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                            pTotalJs.setText("ERROR!!");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobFairP.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                        pTotalJs.setText("ERROR!!");
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
        RequestQueue requestQueue = Volley.newRequestQueue(e_JobFairP.this);
        requestQueue.add(stringRequest);


        StringRequest request = new StringRequest(Request.Method.POST, eStatURL2,
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
                                JobfairListAdapter adapter = new JobfairListAdapter(e_JobFairP.this, R.layout.jf_adapter_view_layout,
                                        jobseeker);
                                jobseekeerList.setAdapter(adapter);
                                jobseekeerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                            Toast.makeText(e_JobFairP.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobFairP.this, "Error! " + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(e_JobFairP.this);
        queue.add(request);
    }
}
