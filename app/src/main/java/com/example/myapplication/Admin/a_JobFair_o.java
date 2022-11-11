package com.example.myapplication.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.example.myapplication.Employers;
import com.example.myapplication.EmployerListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class a_JobFair_o extends AppCompatActivity {

    private  TextView aOngoingTotal, aOngoingLocal, aOngoingOverseas, aOngoingMale, aOngoingFemale,
            aOngoingHots, aOngoingNearHire, aOngoingQualified, aOngoingNotQualified, aOngoingUndefined
            , aOngoingScanned, a_starttime_o;

    private String statsURL = "http://192.168.1.9/dole_php/a_jobfair_o.php"; //stats
    private String retrieveURL = "http://192.168.1.9/dole_php/a_employer_list_o.php"; //listview
    private String stopURL = "http://192.168.1.9/dole_php/a_jobfair_stop.php"; //stop

    private ArrayList<Employers> employerArr = new ArrayList<>();
    private ArrayList<String> employerId = new ArrayList<>();
    private ArrayList<String> employerName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_job_fair_o);

        Intent intent = getIntent();
        final String jfId = intent.getStringExtra("jobfairId");
        setTitle("Ongoing: " + intent.getStringExtra("jobfairName"));

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                overridePendingTransition(0,0);
            }
        });

        final ProgressBar progressBar = findViewById(R.id.aOngoingProgress);

        aOngoingTotal = findViewById(R.id.aOngoingTotal);
        aOngoingLocal = findViewById(R.id.aOngoingLocal);
        aOngoingOverseas = findViewById(R.id.aOngoingOverseas);
        aOngoingMale = findViewById(R.id.aOngoingMale);
        aOngoingFemale = findViewById(R.id.aOngoingFemale);
        aOngoingHots = findViewById(R.id.aOngoingHots);
        aOngoingNearHire = findViewById(R.id.aOngoingNearHire);
        aOngoingQualified = findViewById(R.id.aOngoingQualified);
        aOngoingNotQualified = findViewById(R.id.aOngoingNotQualified);
        aOngoingUndefined = findViewById(R.id.aOngoingUndefined);
        aOngoingScanned = findViewById(R.id.aOngoingScanned);

        a_starttime_o = findViewById(R.id.a_starttime_o);

        final ListView listView = findViewById(R.id.aOngoingEList);
        Button stopBtn = findViewById(R.id.aStopBtn);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                final String sdt = df.format(new Date(System.currentTimeMillis()));

                AlertDialog.Builder builder = new AlertDialog.Builder(a_JobFair_o.this);
                builder.setMessage("Are you sure you want to start this Job Fair?");
                builder.setTitle("Confirm Action");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stopRequest = new StringRequest(Request.Method.POST, stopURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")) {
                                                Toast.makeText(getApplicationContext(), "Job Fair ended!", Toast.LENGTH_SHORT).show();
                                                finishAffinity();
                                                Intent intent1 = new Intent(a_JobFair_o.this, AdminHome.class);
                                                intent1.putExtra("a_name", "admin");
                                                startActivity(intent1);
                                                overridePendingTransition(0, 0);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                                finishAffinity();
                                                Intent intent1 = new Intent(a_JobFair_o.this, AdminHome.class);
                                                intent1.putExtra("a_name", "admin");
                                                startActivity(intent1);
                                                overridePendingTransition(0, 0);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(a_JobFair_o.this, "Error! er" + e, Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                            Intent intent1 = new Intent(a_JobFair_o.this, AdminHome.class);
                                            intent1.putExtra("a_name", "admin");
                                            startActivity(intent1);
                                            overridePendingTransition(0, 0);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(a_JobFair_o.this, "Error! er" + error, Toast.LENGTH_SHORT).show();
                                    }
                                })  {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("jobfairId", jfId);
                                params.put("time", sdt);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(a_JobFair_o.this);
                        requestQueue.add(stopRequest);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        StringRequest request = new StringRequest(Request.Method.POST, statsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String scanned = jsonObject.getString("scanned");
                            String time = jsonObject.getString("time");
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
                                aOngoingTotal.setText(ss);
                                aOngoingScanned.setText(ss1);
                                a_starttime_o.setText("Start Time: " + time);
                                for (int i = 0; i < jsonArrayLoc.length(); i++) {
                                    JSONObject object = jsonArrayLoc.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String location = object.getString("location").trim();
                                        String count = object.getString("count").trim();
                                        if (location.equals("Local")) {
                                            aOngoingLocal.setText("Local: " + count);
                                        } else if (location.equals("Overseas")){
                                            aOngoingOverseas.setText("Overseas: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayGender.length(); i++) {
                                    JSONObject object = jsonArrayGender.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("gender").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("Male")) {
                                            aOngoingMale.setText("Male: " + count);
                                        } else if (gender.equals("Female")){
                                            aOngoingFemale.setText("Female: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayHiring.length(); i++) {
                                    JSONObject object = jsonArrayHiring.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("status").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("HotS")) {
                                            aOngoingHots.setText("HotS: " + count);
                                        } else if (gender.equals("Near-Hire")){
                                            aOngoingNearHire.setText("Near-Hire: " + count);
                                        } else if (gender.equals("Not Qualified")){
                                            aOngoingNotQualified.setText("Not Qualified: " + count);
                                        } else if (gender.equals("Qualified")){
                                            aOngoingQualified.setText("Qualified: " + count);
                                        } else if (gender.equals("Interviewed")){
                                            aOngoingUndefined.setText("Interviewed: " + count);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(a_JobFair_o.this, "Error! e " + e, Toast.LENGTH_SHORT).show();
                            aOngoingTotal.setText("ERROR!");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a_JobFair_o.this, "Error! er " + error, Toast.LENGTH_SHORT).show();
                        aOngoingTotal.setText("ERROR!");
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

        RequestQueue queue = Volley.newRequestQueue(a_JobFair_o.this);
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
                                EmployerListAdapter employerListAdapter = new EmployerListAdapter(a_JobFair_o.this, R.layout.a_adapter_employerlist,
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
                            Toast.makeText(a_JobFair_o.this, "Error! e" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a_JobFair_o.this, "Error! er" + error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(a_JobFair_o.this);
        requestQueue.add(stringRequest);
    }
}
