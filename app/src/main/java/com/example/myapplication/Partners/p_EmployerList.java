package com.example.myapplication.Partners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

public class p_EmployerList extends AppCompatActivity {

    String employersURL = "http://192.168.1.9/dole_php/p_employer_list.php";
    private final ArrayList<Jobfair> employerList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_employer_list);
        Intent intent = getIntent();
        final String jfId = intent.getStringExtra("jobfairId");
        String jfName = intent.getStringExtra("jobfairName");
        final ListView employerLv = findViewById(R.id.employerList);
        final TextView label1 = findViewById(R.id.label13);
        final TextView label2 = findViewById(R.id.label10);
        final TextView textView = findViewById(R.id.eListText2);
        final TextView noEmployerText = findViewById(R.id.noEmployerText2);
        final ProgressBar progressBar = findViewById(R.id.eListProgressBar2);
        final ImageView logo = findViewById(R.id.doleLogo2);

        SwipeRefreshLayout refresh = findViewById(R.id.refresh2);

        setTitle("List of Employers");

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });

        String title = "Employers for the upcoming jobfair:  " + jfName;
        SpannableString ss = new SpannableString(title);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
        ss.setSpan(fcs, 36, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);

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
                                JobfairListAdapter jobfairListAdapter = new JobfairListAdapter(p_EmployerList.this,R.layout.jf_adapter_view_layout,
                                        employerList);
                                employerLv.setAdapter(jobfairListAdapter);
                                employerLv.setVisibility(View.VISIBLE);
                                label1.setVisibility(View.VISIBLE);
                                label2.setVisibility(View.VISIBLE);
                                logo.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                noEmployerText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(p_EmployerList.this, "Error! " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(p_EmployerList.this, "Database Error! " + error, Toast.LENGTH_LONG).show();
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
