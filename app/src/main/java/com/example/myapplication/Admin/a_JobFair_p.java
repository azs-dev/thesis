package com.example.myapplication.Admin;

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

public class a_JobFair_p extends AppCompatActivity {

    private TextView aPreviousTotal, aPreviousLocal, aPreviousOverseas, aPreviousMale, aPreviousFemale,
            aPreviousHots, aPreviousNearHire, aPreviousQualified, aPreviousNotQualified, aPreviousUndefined
            , aPreviousScanned, a_starttime_p, a_stoptime_p;

    private String statsURL = "http://192.168.1.9/dole_php/a_jobfair_p.php"; //stats
    private String retrieveURL = "http://192.168.1.9/dole_php/a_employer_list_p.php"; //listview

    private ArrayList<Employers> employerArr = new ArrayList<>();
    private ArrayList<String> employerId = new ArrayList<>();
    private ArrayList<String> employerName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_jobfair_p);

        Intent intent = getIntent();
        final String jfId = intent.getStringExtra("jobfairId");
        setTitle("Previous: " + intent.getStringExtra("jobfairName"));

        final ProgressBar progressBar = findViewById(R.id.aPreviousProgress);

        aPreviousTotal = findViewById(R.id.aPreviousTotal);
        aPreviousLocal = findViewById(R.id.aPreviousLocal);
        aPreviousOverseas = findViewById(R.id.aPreviousOverseas);
        aPreviousMale = findViewById(R.id.aPreviousMale);
        aPreviousFemale = findViewById(R.id.aPreviousFemale);
        aPreviousHots = findViewById(R.id.aPreviousHots);
        aPreviousNearHire = findViewById(R.id.aPreviousNearHire);
        aPreviousQualified = findViewById(R.id.aPreviousQualified);
        aPreviousNotQualified = findViewById(R.id.aPreviousNotQualified);
        aPreviousUndefined = findViewById(R.id.aPreviousUndefined);
        aPreviousScanned = findViewById(R.id.aPreviousScanned);

        a_starttime_p = findViewById(R.id.a_starttime_p);
        a_stoptime_p = findViewById(R.id.a_stoptime_p);
        
        final ListView listView = findViewById(R.id.aPreviousEList);

        StringRequest request = new StringRequest(Request.Method.POST, statsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String scanned = jsonObject.getString("scanned");
                            String starttime = jsonObject.getString("start_time");
                            String stoptime = jsonObject.getString("stop_time");
                            String registered = jsonObject.getString("registered");
                            JSONArray jsonArrayLoc = jsonObject.getJSONArray("location");
                            JSONArray jsonArrayGender = jsonObject.getJSONArray("gender");
                            JSONArray jsonArrayHiring = jsonObject.getJSONArray("hiring");

                            String finalregistered = "Total Registrants: " + registered;
                            String finalscanned = "Total Scanned: " + scanned;
                            SpannableString ss1 = new SpannableString(finalscanned);
                            SpannableString ss = new SpannableString(finalregistered);
                            ForegroundColorSpan fcs1 = new ForegroundColorSpan(Color.BLUE);
                            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
                            ss1.setSpan(fcs1, 15, finalscanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ss.setSpan(fcs, 19, finalregistered.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                            if (success.equals("1")){
                                aPreviousTotal.setText(ss);
                                aPreviousScanned.setText(ss1);

                                a_starttime_p.setText("Start Time: " + starttime);
                                a_stoptime_p.setText("Stop Time: " + stoptime);

                                for (int i = 0; i < jsonArrayLoc.length(); i++) {
                                    JSONObject object = jsonArrayLoc.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String location = object.getString("location").trim();
                                        String count = object.getString("count").trim();
                                        if (location.equals("Local")) {
                                            aPreviousLocal.setText("Local: " + count);
                                        } else if (location.equals("Overseas")){
                                            aPreviousOverseas.setText("Overseas: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayGender.length(); i++) {
                                    JSONObject object = jsonArrayGender.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("gender").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("Male")) {
                                            aPreviousMale.setText("Male: " + count);
                                        } else if (gender.equals("Female")){
                                            aPreviousFemale.setText("Female: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayHiring.length(); i++) {
                                    JSONObject object = jsonArrayHiring.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("status").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("HotS (Hired on the Spot)")) {
                                            aPreviousHots.setText("HotS: " + count);
                                        } else if (gender.equals("Near-Hire")){
                                            aPreviousNearHire.setText("Near-Hire: " + count);
                                        } else if (gender.equals("Not Qualified")){
                                            aPreviousNotQualified.setText("Not Qualified: " + count);
                                        } else if (gender.equals("Qualified")){
                                            aPreviousQualified.setText("Qualified: " + count);
                                        } else if (gender.equals("Interviewed")){
                                            aPreviousUndefined.setText("Interviewed: " + count);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(a_JobFair_p.this, "Error! e " + e, Toast.LENGTH_SHORT).show();
                            aPreviousTotal.setText("ERROR!");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a_JobFair_p.this, "Error! er " + error, Toast.LENGTH_SHORT).show();
                        aPreviousTotal.setText("ERROR!");
                        progressBar.setVisibility(View.GONE);
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
        RequestQueue queue = Volley.newRequestQueue(a_JobFair_p.this);
        queue.add(request);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, retrieveURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("employers");

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        String eId = object.getString("employer_id").trim();
                                        String eName = object.getString("e_cname").trim();
                                        String count = object.getString("jobseeker_count").trim();

                                        Employers employer = new Employers(eName,"count: " + count);
                                        employerName.add(eName);
                                        employerArr.add(employer);
                                        employerId.add(eId);
                                    }
                                }
                                EmployerListAdapter employerListAdapter = new EmployerListAdapter(a_JobFair_p.this, R.layout.a_adapter_employerlist,
                                        employerArr);
                                listView.setAdapter(employerListAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < employerArr.size(); i++) {
                                            if (position == i) {
                                                EmployerDialog employerDialog = new EmployerDialog();
                                                Bundle bundle = new Bundle();

                                                bundle.putString("employerId", employerId.get(i));
                                                bundle.putString("employerName", employerName.get(i));
                                                bundle.putString("jobfairId", jfId);
                                                employerDialog.setArguments(bundle);
                                                employerDialog.show(getSupportFragmentManager(),"Employer Dialog");
                                            }
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(a_JobFair_p.this, "Error! e" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a_JobFair_p.this, "Error! er" + error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(a_JobFair_p.this);
        requestQueue.add(stringRequest);


    }
}
