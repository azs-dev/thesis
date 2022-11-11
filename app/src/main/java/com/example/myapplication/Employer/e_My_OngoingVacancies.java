package com.example.myapplication.Employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class e_My_OngoingVacancies extends AppCompatActivity {

    String o_vacanciesURL = "http://192.168.1.9/dole_php/e_o_my_vacancies.php";
    private String jfId, eId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_my_ongoing_vacancies);
        Intent intent = getIntent();
        jfId = intent.getStringExtra("jobfairId");
        eId = intent.getStringExtra("employerId");
        final Button addvacancyBtn = findViewById(R.id.addvacancyBtn);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshlayout);
        final ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        final ListView o_vacanciesList = findViewById(R.id.o_vacanciesList);

        setTitle("My Vacancies");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });
        addvacancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, o_vacanciesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("vacancies");
                            final ArrayList<String> jobvacancyTitle = new ArrayList<String>();
                            final ArrayList<String> jobvacancyId = new ArrayList<String>();
                            final ArrayList<Jobfair> jobvacancy = new ArrayList<>();

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        String jvId = object.getString("jv_id").trim();
                                        jobvacancyId.add(jvId);
                                        String jvTitle = object.getString("jv_title").trim();
                                        String jvLocation = object.getString("jv_location").trim();
                                        String jvCount = object.getString("jv_vacancy_count").trim();
                                        jobvacancyTitle.add(jvTitle);
                                        Jobfair jobfair = new Jobfair(jvTitle,jvLocation,"Vacancies: " + jvCount);
                                        jobvacancy.add(jobfair);
                                    }
                                }
                                JobfairListAdapter adapter = new JobfairListAdapter(e_My_OngoingVacancies.this,R.layout.jf_adapter_view_layout,
                                        jobvacancy);
                                progressBar2.setVisibility(View.GONE);
                                o_vacanciesList.setVisibility(View.VISIBLE);
                                addvacancyBtn.setVisibility(View.VISIBLE);
                                o_vacanciesList.setAdapter(adapter);
                                o_vacanciesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                Toast.makeText(e_My_OngoingVacancies.this,"Empty!",Toast.LENGTH_SHORT).show();
                                progressBar2.setVisibility(View.GONE);
                                o_vacanciesList.setVisibility(View.VISIBLE);
                                addvacancyBtn.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(e_My_OngoingVacancies.this,"Error!" + e,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_My_OngoingVacancies.this,"Error!" + error,Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfId);
                params.put("employerId", eId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void openDialog(){
        VacancyDialog vacancyDialog = new VacancyDialog();
        Bundle bundle = new Bundle();

        bundle.putString("e_id", eId);
        bundle.putString("jf_id", jfId);
        vacancyDialog.setArguments(bundle);
        vacancyDialog.show(getSupportFragmentManager(),"Vacancy Dialog");
    }
}
