package com.example.myapplication.Employer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class VacancyDialog extends AppCompatDialogFragment {

    private EditText jv_title, jv_OthersEdit, jv_VacCount, jv_DescriptionEdit ;
    private Spinner jv_educationSpin, jv_location;
    private String eID, jfID;
    private Button jv_skillsBtn;

    private ArrayList<Integer> requiredSkill = new ArrayList<>();  //selected skills
    private boolean[] checkedItems; //check if checked or not
    private String[] skillArray; //List if skills
    String finalskills;

    private String postvURL = "http://192.168.1.9/dole_php/e_post_vacancy.php";
    private String spinnerURL = "http://192.168.1.9/dole_php/e_vacancy_spinner.php";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.e_dialog_vacancy_create, null);
        Bundle bundle = getArguments();


        jv_title = view.findViewById(R.id.jv_titleEdit);
        jv_OthersEdit = view.findViewById(R.id.jv_OthersEdit);
        jv_VacCount = view.findViewById(R.id.jv_VacCount);
        jv_DescriptionEdit = view.findViewById(R.id.jv_DescriptionEdit);
        jv_skillsBtn = view.findViewById(R.id.jv_skillsBtn);

        finalskills = "";

        jv_location = view.findViewById(R.id.jv_location);
        jv_educationSpin = view.findViewById(R.id.jv_educationSpin);
        eID = bundle.getString("e_id");
        jfID = bundle.getString("jf_id");

        //Location//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                (getContext(),R.array.location, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jv_location.setAdapter(arrayAdapter);
        //skill//
        StringRequest stringRequest = new StringRequest(Request.Method.POST, spinnerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonskillArray = jsonObject.getJSONArray("skills");
                            JSONArray jsonedArray = jsonObject.getJSONArray("ed");
                            ArrayList<String> arrayedList = new ArrayList<>();
                            ArrayList<String> skillarrayList = new ArrayList<>();

                            if (success.equals("1")) {
                                //for skills
                                for (int i = 0; i < jsonskillArray.length(); i++) {
                                    JSONObject object = jsonskillArray.getJSONObject(i);
                                    for (int y = 1; y<object.length()+1;y++){
                                        String skill = object.getString(Integer.toString(y)).trim();
                                        skillarrayList.add(skill);
                                    }
                                }
                                skillArray = skillarrayList.toArray(new String[] {});
                                checkedItems = new boolean[skillArray.length];
                                //for education
                                for (int i = 0; i < jsonedArray.length(); i++) {
                                    JSONObject object = jsonedArray.getJSONObject(i);
                                    for (int y = 1; y<6;y++){
                                        String ed = object.getString(Integer.toString(y)).trim();
                                        arrayedList.add(ed);
                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item, arrayedList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                jv_educationSpin.setAdapter(adapter);
                            } else {
                                Toast.makeText(getContext(),"Spinner Empty!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Spinner Error   "+ error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        //end
        //skill dialog
        jv_skillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Set Required Skills");
                builder.setMultiChoiceItems(skillArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked){
                            requiredSkill.add(position);
                        } else {
                            requiredSkill.remove((Integer.valueOf(position)));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalskills = "";
                        for (int i = 0; i < requiredSkill.size(); i++){
                            finalskills = finalskills + skillArray[requiredSkill.get(i)];
                            if (i != requiredSkill.size() -1){
                                finalskills = finalskills + ",";
                            }
                        }
                        Toast.makeText(getContext(),finalskills,Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                            requiredSkill.clear();
                            finalskills = "";
                        }
                    }
                });
                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });
        //end

        builder.setView(view)
                .setCancelable(false)
                .setTitle("Add Vacancy")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, postvURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");
                                            String message = jsonObject.getString("message");

                                            if (success.equals("1")) {
                                                Toast.makeText(view.getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                            } else if (message.equals("Fields must not be empty!")) {
                                                Toast.makeText(view.getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (message.equals("job title already exists")) {
                                                Toast.makeText(view.getContext(), "Error! You've posted the same job vacancy already.",
                                                        Toast.LENGTH_SHORT).show();
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
                                params.put("jv_jf_id", jfID);
                                params.put("jv_e_id", eID);
                                params.put("jv_title", jv_title.getText().toString());
                                params.put("jv_skill_o", jv_OthersEdit.getText().toString());
                                params.put("jv_skill", finalskills.trim());
                                params.put("jv_education", jv_educationSpin.getSelectedItem().toString());
                                params.put("jv_location", jv_location.getSelectedItem().toString());
                                params.put("jv_vacancy_count", jv_VacCount.getText().toString());
                                params.put("jv_description", jv_DescriptionEdit.getText().toString());
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);
                    }
                });
        return builder.create();
    }
}
