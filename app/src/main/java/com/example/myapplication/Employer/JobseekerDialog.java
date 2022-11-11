package com.example.myapplication.Employer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.myapplication.Employer.e_JobFairP;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobseekerDialog extends AppCompatDialogFragment {

    private TextView vacancyText, hiringText, locationText, commentsText, nameText, emailText,
    numberText, addressText, dateofbirthText, genderText, jobpreferredText, educationText, skillsText;

    private String dialogURL = "http://192.168.1.9/dole_php/e_js_dialog.php";
    private String finalskills = "";
    private ProgressBar progressBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.e_dialog_jobseeker,null);

        builder.setView(view);
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        Bundle bundle = getArguments();
        final String jsId = bundle.getString("js_id");
        final String eId = bundle.getString("e_id");
        final String jfId = bundle.getString("jf_id");

        progressBar = view.findViewById(R.id.progressBar5);

        vacancyText = view.findViewById(R.id.vacancyText);
        hiringText = view.findViewById(R.id.hiringText);
        locationText = view.findViewById(R.id.locationText);
        commentsText = view.findViewById(R.id.commentsText);
        nameText = view.findViewById(R.id.nameText);
        emailText = view.findViewById(R.id.emailText);
        numberText = view.findViewById(R.id.numberText);
        addressText = view.findViewById(R.id.addressText);
        dateofbirthText = view.findViewById(R.id.dateofbirthText);
        genderText = view.findViewById(R.id.genderText);
        jobpreferredText = view.findViewById(R.id.jobpreferredText);
        educationText = view.findViewById(R.id.educationText);
        skillsText = view.findViewById(R.id.skillsText);

       StringRequest stringRequest = new StringRequest(Request.Method.POST, dialogURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String skill_success = jsonObject.getString("skill-success");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobseeker");
                            JSONArray jsonskillArray = jsonObject.getJSONArray("skills");

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        vacancyText.setText(object.getString("jv_title"));
                                        hiringText.setText(object.getString("hiring_status"));
                                        locationText.setText(object.getString("jv_location"));
                                        commentsText.setText(object.getString("comments"));
                                        nameText.setText(object.getString("js_first_name") +" " + object.getString("js_last_name"));
                                        emailText.setText(object.getString("js_email"));
                                        numberText.setText(object.getString("js_contactno"));
                                        addressText.setText(object.getString("js_address"));
                                        dateofbirthText.setText(object.getString("js_dateofbirth"));
                                        genderText.setText(object.getString("js_gender"));
                                        jobpreferredText.setText(object.getString("job_preferred"));
                                        educationText.setText(object.getString("ed_level"));
                                    }
                                }
                            }

                            if (skill_success.equals("1")) {
                                for (int i = 0; i < jsonskillArray.length(); i++) {
                                    JSONObject object = jsonskillArray.getJSONObject(i);
                                    String skills = object.getString("js_skills").trim();
                                    finalskills = finalskills + skills + ", ";
                                }
                                finalskills = finalskills + skillsText.getText().toString();
                                skillsText.setText(finalskills);
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error e! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobseekerId", jsId);
                params.put("employerId", eId);
                params.put("jobfairId", jfId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        return builder.create();
    }
}
