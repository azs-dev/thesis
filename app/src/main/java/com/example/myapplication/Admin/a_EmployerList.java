package com.example.myapplication.Admin;

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

public class a_EmployerList extends AppCompatActivity {

    String employersURL = "http://192.168.1.9/dole_php/a_employer_list_u.php";
    private final ArrayList<Jobfair> employerList = new ArrayList();
    private final ArrayList<String> employerId = new ArrayList();

    private String jfId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_employer_list);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                overridePendingTransition(0,0);
            }
        });

        Intent intent = getIntent();
        jfId = intent.getStringExtra("jobfairId");
        String jfName = intent.getStringExtra("jobfairName");
        setTitle("List of Employers");

        final ListView listView = findViewById(R.id.a_eList);
        final ProgressBar a_eProgressBar = findViewById(R.id.a_eProgressBar);
        Button addEBtn = findViewById(R.id.addEBtn);
        TextView textView = findViewById(R.id.a_elistText);

        textView.setText("Employers for the upcoming jobfair: " + jfName);

        addEBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEmployerDialog addEmployerDialog = new AddEmployerDialog();
                Bundle bundle = new Bundle();

                bundle.putString("jf_id", jfId);
                addEmployerDialog.setArguments(bundle);
                addEmployerDialog.show(getSupportFragmentManager(),"Employer Dialog");
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, employersURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("employers");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String eId = object.getString("employer_id").trim();
                                        String eName = object.getString("e_cname").trim();
                                        String eEmail = object.getString("e_email").trim();
                                        String eNumber = object.getString("e_cnumber").trim();

                                        Jobfair employer = new Jobfair(eName,eEmail,eNumber);
                                        employerList.add(employer);
                                        employerId.add(eId);
                                    }
                                }
                                JobfairListAdapter jobfairListAdapter = new JobfairListAdapter(a_EmployerList.this,R.layout.jf_adapter_view_layout,
                                        employerList);
                                listView.setAdapter(jobfairListAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < employerList.size(); i++) {
                                            if (position == i) {
                                                EmployerListDialog employerListDialog = new EmployerListDialog();
                                                Bundle bundle = new Bundle();

                                                bundle.putString("employerId", employerId.get(i));
                                                bundle.putString("jobfairId", jfId);
                                                employerListDialog.setArguments(bundle);
                                                employerListDialog.show(getSupportFragmentManager(),"Employer Dialog");
                                            }
                                        }
                                    }
                                });
                                a_eProgressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(a_EmployerList.this, "No employers to show.", Toast.LENGTH_SHORT).show();
                                a_eProgressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(a_EmployerList.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a_EmployerList.this, "Error! " + error, Toast.LENGTH_SHORT).show();
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
