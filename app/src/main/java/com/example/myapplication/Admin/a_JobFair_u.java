package com.example.myapplication.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class a_JobFair_u extends AppCompatActivity {

    private EditText a_jfName, a_jfDate, a_jfDateStop, a_jfOrganizer, a_jfLocation, a_jfDescription;
    private TextView a_jfAttendees, textView1;
    private String jfName, finalDate, jfId;
    private Date jfDate;

    private DatePickerDialog.OnDateSetListener jfDateSetListener, jfDateSetListener2;

    String jobfairURL = "http://192.168.1.9/dole_php/a_jobfair_u.php";
    String startURL = "http://192.168.1.9/dole_php/a_jobfair_start.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_job_fair_u);

        Intent intent = getIntent();
        jfId = intent.getStringExtra("jobfairId");
        setTitle("Upcoming: " + intent.getStringExtra("jobfairName"));



        a_jfAttendees = findViewById(R.id.a_jfAttendees);
        textView1 = findViewById(R.id.textView55);

        final Button startBtn = findViewById(R.id.startBtn);
        final Button employerListBtn = findViewById(R.id.a_employerlistBtn);
        final Button editBtn = findViewById(R.id.a_editJobFair);
        final Button saveBtn = findViewById(R.id.a_editJfSave);
        final Button cancelBtn = findViewById(R.id.a_editJfCancel);

        a_jfName = findViewById(R.id.a_jfName);
        a_jfDate = findViewById(R.id.a_jfDate);
        a_jfDateStop = findViewById(R.id.a_jfDateStop);
        a_jfOrganizer = findViewById(R.id.a_jfOrganizer);
        a_jfLocation = findViewById(R.id.a_jfLocation);
        a_jfDescription = findViewById(R.id.a_jfDescription);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJobfair();
            }
        });

        employerListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a_JobFair_u.this, a_EmployerList.class);
                intent.putExtra("jobfairName", jfName);
                intent.putExtra("jobfairId", jfId);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employerListBtn.setVisibility(View.GONE);
                startBtn.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);

                saveBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);

                a_jfName.requestFocus();
                a_jfName.setFocusableInTouchMode(true);
                a_jfName.setFocusable(true);
                a_jfOrganizer.setFocusable(true);
                a_jfOrganizer.setFocusableInTouchMode(true);
                a_jfLocation.setFocusableInTouchMode(true);
                a_jfLocation.setFocusable(true);
                a_jfOrganizer.setFocusable(true);
                a_jfOrganizer.setFocusableInTouchMode(true);
                a_jfDescription.setFocusable(true);
                a_jfOrganizer.setFocusableInTouchMode(true);

                a_jfDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dialog = new DatePickerDialog(a_JobFair_u.this,android.R.style.Theme,jfDateSetListener
                                ,year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                jfDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = year + "-" + month + "-" + dayOfMonth ;
                        a_jfDate.setText(date);
                    }
                };

                a_jfDateStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal1 = Calendar.getInstance();
                        int year1 = cal1.get(Calendar.YEAR);
                        int month1 = cal1.get(Calendar.MONTH);
                        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dialog1 = new DatePickerDialog(a_JobFair_u.this,android.R.style.Theme,jfDateSetListener2
                                ,year1,month1,day1);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();
                    }
                });

                jfDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int month1, int dayOfMonth1) {
                        month1 = month1 + 1;
                        String date1 = year1 + "-" + month1 + "-" + dayOfMonth1;
                        a_jfDateStop.setText(date1);
                    }
                };

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editURL = "http://192.168.1.9/dole_php/a_jobfair_edit.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, editURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");

                                                if (success.equals("1")) {
                                                    Toast.makeText(a_JobFair_u.this, "Success   ", Toast.LENGTH_SHORT).show();
                                                    overridePendingTransition(0, 0);
                                                    recreate();
                                                } else {
                                                    Toast.makeText(a_JobFair_u.this, "Database Error!", Toast.LENGTH_SHORT).show();

                                                }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(a_JobFair_u.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(a_JobFair_u.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("jf_name", a_jfName.getText().toString());
                                params.put("jf_date", a_jfDate.getText().toString());
                                params.put("jf_datestop", a_jfDateStop.getText().toString());
                                params.put("jf_location", a_jfLocation.getText().toString());
                                params.put("jf_organizer", a_jfOrganizer.getText().toString());
                                params.put("jf_description", a_jfDescription.getText().toString());
                                params.put("jobfairId", jfId);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(a_JobFair_u.this);
                        requestQueue.add(stringRequest);
                    }
                });

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employerListBtn.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);

                saveBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);

                recreate();
                overridePendingTransition(0, 0);
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
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
                                    jfName = object.getString("jf_name").trim();
                                    setTitle("Upcoming: " + jfName);
                                    a_jfName.setText(object.getString("jf_name").trim());
                                    a_jfDate.setText(object.getString("jf_date").trim());
                                    a_jfDateStop.setText(object.getString("jf_datestop"));
                                    a_jfOrganizer.setText(object.getString("jf_organizer"));
                                    a_jfLocation.setText(object.getString("jf_location").trim());
                                    a_jfDescription.setText(object.getString("jf_description").trim());
                                    a_jfAttendees.setText(object.getString("jf_pattendees").trim());

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = object.getString("jf_date");
                                    jfDate = sdf.parse(date);
                                    finalDate = DateFormat.getDateInstance().format(jfDate);

                                }
                            } else {
                                Toast.makeText(a_JobFair_u.this, "Database error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(a_JobFair_u.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(a_JobFair_u.this, "Error! " + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    
    public void startJobfair (){
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        if (!finalDate.equals(currentDate)){
            AlertDialog.Builder builder = new AlertDialog.Builder(a_JobFair_u.this);
            builder.setMessage("You may only start the Job Fair on the said Date above.");
            builder.setTitle("Error!");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(a_JobFair_u.this);
            builder.setMessage("Are you sure you want to start this Job Fair?");
            builder.setTitle("Confirm Action");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                    final String sdt = df.format(new Date(System.currentTimeMillis()));

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, startURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("1")){
                                            Toast.makeText(getApplicationContext(), "Job Fair launched!", Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                            Intent intent1 = new Intent(a_JobFair_u.this, AdminHome.class);
                                            intent1.putExtra("a_name", "admin");
                                            startActivity(intent1);
                                            overridePendingTransition(0, 0);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                            Intent intent1 = new Intent(a_JobFair_u.this, AdminHome.class);
                                            intent1.putExtra("a_name", "admin");
                                            startActivity(intent1);
                                            overridePendingTransition(0, 0);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(a_JobFair_u.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(a_JobFair_u.this, AdminHome.class);
                                        intent1.putExtra("a_name", "admin");
                                        startActivity(intent1);
                                        overridePendingTransition(0, 0);
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(a_JobFair_u.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("jobfairId", jfId);
                            params.put("time", sdt);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(a_JobFair_u.this);
                    requestQueue.add(stringRequest);
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
    }
}
