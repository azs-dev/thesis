package com.example.myapplication.JobSeeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

public class MatchDialog extends AppCompatDialogFragment {

    private EditText jsVDialogTitle, jsVDialogLocation, jsVDialogEducation,
            jsVDialogSkills, jsVDialogCount, jsVDialogDescription;

    private String finalskills = "";
    private String otherSkill = "";

    String matchdialogURL = "http://192.168.1.9/dole_php/js_match_dialog.php";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.js_dialog_match, null);
        Bundle bundle = getArguments();
        final String jvId = bundle.getString("jv_id");

        jsVDialogTitle = view.findViewById(R.id.jsVDialogTitle);
        jsVDialogLocation = view.findViewById(R.id.jsVDialogLocation);
        jsVDialogEducation = view.findViewById(R.id.jsVDialogEducation);
        jsVDialogSkills = view.findViewById(R.id.jsVDialogSkills);
        jsVDialogCount = view.findViewById(R.id.jsVDialogCount);
        jsVDialogDescription = view.findViewById(R.id.jsVDialogDescription);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, matchdialogURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String skill_success = jsonObject.getString("skill-success");
                            JSONArray jsonArray = jsonObject.getJSONArray("vacancy");
                            JSONArray jsonskillArray = jsonObject.getJSONArray("skills");

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        jsVDialogTitle.setText(object.getString("jv_title"));
                                        jsVDialogLocation.setText(object.getString("jv_location"));
                                        jsVDialogEducation.setText(object.getString("ed_level"));
                                        jsVDialogCount.setText(object.getString("jv_vacancy_count"));
                                        jsVDialogDescription.setText(object.getString("jv_description"));
                                        otherSkill = object.getString("jv_skill_o");
                                    }
                                }
                            }
                            if (skill_success.equals("1")){
                                for (int i = 0; i < jsonskillArray.length(); i++) {
                                    JSONObject object = jsonskillArray.getJSONObject(i);
                                    String skills = object.getString("jv_skills").trim();
                                    finalskills = finalskills + skills + ", ";
                                }
                                finalskills = finalskills + otherSkill;
                                jsVDialogSkills.setText(finalskills);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Error! " + e ,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error! " + error ,Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jv_id", jvId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        builder.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
