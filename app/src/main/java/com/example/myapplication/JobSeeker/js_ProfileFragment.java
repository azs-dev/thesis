package com.example.myapplication.JobSeeker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class js_ProfileFragment extends Fragment {

    private String jsId;
    private String qrString;
    private String finalskillsProfile = "";
    private String finalskillsProfile1 = "";
    private String profileURL = "http://192.168.1.9/dole_php/js_profile_fragment.php";
    String spinnerURL = "http://192.168.1.9/dole_php/js_register_spinner.php";

    private FragmentManager manager;

    private Button saveBtn, cancelBtn, skillsBtn, editBtn;
    private TextView otherSkillsTextProfile, qrtext;
    private EditText otherSkills, usernameEdit, fullnameEdit,emailEdit,contactnoEdit,addressEdit,dateofbirthEdit
    ,jobpreferredEdit,educationEdit,skillsEdit;


    private DatePickerDialog.OnDateSetListener jfDateSetListener;

    private Spinner educationSpinner;

    //for skills//
    ArrayList<Integer> userSkill = new ArrayList<>();  //selected skills
    boolean[] checkedItems; //check if checked or not
    String[] skillArray; //List if skills
    String finalskills1 ="";
    //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.js_fragment_profile, container, false);
        usernameEdit = view.findViewById(R.id.usernameEdit);
        fullnameEdit = view.findViewById(R.id.fullnameEdit);
        emailEdit = view.findViewById(R.id.emailEdit);
        contactnoEdit = view.findViewById(R.id.contactnoEdit);
        addressEdit = view.findViewById(R.id.addressEdit);
        dateofbirthEdit = view.findViewById(R.id.dateofbirthEdit);
        jobpreferredEdit = view.findViewById(R.id.jobpreferredEdit);
        educationEdit = view.findViewById(R.id.educationEdit);
        skillsEdit = view.findViewById(R.id.skillsEdit);

        //for Edit
        otherSkills = view.findViewById(R.id.otherSkillsProfile);
        otherSkillsTextProfile = view.findViewById(R.id.otherSkillsTextProfile);
        qrtext = view.findViewById(R.id.qrtext);
        educationSpinner = view.findViewById(R.id.educationSpinner);

        saveBtn = view.findViewById(R.id.saveBtnProfile);
        cancelBtn = view.findViewById(R.id.cancelBtnProfile);
        skillsBtn = view.findViewById(R.id.skillsBtnProfile);
        editBtn = view.findViewById(R.id.editBtnProfile);
        //end

        final ImageView qrImage = view.findViewById(R.id.qrImage);
        final ProgressBar js_progressBar = view.findViewById(R.id.js_progressBar);

        final Bundle bundle = getArguments();
        if (bundle != null){
            jsId = bundle.getString("id");
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrtext.setVisibility(View.GONE);
                qrImage.setVisibility(View.GONE);
                skillsEdit.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
                educationEdit.setVisibility(View.GONE);

                educationSpinner.setVisibility(View.VISIBLE);
                otherSkills.setVisibility(View.VISIBLE);
                otherSkillsTextProfile.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
                skillsBtn.setVisibility(View.VISIBLE);

                emailEdit.requestFocus();
                emailEdit.setFocusableInTouchMode(true);
                emailEdit.setFocusable(true);
                contactnoEdit.setFocusable(true);
                contactnoEdit.setFocusableInTouchMode(true);
                addressEdit.setFocusableInTouchMode(true);
                addressEdit.setFocusable(true);
                jobpreferredEdit.setFocusable(true);
                jobpreferredEdit.setFocusableInTouchMode(true);
                otherSkills.setFocusable(true);
                otherSkills.setFocusableInTouchMode(true);

                //getting skills and education from db
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, spinnerURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    JSONArray jsonskillArray = jsonObject.getJSONArray("skills");
                                    JSONArray jsonedArray = jsonObject.getJSONArray("ed");
                                    ArrayList<String> edarrayList = new ArrayList<>();
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
                                        skillArray = skillarrayList.toArray(new String[] {}); //transferring data to array for dialog
                                        checkedItems = new boolean[skillArray.length]; //setting number of boolean array
                                        //for education
                                        for (int i = 0; i < jsonedArray.length(); i++) {
                                            JSONObject object = jsonedArray.getJSONObject(i);
                                            for (int y = 1; y<object.length()+1;y++){
                                                String ed = object.getString(Integer.toString(y)).trim();
                                                edarrayList.add(ed);
                                            }
                                        }
                                        edarrayList.add(0,"Set Educational Attainment");
                                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),
                                                android.R.layout.simple_spinner_dropdown_item, edarrayList);
                                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        educationSpinner.setAdapter(adapter1);
                                        educationSpinner.setSelection(0);
                                        //end education
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                requestQueue1.add(stringRequest1);
                //end

                skillsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                        builder.setTitle("Select skills");
                        builder.setMultiChoiceItems(skillArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked){
                                    userSkill.add(position);
                                } else {
                                    userSkill.remove((Integer.valueOf(position)));
                                }
                            }
                        });
                        builder.setCancelable(false);
                        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finalskills1 = "";
                                for (int i = 0; i < userSkill.size(); i++){
                                    finalskills1 = finalskills1 + skillArray[userSkill.get(i)];
                                    if (i != userSkill.size() -1){
                                        finalskills1 = finalskills1 + ",";
                                    }
                                }
                                Toast.makeText(getContext(),finalskills1,Toast.LENGTH_SHORT).show();
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
                                    userSkill.clear();
                                    finalskills1 = "";
                                }
                            }
                        });
                        AlertDialog mDialog = builder.create();
                        mDialog.show();
                    }
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (educationSpinner.getSelectedItem().toString().equals("Set Educational Attainment")) {
                            Toast.makeText(getContext(), "Please select an Educational Attainment", Toast.LENGTH_SHORT).show();
                        } else {
                            String editURL = "http://192.168.1.9/dole_php/js_profile_edit_test.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, editURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")) {
                                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                                js_ProfileFragment profileFragment = new js_ProfileFragment();

                                                Bundle bundleId = new Bundle();
                                                bundleId.putString("id", jsId);
                                                profileFragment.setArguments(bundleId);
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.js_fragment_container,
                                                        profileFragment).commit();
                                            } else {
                                                Toast.makeText(getContext(), "Database Error! ", Toast.LENGTH_SHORT).show();
                                                js_ProfileFragment profileFragment = new js_ProfileFragment();

                                                Bundle bundleId = new Bundle();
                                                bundleId.putString("id", jsId);
                                                profileFragment.setArguments(bundleId);
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.js_fragment_container,
                                                        profileFragment).commit();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "Error! errrr" + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
                                    }
                                })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("jobseekerId", jsId);
                                    params.put("js_email", emailEdit.getText().toString());
                                    params.put("js_contactno", contactnoEdit.getText().toString());
                                    params.put("js_address", addressEdit.getText().toString());
                                    params.put("js_edattain", educationSpinner.getSelectedItem().toString());
                                    params.put("js_jobpreferred", jobpreferredEdit.getText().toString());
                                    params.put("js_o_skill", otherSkills.getText().toString());
                                    params.put("js_skill", finalskills1);

                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(stringRequest);
                        }
                    }
                });

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrtext.setVisibility(View.VISIBLE);
                qrImage.setVisibility(View.VISIBLE);
                skillsEdit.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.VISIBLE);
                educationEdit.setVisibility(View.VISIBLE);

                otherSkills.setVisibility(View.GONE);
                otherSkillsTextProfile.setVisibility(View.GONE);
                saveBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                skillsBtn.setVisibility(View.GONE);
                educationSpinner.setVisibility(View.GONE);

                js_ProfileFragment profileFragment = new js_ProfileFragment();

                Bundle bundleId = new Bundle();
                bundleId.putString("id", jsId);
                profileFragment.setArguments(bundleId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.js_fragment_container,
                        profileFragment).commit();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, profileURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String skill_success = jsonObject.getString("skill-success");
                            JSONArray jsonjsArray = jsonObject.getJSONArray("jobseeker");
                            JSONArray jsonskillArray = jsonObject.getJSONArray("skills");

                            if (success.equals("1")){
                                for (int i = 0; i< jsonjsArray.length(); i++){
                                    JSONObject object = jsonjsArray.getJSONObject(i);
                                    String username = object.getString("js_username").trim();
                                    String firstname = object.getString("js_first_name").trim();
                                    String lastname = object.getString("js_last_name").trim();
                                    String email = object.getString("js_email").trim();
                                    String contactno = object.getString("js_contactno").trim();
                                    String address = object.getString("js_address").trim();
                                    String dateofbirth = object.getString("js_dateofbirth").trim();
                                    String jobpreferred = object.getString("js_jobpreferred").trim();
                                    String otherskill = object.getString("js_o_skill").trim();
                                    String edlevel = object.getString("ed_level").trim();
                                    qrString = object.getString("js_username");

                                    usernameEdit.setText(username);
                                    String fullname = firstname + " " + lastname;
                                    fullnameEdit.setText(fullname);
                                    emailEdit.setText(email);
                                    contactnoEdit.setText(contactno);
                                    addressEdit.setText(address);
                                    dateofbirthEdit.setText(dateofbirth);
                                    jobpreferredEdit.setText(jobpreferred);
                                    educationEdit.setText(edlevel);
                                    skillsEdit.setText(otherskill);
                                    js_progressBar.setVisibility(View.GONE);
                                }

                                if (skill_success.equals("1")) {
                                    for (int i = 0; i < jsonskillArray.length(); i++) {
                                        JSONObject object = jsonskillArray.getJSONObject(i);
                                        String skills = object.getString("js_skills").trim();
                                        finalskillsProfile = finalskillsProfile + skills + ", ";
                                    }
                                    finalskillsProfile1 = finalskillsProfile + skillsEdit.getText().toString();
                                    skillsEdit.setText(finalskillsProfile1);
                                }

                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                qrString = bundle.getString("id");
                                /*int QR = Integer.parseInt(bundle.getString("id"));
                                QR = QR * 2020;
                                qrString = Integer.toString(QR); HASH */
                                try {
                                    BitMatrix bitMatrix = multiFormatWriter.encode(qrString,
                                            BarcodeFormat.QR_CODE,1000,1000);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                    qrImage.setImageBitmap(bitmap);
                                } catch (WriterException e){
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Database Error!" + e , Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Database Error "+ error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("js_id", jsId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return view;
    }


}
