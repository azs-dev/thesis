package com.example.myapplication.Employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class e_MyVacancies extends AppCompatActivity {

    String myvacanciesURL = "http://192.168.1.9/dole_php/e_my_vacancies.php";

    ArrayList<Jobfair> jobVacancies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_my_vacancies);
        Intent intent = getIntent();
        final String jobfairId = intent.getStringExtra("jobfairId");
        final String employerId = intent.getStringExtra("employerId");
        String jobfairName = intent.getStringExtra("jobfairName");
        SwipeRefreshLayout swiperefresh = findViewById(R.id.swiperefresh);

        TextView myvacanciesText = findViewById(R.id.myvacanciesText);
        myvacanciesText.setText("Below are the List of Vacancies you posted for job fair: " + jobfairName);
        final ListView vacanciesList = findViewById(R.id.vacanciesList);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myvacanciesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("vacancies");
                            final ArrayList<String> jobvacancyTitle = new ArrayList<String>();
                            final ArrayList<String> jobvacancyId = new ArrayList<String>();

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        String jvId = object.getString("jv_id").trim();
                                        jobvacancyId.add(jvId);
                                        String jvTitle = object.getString("jv_title").trim();
                                        jobvacancyTitle.add(jvTitle);
                                        String jvLocation = object.getString("jv_location").trim();
                                        String jvCount = object.getString("jv_count").trim();

                                        Jobfair jobfair = new Jobfair(jvTitle, jvLocation, jvCount);
                                        jobVacancies.add(jobfair);
                                    }
                                }
                                JobfairListAdapter jobfairListAdapter = new JobfairListAdapter(e_MyVacancies.this,R.layout.jf_adapter_view_layout,
                                        jobVacancies);
                                vacanciesList.setAdapter(jobfairListAdapter);
                                vacanciesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < jobvacancyTitle.size(); i++) {
                                            if (position == i) {
                                                MyVacancyDialog myVacancyDialog = new MyVacancyDialog();
                                                Bundle bundle = new Bundle();

                                                bundle.putString("jv_id", jobvacancyId.get(i));
                                                myVacancyDialog.setArguments(bundle);
                                                myVacancyDialog.show(getSupportFragmentManager(),"My Vacancy Dialog");
                                            }
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(e_MyVacancies.this,"Empty!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(e_MyVacancies.this,"Database error! " + error,Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jobfairId);
                params.put("employerId", employerId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
