package com.example.myapplication.JobSeeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.myapplication.R;
import com.example.myapplication.Vacancy;
import com.example.myapplication.VacancyListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class js_MatchMe extends AppCompatActivity {

    String matchURL = "http://192.168.1.9/dole_php/MATCH_ALGOTEST.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_matchme);
        final TextView jfName = findViewById(R.id.jfName);
        final Button matchBtn = findViewById(R.id.matchBtn);
        final ListView resultList = findViewById(R.id.resultList);
        final TextView noMatchText = findViewById(R.id.noMatchText);
        final TextView matchedText = findViewById(R.id.matchedText);
        final TextView label1 = findViewById(R.id.label18);
        final TextView label2 = findViewById(R.id.label19);
        final ImageView imageView = findViewById(R.id.dole_logo_match);
        final TextView matchProgressText = findViewById(R.id.matchProgressText);
        final ProgressBar matchProgressBar = findViewById(R.id.matchProgressBar);

        Intent intent = getIntent();
        final String extraName = intent.getStringExtra("jobfairName");
        final String jobfairId = intent.getStringExtra("jobfairId");
        final String jobseekerId = intent.getStringExtra("jobseekerId");
        jfName.setText(extraName);


        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchedText.setVisibility(GONE);
                noMatchText.setVisibility(GONE);
                imageView.setVisibility(GONE);
                resultList.setVisibility(GONE);
                label1.setVisibility(GONE);
                label2.setVisibility(GONE);
                matchProgressBar.setVisibility(View.VISIBLE);
                matchProgressText.setVisibility(View.VISIBLE);
                final ArrayList<String> jobVacancyName = new ArrayList<String>();
                final ArrayList<String> jobVacancyEmployer = new ArrayList<String>();
                final ArrayList<String> jobVacancyLoc = new ArrayList<String>();
                final ArrayList<String> jobVacancyId = new ArrayList<String>();
                final ArrayList<Vacancy> vacancyList = new ArrayList<>();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, matchURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String userdata_success = jsonObject.getString("userdata-success");
                                    JSONArray jsonArray = jsonObject.getJSONArray("vacancies");

                                    if (userdata_success.equals("1")) {
                                        for (int i = 0; i < jsonArray.length(); i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            for (int y=0;y<1;y++){
                                                String jvName = object.getString("jv_title").trim();
                                                jobVacancyName.add(jvName);
                                                String jvEmployer = object.getString("e_cname").trim();
                                                jobVacancyEmployer.add(jvEmployer);
                                                String jvLoc = object.getString("jv_location").trim();
                                                jobVacancyLoc.add(jvLoc);
                                                String jvId = object.getString("jv_id").trim();
                                                String matchedby = object.getString("matchedby").trim();
                                                jobVacancyId.add(jvId);
                                                Vacancy vacancy = new Vacancy(jvEmployer,jvName,"Matched by: " + matchedby);
                                                vacancyList.add(vacancy);
                                            }
                                        }

                                        if (!jobVacancyName.isEmpty()){

                                            VacancyListAdapter adapter = new VacancyListAdapter(js_MatchMe.this, R.layout.jv_adapter_view_layout,vacancyList);
                                            resultList.setAdapter(adapter);
                                            resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    MatchDialog matchDialog = new MatchDialog();
                                                    Bundle bundle = new Bundle();
                                                    String jvId = jobVacancyId.get(position);

                                                    bundle.putString("jv_id", jvId);
                                                    matchDialog.setArguments(bundle);
                                                    matchDialog.show(getSupportFragmentManager(),"Vacancy Dialog");
                                                }
                                            });

                                            matchProgressBar.setVisibility(GONE);
                                            matchProgressText.setVisibility(GONE);
                                            resultList.setVisibility(View.VISIBLE);
                                            label1.setVisibility(View.VISIBLE);
                                            label2.setVisibility(View.VISIBLE);
                                            matchedText.setText("Listed below are the vacancies under " + extraName +" that we think fit you the best!");
                                            matchedText.setVisibility(View.VISIBLE);
                                        } else {
                                            label1.setVisibility(GONE);
                                            label2.setVisibility(GONE);
                                            resultList.setVisibility(GONE);
                                            noMatchText.setVisibility(View.VISIBLE);
                                            imageView.setVisibility(View.VISIBLE);
                                            matchProgressBar.setVisibility(GONE);
                                            matchProgressText.setVisibility(GONE);
                                        }
                                    } else {
                                        Toast.makeText(js_MatchMe.this,"Database Error!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(js_MatchMe.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("js_id", jobseekerId);
                        params.put("jf_id", jobfairId);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(js_MatchMe.this);
                requestQueue.add(stringRequest);
            }
        });

    }
}
