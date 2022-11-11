package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateJfDialog extends AppCompatDialogFragment {

    private DatePickerDialog.OnDateSetListener jfDateSetListener, jfDateSetListener2;
    private EditText a_jfTitle, a_jfLoc, a_jfDate, a_jfDesc, a_jfDateStop, a_jfOrganizer;
    private String createjfURL = "http://192.168.1.9/dole_php/a_create_jobfair.php";

    private FragmentManager manager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.a_dialog_createjf, null);

        manager = getActivity().getSupportFragmentManager();

        a_jfTitle = view.findViewById(R.id.a_jfTitle);
        a_jfLoc = view.findViewById(R.id.a_jfLoc);
        a_jfDate = view.findViewById(R.id.a_jfDate);
        a_jfDateStop = view.findViewById(R.id.a_jfDateStop);
        a_jfDesc = view.findViewById(R.id.a_jfDesc);
        a_jfOrganizer = view.findViewById(R.id.a_jfOrganizer);

        a_jfDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(),android.R.style.Theme,jfDateSetListener
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
                DatePickerDialog dialog1 = new DatePickerDialog(getContext(),android.R.style.Theme,jfDateSetListener2
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

        builder.setView(view)
                .setTitle("Create Job Fair")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean error = false;
                        if (a_jfTitle.getText().toString().length() <5 ) {
                            a_jfTitle.setError("Please enter a valid Title");
                            error = true;
                        } if (a_jfDate.getText().toString().isEmpty()) {
                            a_jfDate.setError("Please set a valid date");
                            error = true;
                        } if (a_jfDateStop.getText().toString().isEmpty()) {
                            a_jfDateStop.setError("Please set a valid date");
                            error = true;
                        } if (a_jfLoc.getText().toString().length()<3) {
                            a_jfLoc.setError("Please enter a valid location");
                            error = true;
                        } if (a_jfOrganizer.getText().toString().isEmpty()) {
                            a_jfOrganizer.setError("Please set a valid date");
                            error = true;
                        } if (a_jfDesc.getText().toString().isEmpty()) {
                            a_jfDesc.setError("Please enter a valid description");
                            error = true;
                        }
                        if (!error) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, createjfURL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String success = jsonObject.getString("success");
                                                        String message = jsonObject.getString("message");

                                                        if (message.equals("duplicate")) {
                                                            Toast.makeText(view.getContext(), "Error! Duplication of Job Fair name.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            if (success.equals("1")) {
                                                                Toast.makeText(view.getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();

                                                                manager.beginTransaction().replace(R.id.a_fragment_container,
                                                                        new a_HomeFragment()).commit();
                                                            } else {
                                                                Toast.makeText(view.getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(view.getContext(), "Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(view.getContext(), "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("jf_name", a_jfTitle.getText().toString());
                                            params.put("jf_date", a_jfDate.getText().toString());
                                            params.put("jf_datestop", a_jfDateStop.getText().toString());
                                            params.put("jf_location", a_jfLoc.getText().toString());
                                            params.put("jf_organizer", a_jfOrganizer.getText().toString());
                                            params.put("jf_description", a_jfDesc.getText().toString());
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                    requestQueue.add(stringRequest);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setMessage("Are you sure you want to create this Job Fair?");
                            builder1.setTitle("Confirm Action");
                            builder1.create();
                            builder1.show();
                        } else {
                            Toast.makeText(view.getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }
}
