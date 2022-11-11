package com.example.myapplication.JobSeeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class js_EmployerList extends AppCompatActivity {

    String employersURL = "http://192.168.1.9/dole_php/js_employer_list.php";
    private final ArrayList<Jobfair> employerList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_employer_list);
        Intent intent = getIntent();
        final String jfId = intent.getStringExtra("jobfairId");
        String jfName = intent.getStringExtra("jobfairName");
        final ListView employerLv = findViewById(R.id.employerList);
        final TextView textView = findViewById(R.id.eListText);
        final TextView label1 = findViewById(R.id.label20);
        final TextView label2 = findViewById(R.id.label21);
        final TextView noEmployerText = findViewById(R.id.noEmployerText);
        final ProgressBar progressBar = findViewById(R.id.eListProgressBar);
        final ImageView logo = findViewById(R.id.doleLogo);

        SwipeRefreshLayout refresh = findViewById(R.id.refresh);

        setTitle("List of Employers");

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });


        textView.setText("Employers for the upcoming jobfair: " + jfName);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, employersURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("employers");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
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
                                JobfairListAdapter jobfairListAdapter = new JobfairListAdapter(js_EmployerList.this,R.layout.jf_adapter_view_layout,
                                        employerList);
                                employerLv.setAdapter(jobfairListAdapter);
                                employerLv.setVisibility(View.VISIBLE);
                                label1.setVisibility(View.VISIBLE);
                                label2.setVisibility(View.VISIBLE);
                                logo.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                label1.setVisibility(View.GONE);
                                label2.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                noEmployerText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(js_EmployerList.this, "Database Error! " + error, Toast.LENGTH_LONG).show();
                    }
                }) {
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
