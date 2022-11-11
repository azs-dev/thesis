package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddEmployerDialog extends AppCompatDialogFragment {

    String employersURL = "http://192.168.1.9/dole_php/a_employer_add.php"; // add employer
    String retrieveURL = "http://192.168.1.9/dole_php/a_employer_retrieve.php"; //get employers for spinner

    final ArrayList<String> employerList = new ArrayList<>();
    final ArrayList<String> employerId = new ArrayList<String>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.a_dialog_add_employer,null);
        builder.setView(view);
        final Spinner employerSpinner = view.findViewById(R.id.employerSpinner);

        Bundle bundle = getArguments();
        final String jfId = bundle.getString("jf_id");

        StringRequest request = new StringRequest(Request.Method.POST, retrieveURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("employers");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String eId = object.getString("e_id").trim();
                                        String eName = object.getString("e_cname").trim();

                                        employerList.add(eName);
                                        employerId.add(eId);
                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                                        employerList);
                                employerSpinner.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobfairId", jfId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(request);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, employersURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");
                                            String message = jsonObject.getString("message");

                                            if (message.equals("duplicate")){
                                                Toast.makeText(view.getContext(), "Employer already registered!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (success.equals("1")) {
                                                    Toast.makeText(view.getContext(), "Employer added!", Toast.LENGTH_SHORT).show();
                                                    dismiss();
                                                } else {
                                                    Toast.makeText(view.getContext(), "Error adding employer!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(view.getContext(), "Error!" + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(view.getContext(), "Error!" + error, Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("jobfairId", jfId);
                                params.put("employerId", employerId.get(employerList.indexOf(employerSpinner.getSelectedItem().toString())));
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        requestQueue.add(stringRequest);
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.setTitle("Confirm Action");
                builder1.setMessage("Are you sure you want to add this Employer to this Job Fair?");
                builder1.show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle("Add Employer");

        return builder.create();
    }
}
