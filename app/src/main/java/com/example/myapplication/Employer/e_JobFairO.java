package com.example.myapplication.Employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class e_JobFairO extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    Button scanBtn, listBtn, o_myvacanciesBtn, submitBtn_o;
    private String eID, jfID, jsID;
    private ZXingScannerView zXingScannerView;
    private TextView textView4, textView3;
    private EditText uniqueEdit;


    String activationcheckURL = "http://192.168.1.9/dole_php/e_validcheck.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_jobfair_o);
        Intent intent = getIntent();
        setTitle("Ongoing: " + intent.getStringExtra("jobfairName"));

         eID = intent.getStringExtra("e_id");
         jfID = intent.getStringExtra("jobfairId");

        submitBtn_o = findViewById(R.id.submitBtn_o);
        scanBtn = findViewById(R.id.scanBtn);
        listBtn = findViewById(R.id.listBtn);
        o_myvacanciesBtn = findViewById(R.id.o_myvacanciesBtn);
        uniqueEdit = findViewById(R.id.uniqueEdit);
        textView4 = findViewById(R.id.textView4);
        textView3 = findViewById(R.id.textView3);
        final ProgressBar progressBar = findViewById(R.id.e_jfO_ProgressBar);


        submitBtn_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = uniqueEdit.getText().toString();
                int uniqueCode = Integer.parseInt(value);

                uniqueCode = uniqueCode/2020;
                jsID = Integer.toString(uniqueCode);
                openDialog();
                uniqueEdit.getText().clear();
            }
        });

        o_myvacanciesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(e_JobFairO.this, e_My_OngoingVacancies.class);
                intent1.putExtra("employerId",eID);
                intent1.putExtra("jobfairId", jfID);
                startActivity(intent1);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(e_JobFairO.this, e_JobSeekerList.class);
                intent.putExtra("employerId", eID );
                intent.putExtra("jobfairId", jfID);
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
                                textView4.setText("Sorry you are not registered in this Job Fair. Head " +
                                        "over to DOLE to register for the next upcoming Job Fairs.");
                                textView3.setVisibility(View.GONE);
                                listBtn.setVisibility(View.GONE);
                                o_myvacanciesBtn.setVisibility(View.GONE);
                                scanBtn.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                uniqueEdit.setVisibility(View.GONE);
                                submitBtn_o.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(e_JobFairO.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobFairO.this, "Error! " + error, Toast.LENGTH_SHORT).show();
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

    }

    public void scan (View view) {
        String vacancycheckURL = "http://192.168.1.9/dole_php/e_vacancycheck.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, vacancycheckURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                zXingScannerView = new ZXingScannerView(getApplicationContext());
                                setContentView(zXingScannerView);
                                zXingScannerView.setResultHandler(e_JobFairO.this);
                                zXingScannerView.startCamera();
                            } else if (success.equals("0")) {
                                Toast.makeText(e_JobFairO.this, "You may only start scanning after posting a vacancy!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(e_JobFairO.this, "Database Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
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

    }


    @Override
    public void handleResult(Result result) {
        jsID = result.getText();
        openDialog();
        //zXingScannerView.resumeCameraPreview(this);

        zXingScannerView.removeAllViews();
        //finish();
        //startActivity(getIntent());
    }

    public void openDialog(){
        ScanDialog scanDialog = new ScanDialog();
        Bundle bundle = new Bundle();
        bundle.putString("js_id", jsID);
        bundle.putString("jf_id", jfID);
        bundle.putString("e_id", eID);
        scanDialog.setArguments(bundle);
        scanDialog.show(getSupportFragmentManager(),"Scan Dialog");

    }

}
