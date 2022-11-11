package com.example.myapplication.Employer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class e_JobFairU extends AppCompatActivity {

    private TextView jfName, jfLocation, jfDate, jfDesc,jfDateStop,jfOrganizer, clickText2, clickText3;
    private String jfName1;
    private Button postBtn, myVacanciesBtn;
    private String jfID, eID;

    String activationcheckURL = "http://192.168.1.9/dole_php/e_validcheck.php";
    String jobfairURL = "http://192.168.1.9/dole_php/e_jobfair_u.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_jobfair_u);

        jfName = findViewById(R.id.jfName);
        jfLocation = findViewById(R.id.jfLocation);
        jfDate = findViewById(R.id.jfDate);
        jfDesc = findViewById(R.id.jfDesc);
        postBtn = findViewById(R.id.postBtn);
        jfDateStop = findViewById(R.id.jfDateStop);
        jfOrganizer = findViewById(R.id.jfOrganizer);
        myVacanciesBtn = findViewById(R.id.myVacanciesBtn);
        clickText2 = findViewById(R.id.clickText2);
        clickText3 = findViewById(R.id.clickText3);
        final ProgressBar progressBar = findViewById(R.id.e_jfU_ProgressBar);

        Intent intent = getIntent();

        jfID = intent.getStringExtra("jobfairId");
        eID = intent.getStringExtra("e_id");
        setTitle("Upcoming: " + jfName.getText());

        final String jobfairName = intent.getStringExtra("jobfairName");

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        myVacanciesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(e_JobFairU.this, e_MyVacancies.class);
                intent.putExtra("jobfairId",jfID);
                intent.putExtra("employerId",eID);
                intent.putExtra("jobfairName",jobfairName);
                startActivity(intent);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, activationcheckURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("0")) {
                                clickText3.setVisibility(View.VISIBLE);
                                clickText2.setVisibility(View.GONE);
                                postBtn.setVisibility(View.GONE);
                                myVacanciesBtn.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                clickText3.setVisibility(View.GONE);
                                clickText2.setVisibility(View.VISIBLE);
                                postBtn.setVisibility(View.VISIBLE);
                                myVacanciesBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(e_JobFairU.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobFairU.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfID);
                params.put("employerId", eID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        StringRequest request = new StringRequest(Request.Method.POST, jobfairURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobfair");

                            if (success.equals("1")){
                                for (int y = 0; y<jsonArray.length(); y++){
                                    JSONObject object = jsonArray.getJSONObject(y);
                                    jfName1 = object.getString("jf_name").trim();
                                    setTitle("Upcoming: " + jfName1);
                                    jfName.setText(object.getString("jf_name").trim());
                                    jfDate.setText(object.getString("jf_date").trim());
                                    jfDateStop.setText(object.getString("jf_datestop"));
                                    jfOrganizer.setText(object.getString("jf_organizer"));
                                    jfLocation.setText(object.getString("jf_location").trim());
                                    jfDesc.setText(object.getString("jf_description").trim());

                                }
                            } else {
                                Toast.makeText(e_JobFairU.this, "Database error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(e_JobFairU.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfID);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void openDialog(){
        VacancyDialog vacancyDialog = new VacancyDialog();
        Bundle bundle = new Bundle();

        bundle.putString("e_id", eID);
        bundle.putString("jf_id", jfID);
        vacancyDialog.setArguments(bundle);
        vacancyDialog.show(getSupportFragmentManager(),"Vacancy Dialog");
    }
}
