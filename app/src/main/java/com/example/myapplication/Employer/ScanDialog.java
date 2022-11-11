package com.example.myapplication.Employer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

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

public class ScanDialog extends AppCompatDialogFragment {

     private TextView nameEdit, emailEdit, numberEdit, addressEdit, birthdateEdit, genderEdit,
            jobpreferredEdit, educationEdit, skillsEdit, vacancyLocation;

    private Spinner vacancySpinner, hstatusSpinner;
    private EditText editComments;

    private String finalskills = "";
    private String scandataURL  = "http://192.168.1.9/dole_php/e_scan_retrieve.php"; //retrieving data from db using JSid
    private String eScanURL = "http://192.168.1.9/dole_php/e_scan_insert.php"; //adding data to db

    private String firstname, lastname, email, contactno, address, gender, dateofbirth, jobpreferred,
    otherskill, edlevel, fullname;

    final ArrayList<String> myVacancies = new ArrayList<String>();
    final ArrayList<String> myVacanciesId = new ArrayList<String>();
    final ArrayList<String> myVacanciesLocation = new ArrayList<String>();
    final ArrayList<String> hiringStatus = new ArrayList<String>();

    private FragmentActivity view5;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.e_dialog_scan,null);
        Bundle bundle = getArguments();
        final String jsId = bundle.getString("js_id");
        final String eId = bundle.getString("e_id");
        final String jfId = bundle.getString("jf_id");

        view5 = getActivity();

        nameEdit = view.findViewById(R.id.nameEdit);
        emailEdit = view.findViewById(R.id.emailEdit);
        numberEdit = view.findViewById(R.id.numberEdit);
        addressEdit = view.findViewById(R.id.addressEdit);
        birthdateEdit = view.findViewById(R.id.birthdateEdit);
        genderEdit = view.findViewById(R.id.genderEdit);
        jobpreferredEdit = view.findViewById(R.id.jobpreferredEdit);
        educationEdit = view.findViewById(R.id.educationEdit);
        skillsEdit = view.findViewById(R.id.skillsEdit);
        vacancyLocation = view.findViewById(R.id.vacancyLocation);

        vacancySpinner = view.findViewById(R.id.vacancySpinner);
        hstatusSpinner = view.findViewById(R.id.hstatusSpinner);

        editComments = view.findViewById(R.id.editComments);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, scandataURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String skill_success = jsonObject.getString("skill-success");
                            String vacancy_success = jsonObject.getString("vacancy-success");
                            JSONArray jsonjsArray = jsonObject.getJSONArray("jobseeker");
                            JSONArray jsonskillArray = jsonObject.getJSONArray("skills");

                            JSONArray jsonvacancyArray = jsonObject.getJSONArray("vacancies");
                            JSONArray jsonHstatusArray = jsonObject.getJSONArray("hstatus");

                            if (success.equals("1")){
                                for (int i = 0; i< jsonjsArray.length(); i++) {
                                    JSONObject object = jsonjsArray.getJSONObject(i);
                                    firstname = object.getString("js_first_name").trim();
                                    lastname = object.getString("js_last_name").trim();
                                    email = object.getString("js_email").trim();
                                    contactno = object.getString("js_contactno").trim();
                                    address = object.getString("js_address").trim();
                                    gender = object.getString("js_gender").trim();
                                    dateofbirth = object.getString("js_dateofbirth").trim();
                                    jobpreferred = object.getString("js_jobpreferred").trim();
                                    otherskill = object.getString("js_o_skill").trim();
                                    edlevel = object.getString("ed_level").trim();
                                }

                                fullname = firstname + " " + lastname;
                                nameEdit.setText(fullname);
                                if (email.equals("null")) {
                                    emailEdit.setText("  --- Not Set ---");
                                } else {
                                    emailEdit.setText(email);
                                }
                                if (contactno.equals("null")){
                                    numberEdit.setText("  --- Not Set ---");
                                } else {
                                    numberEdit.setText(contactno);
                                }
                                if (address.equals("null")) {
                                    addressEdit.setText("  --- Not Set ---");
                                } else {
                                    addressEdit.setText(address);
                                }
                                birthdateEdit.setText(dateofbirth);
                                genderEdit.setText(gender);
                                if (jobpreferred.equals("null")) {
                                    jobpreferredEdit.setText("  --- Not Set ---");
                                } else {
                                    jobpreferredEdit.setText(jobpreferred);
                                }
                                if (edlevel.equals("null")) {
                                    educationEdit.setText("  --- Not Set ---");
                                } else {
                                    educationEdit.setText(edlevel);
                                }
                                if (otherskill.equals("null")){
                                    skillsEdit.setText("  --- Not Set ---");
                                } else {
                                    skillsEdit.setText(otherskill);
                                }

                                //hiring status
                                for (int i = 0; i < jsonHstatusArray.length(); i++) {
                                    JSONObject object = jsonHstatusArray.getJSONObject(i);
                                    for (int y = 1; y<object.length()+1;y++){
                                        String status = object.getString(Integer.toString(y)).trim();
                                        hiringStatus.add(status);
                                    }
                                }
                                ArrayAdapter<String> harrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,
                                        hiringStatus);
                                hstatusSpinner.setAdapter(harrayAdapter);
                                hstatusSpinner.setSelection(4);
                                //for vacancy
                                if (vacancy_success.equals("1")) {
                                    for (int i = 0; i < jsonvacancyArray.length(); i++) {
                                        JSONObject object = jsonvacancyArray.getJSONObject(i);
                                            String vacancy_id = object.getString("jv_id").trim();
                                            String vacancy_title = object.getString("jv_title").trim();
                                            String vacancy_location = object.getString("jv_location");
                                            myVacanciesLocation.add(vacancy_location);
                                            myVacanciesId.add(vacancy_id);
                                            myVacancies.add(vacancy_title);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                                            myVacancies);
                                    vacancySpinner.setAdapter(adapter);
                                } else {
                                    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                                            (getContext(), R.array.empty, android.R.layout.simple_spinner_item);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    vacancySpinner.setAdapter(arrayAdapter);
                                }

                                if (skill_success.equals("1")) {
                                    for (int i = 0; i < jsonskillArray.length(); i++) {
                                        JSONObject object = jsonskillArray.getJSONObject(i);
                                        String skills = object.getString("js_skills").trim();
                                        finalskills = finalskills + skills + ", ";
                                    }
                                    finalskills = finalskills + skillsEdit.getText().toString();
                                    skillsEdit.setText(finalskills);
                                }
                            } else if (success.equals("0")) {
                                Toast.makeText(getContext(),"JOB SEEKER NOT REGISTERED!", Toast.LENGTH_SHORT).show();
                                view5.recreate();
                                dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Error! E" + e, Toast.LENGTH_SHORT).show();
                            view5.recreate();
                            dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error! " + error, Toast.LENGTH_SHORT).show();
                        view5.recreate();
                        dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobseekerId", jsId);
                params.put("jobfairId", jfId);
                params.put("employerId", eId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        vacancySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vacancyLocation.setText(myVacanciesLocation.get(myVacancies.indexOf(vacancySpinner.getSelectedItem().toString())));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        getActivity().recreate();

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog dialog1 = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                                .setTitle("Confirm Action")
                                .setMessage("Are you sure to list this job seeker?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, eScanURL,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String success = jsonObject.getString("success");
                                                            String message = jsonObject.getString("message");

                                                            if (success.equals("1")) {
                                                                Toast.makeText(view.getContext(), "Jobseeker added!", Toast.LENGTH_SHORT).show();
                                                                view5.recreate();
                                                                dismiss();
                                                            } else if (success.equals("0")){
                                                                if (message.equals("duplicate")) {
                                                                    Toast.makeText(view.getContext(), "Error! Duplication of data!", Toast.LENGTH_LONG).show();
                                                                    view5.recreate();
                                                                    dismiss();
                                                                } if (message.equals("error")) {
                                                                    Toast.makeText(view.getContext(), "Database Error!", Toast.LENGTH_LONG).show();
                                                                    view5.recreate();
                                                                    dismiss();
                                                                }
                                                            }
                                                        }  catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(view.getContext(),"Error! " + e, Toast.LENGTH_SHORT).show();
                                                            view5.recreate();
                                                            dismiss();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(view.getContext(),"Error! " + error, Toast.LENGTH_LONG).show();
                                                        view5.recreate();
                                                        dismiss();
                                                    }
                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("vacancyId", myVacanciesId.get(myVacancies.indexOf(vacancySpinner.getSelectedItem().toString())));
                                                params.put("jobfairId", jfId);
                                                params.put("jobseekerId", jsId);
                                                params.put("employerId", eId);
                                                params.put("hiringstatus", hstatusSpinner.getSelectedItem().toString());
                                                params.put("location", myVacanciesLocation.get(myVacancies.indexOf(vacancySpinner.getSelectedItem().toString())));
                                                params.put("gender", gender);
                                                params.put("comments", editComments.getText().toString());
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest1);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        view5.recreate();
                                        dismiss();
                                    }
                                })
                                .show();

                    }
                });
        return builder.create();
    }

}
