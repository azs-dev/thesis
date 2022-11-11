package com.example.myapplication.Employer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyVacancyDialog extends AppCompatDialogFragment {

    private TextView myvTitle, myvEducation, myvLocation, myvCount;
    private EditText myvDescription;

    String myvacancydialogURL = "http://192.168.1.9/dole_php/e_dialog_my_vacancy.php";
    String deletevacancyURL = "http://192.168.1.9/dole_php/e_delete_vacancy.php";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.e_dialog_myvac, null);
        Bundle bundle = getArguments();
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final String jvID = bundle.getString("jv_id");

        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                        builder1.setTitle("Confirm Action")
                                .setMessage("Are you sure you want to delete this Job Vacancy?")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dismiss();
                                    }
                                })
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, deletevacancyURL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String success = jsonObject.getString("success");

                                                            if (success.equals("1")){
                                                                Toast.makeText(view.getContext(),"Vacancy Deleted", Toast.LENGTH_SHORT).show();
                                                            }

                                                        } catch (JSONException e) {
                                                            Toast.makeText(view.getContext(),"Error! " + e,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(view.getContext(),"Error! " + error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("jobvacancyId", jvID);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest);
                                    }
                                });
                        builder1.create().show();

                    }

                });

        myvTitle = view.findViewById(R.id.myvTitle);
        myvEducation = view.findViewById(R.id.myvEducation);
        myvLocation = view.findViewById(R.id.myvLocation);
        myvCount = view.findViewById(R.id.myvCount);
        myvDescription = view.findViewById(R.id.myvDescription);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myvacancydialogURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("vacancies");

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        myvTitle.setText(object.getString("jv_title"));
                                        myvEducation.setText(object.getString("jv_education"));
                                        myvLocation.setText(object.getString("jv_location"));
                                        myvCount.setText(object.getString("jv_vacancy_count"));
                                        myvDescription.setText(object.getString("jv_description"));
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } catch (JSONException e){
                            Toast.makeText(getContext(),"Error! " + e,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobvacancyId", jvID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return builder.create();
    }
}
