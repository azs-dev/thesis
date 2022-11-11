package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

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

public class EmployerDialog extends AppCompatDialogFragment {

    TextView aEDialogName, aEDialogLocal, aEDialogOverseas, aEDialogMale, aEDialogFemale,
            aEDialogHots, aEDialogNearHire, aEDialogQualified, aEDialogNotQualified, aEDialogUndefined
            , aEDialogScanned;

    private String statURL = "http://192.168.1.9/dole_php/e_stat_jobfair.php";
    private String listviewURL = "http://192.168.1.9/dole_php/e_stat_listview.php";

    final ArrayList<String> jobseekerId = new ArrayList<String>();
    final ArrayList<Jobfair> jobseeker = new ArrayList<>();
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.a_dialog_employer, null);

        Bundle bundle = getArguments();
        final String jfId = bundle.getString("jobfairId");
        final String eId = bundle.getString("employerId");
        final String eName = bundle.getString("employerName");


        builder.setView(view);
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        aEDialogLocal = view.findViewById(R.id.aEDialogLocal);
        aEDialogOverseas = view.findViewById(R.id.aEDialogOverseas);
        aEDialogMale = view.findViewById(R.id.aEDialogMale);
        aEDialogFemale = view.findViewById(R.id.aEDialogFemale);
        aEDialogHots = view.findViewById(R.id.aEDialogHots);
        aEDialogNearHire = view.findViewById(R.id.aEDialogNearHire);
        aEDialogQualified = view.findViewById(R.id.aEDialogQualified);
        aEDialogNotQualified = view.findViewById(R.id.aEDialogNotQualified);
        aEDialogUndefined = view.findViewById(R.id.aEDialogUndefined);
        aEDialogScanned = view.findViewById(R.id.aEDialogScanned);
        aEDialogName = view.findViewById(R.id.aEDialogName);
        aEDialogName.setText(eName);

        final ListView listView = view.findViewById(R.id.aEDialogList);
        final ProgressBar progressBar = view.findViewById(R.id.aEDialogProgress);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, statURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String headcount = jsonObject.getString("headcount");
                            JSONArray jsonArrayLoc = jsonObject.getJSONArray("location");
                            JSONArray jsonArrayGender = jsonObject.getJSONArray("gender");
                            JSONArray jsonArrayHiring = jsonObject.getJSONArray("hiring");

                            if (success.equals("1")){
                                aEDialogScanned.setText("Jobseeker Scanned: " + headcount);

                                for (int i = 0; i < jsonArrayLoc.length(); i++) {
                                    JSONObject object = jsonArrayLoc.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String location = object.getString("location").trim();
                                        String count = object.getString("count").trim();
                                        if (location.equals("Local")) {
                                            aEDialogLocal.setText("Local: " + count);
                                        } else if (location.equals("Overseas")){
                                            aEDialogOverseas.setText("Overseas: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayGender.length(); i++) {
                                    JSONObject object = jsonArrayGender.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("gender").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("Male")) {
                                            aEDialogMale.setText("Male: " + count);
                                        } else if (gender.equals("Female")){
                                            aEDialogFemale.setText("Female: " + count);
                                        }
                                    }
                                }

                                for (int i = 0; i < jsonArrayHiring.length(); i++) {
                                    JSONObject object = jsonArrayHiring.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String gender = object.getString("status").trim();
                                        String count = object.getString("count").trim();
                                        if (gender.equals("HotS")) {
                                            aEDialogHots.setText("HotS: " + count);
                                        } else if (gender.equals("Near-Hire")){
                                            aEDialogNearHire.setText("Near-Hire: " + count);
                                        } else if (gender.equals("Not Qualified")){
                                            aEDialogNotQualified.setText("Not Qualified: " + count);
                                        } else if (gender.equals("Qualified")){
                                            aEDialogQualified.setText("Qualified: " + count);
                                        } else if (gender.equals("Interviewed")){
                                            aEDialogUndefined.setText("Interviewed: " + count);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                            aEDialogScanned.setText("ERROR!!");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
                        aEDialogScanned.setText("ERROR!!");
                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employerId", eId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        StringRequest request = new StringRequest(Request.Method.POST, listviewURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobseeker");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String jsId = object.getString("js_id").trim();
                                        jobseekerId.add(jsId);

                                        String jsfName = object.getString("js_first_name").trim();
                                        String jslName = object.getString("js_last_name").trim();
                                        String fullname = jsfName + " " + jslName;
                                        String hiring = object.getString("hiring_status");
                                        String location = object.getString("jv_location");
                                        Jobfair jobfair = new Jobfair(fullname,location,hiring);
                                        jobseeker.add(jobfair);

                                    }
                                }
                                JobfairListAdapter adapter = new JobfairListAdapter(view.getContext(), R.layout.jf_adapter_view_layout,
                                        jobseeker);
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employerId", eId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(request);

        return builder.create();
    }
}
