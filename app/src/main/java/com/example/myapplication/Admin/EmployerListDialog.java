package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class EmployerListDialog extends AppCompatDialogFragment {

    TextView employerListName, employerListEmail, employerListNumber, employerListPerson;

    private String employerURL = "http://192.168.1.9/dole_php/a_employerlist_dialog.php"; //retrieve
    private String deleteURL = "http://192.168.1.9/dole_php/a_employerlist_dialog_delete.php"; //delete

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.a_dialog_employerlist, null);

        Bundle bundle = getArguments();
        final String eId = bundle.getString("employerId");
        final String jfId = bundle.getString("jobfairId");

        employerListName = view.findViewById(R.id.employerListName);
        employerListEmail = view.findViewById(R.id.employerListEmail);
        employerListNumber = view.findViewById(R.id.employerListNumber);
        employerListPerson = view.findViewById(R.id.employerListPerson);

        builder.setView(view);
        
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    
                                    if (success.equals("1")){
                                        Toast.makeText(view.getContext(), "Delete Success!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(view.getContext(), "Database Error", Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                requestQueue.add(stringRequest);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, employerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("employer");
                            
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    employerListName.setText(object.getString("e_cname").trim());
                                    employerListEmail.setText(object.getString("e_caddress").trim());
                                    employerListNumber.setText(object.getString("e_cnumber").trim());
                                    employerListPerson.setText(object.getString("e_cperson").trim());
                                }
                            } else {
                                Toast.makeText(view.getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                dismiss();
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        return builder.create();
    }
}
