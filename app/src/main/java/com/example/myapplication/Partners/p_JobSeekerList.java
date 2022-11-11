package com.example.myapplication.Partners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class p_JobSeekerList extends AppCompatActivity {

    final ArrayList<String> jobseekerList = new ArrayList<String>();
    private ListView jobseekerLv;

    private String jslistURL = "http://192.168.1.9/dole_php/p_o_jslist.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_job_seeker_list);

        Intent intent = getIntent();
        final String pId = intent.getStringExtra("partnerId");
        final String jfId = intent.getStringExtra("jobfairId");

        final ArrayList<String> jobseekerId = new ArrayList<String>();
        final ArrayList<Employers> jobseeker = new ArrayList<>();

        final TextView pJobseekerScanned = findViewById(R.id.pJobseekerScanned);

        jobseekerLv = findViewById(R.id.pjobseekerLv);
        setTitle("Job Seekers List");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jslistURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String headcount = jsonObject.getString("p_scanned");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobseeker");
                            String totalheadcount = "Total Scanned: " + headcount;

                            SpannableString ss = new SpannableString(totalheadcount);
                            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
                            ss.setSpan(fcs,15,totalheadcount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            pJobseekerScanned.setText(ss);

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String jsId = object.getString("js_id").trim();
                                        jobseekerId.add(jsId);

                                        String jsfName = object.getString("js_first_name").trim();
                                        String jslName = object.getString("js_last_name").trim();
                                        String fullname = jsfName + " " + jslName;
                                        String gender = object.getString("js_gender");

                                        Employers jobseekers = new Employers(fullname, gender);
                                        jobseeker.add(jobseekers);

                                    }
                                }
                                EmployerListAdapter employerListAdapter = new EmployerListAdapter(p_JobSeekerList.this, R.layout.a_adapter_employerlist,
                                        jobseeker);
                                jobseekerLv.setAdapter(employerListAdapter);
                                jobseekerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                //ONCLICK
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(p_JobSeekerList.this, "Error! e" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(p_JobSeekerList.this, "Error! er" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("partnerId", pId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(p_JobSeekerList.this);
        requestQueue.add(stringRequest);

    }
}
