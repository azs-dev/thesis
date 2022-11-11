package com.example.myapplication.JobSeeker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JobSeekerSignup extends AppCompatActivity {
        //Date of birth
        private EditText mDisplayDate;
        private DatePickerDialog.OnDateSetListener mDateSetListener;
        //
        private EditText usernameEdit, password1, password2, emailAddress,firstNameEdit, lastName, contactNo,
                address, dateOfBirth, course, school, jobLocation, preferredOccupation, otherSkill;
        private Spinner edAttainmentSpinner, genderSpin;
        private Button submitBtn, skillsBtn;
        private ProgressBar loading, progressBar;

        //for skills//
        ArrayList<Integer> userSkill = new ArrayList<>();  //selected skills
        boolean[] checkedItems; //check if checked or not
        String[] skillArray; //List if skills
        String finalskills;
        //

        String submitURL = "http://192.168.1.9/dole_php/js_register.php"; //remember to check IPCONFIG in command prompt
        String spinnerURL = "http://192.168.1.9/dole_php/js_register_spinner.php";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_signup);
        setTitle("Jobseeker Sign up");

        loading = findViewById(R.id.loading);

        usernameEdit = findViewById(R.id.username);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        emailAddress = findViewById(R.id.emailAddress);
        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastName = findViewById(R.id.lastName);
        contactNo = findViewById(R.id.contactNo);
        address = findViewById(R.id.address);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        course = findViewById(R.id.course);
        school = findViewById(R.id.school);
        jobLocation = findViewById(R.id.jobLocation);
        preferredOccupation = findViewById(R.id.preferredOccupation);
        submitBtn = findViewById(R.id.submitBtn);
        edAttainmentSpinner = findViewById(R.id.edAttainmentSpinner);
        genderSpin = findViewById(R.id.genderSpin);
        otherSkill = findViewById(R.id.otherSkill);
        skillsBtn = findViewById(R.id.skillsBtnProfile);
        progressBar = findViewById(R.id.progressBar);

        usernameEdit.addTextChangedListener(loginTextWatcher);
        password1.addTextChangedListener(loginTextWatcher);
        password2.addTextChangedListener(loginTextWatcher);
        emailAddress.addTextChangedListener(loginTextWatcher);
        firstNameEdit.addTextChangedListener(loginTextWatcher);
        lastName.addTextChangedListener(loginTextWatcher);
        contactNo.addTextChangedListener(loginTextWatcher);
        address.addTextChangedListener(loginTextWatcher);
        dateOfBirth.addTextChangedListener(loginTextWatcher);
        course.addTextChangedListener(loginTextWatcher);
        school.addTextChangedListener(loginTextWatcher);
        jobLocation.addTextChangedListener(loginTextWatcher);
        preferredOccupation.addTextChangedListener(loginTextWatcher);

        //BEGIN OF DATE OF BIRTH
        mDisplayDate = dateOfBirth;
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(JobSeekerSignup.this,android.R.style.Theme,mDateSetListener
                ,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                mDisplayDate.setText(date);
            }
        };
        //END OF DATE OF BIRTH

        //BEGIN OF GENDER
        final Spinner spinner = findViewById(R.id.genderSpin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //end

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
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(JobSeekerSignup.this,
                                        android.R.layout.simple_spinner_dropdown_item, edarrayList);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                edAttainmentSpinner.setAdapter(adapter1);
                                edAttainmentSpinner.setSelection(0);
                                //end education
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JobSeekerSignup.this, "Database Error!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);
        //end
        //skills dialog
        skillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerSignup.this, R.style.AlertDialogTheme);
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
                        finalskills = "";
                        for (int i = 0; i < userSkill.size(); i++){
                            finalskills = finalskills + skillArray[userSkill.get(i)];
                            if (i != userSkill.size() -1){
                                finalskills = finalskills + ",";
                            }
                        }
                        Toast.makeText(JobSeekerSignup.this,finalskills,Toast.LENGTH_SHORT).show();
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
                            finalskills = "";
                        }
                    }
                });
                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });
        //end of skills dialog/

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1Check = password1.getText().toString().trim();
                String password2Check = password2.getText().toString().trim();
                boolean error = false;
                if (!password1Check.equals(password2Check)) {
                    password1.setError("Passwords do not match!");
                    password2.setError("Passwords do not match!");
                    password1.getText().clear();
                    password2.getText().clear();
                    error = true;
                } if (edAttainmentSpinner.getSelectedItem().toString().equals("Set Educational Attainment")) {
                    error = true;
                    usernameEdit.setError("Please select an Educational Attainment");
                }
                if (usernameEdit.getText().toString().length() < 4 ) {
                    error = true;
                    usernameEdit.setError("Username must have more than 4 characters");
                }
                if (emailAddress.getText().toString().length() < 10 ) {
                    error = true;
                    emailAddress.setError("Please enter a valid e-mail");
                }
                if (firstNameEdit.getText().toString().length() < 2 ) {
                    error = true;
                    firstNameEdit.setError("Invalid first name");
                }
                if (lastName.getText().toString().length() < 2 ) {
                    error = true;
                    lastName.setError("Invalid last name");
                }
                if (contactNo.getText().toString().length() < 10 ) {
                    error = true;
                    contactNo.setError("Please enter a valid number");
                }
                if (address.getText().toString().length() < 4 ) {
                    error = true;
                    address.setError("Please enter a valid addresss");
                }
                if (school.getText().toString().length() < 2 ) {
                    error = true;
                    school.setError("Please enter a valid school");
                }if (usernameEdit.getText().toString().equals("admin") ) {
                    error = true;
                    usernameEdit.setError("Username unavailable");
                } if (preferredOccupation.getText().toString().equals("partner")) {
                    error = true;
                    usernameEdit.setError("Username unavailable");
                }
                if (jobLocation.getText().toString().length() < 5 ) {
                    error = true;
                    jobLocation.setError("Please enter a valid location");
                }
                if (preferredOccupation.getText().toString().length() < 3 ) {
                    error = true;
                    preferredOccupation.setError("Please enter a valid job preference");
                } if (!error){
                    Submit();
                } else {
                    usernameEdit.requestFocus();
                    usernameEdit.getText().clear();
                    usernameEdit.setError("Please fill up the form accordingly");
                }
            }
        });
    }


    private void Submit() {
        loading.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);

        final String username = this.usernameEdit.getText().toString().trim();
        final String password1 = this.password1.getText().toString().trim();
        final String email = this.emailAddress.getText().toString().trim();
        final String firstname = this.firstNameEdit.getText().toString().trim();
        final String lastname = this.lastName.getText().toString().trim();
        final String contactnumber = this.contactNo.getText().toString().trim();
        final String gender = this.genderSpin.getSelectedItem().toString().trim();
        final String address = this.address.getText().toString().trim();
        final String dateofbirth = this.mDisplayDate.getText().toString().trim();
        final String edattainment = this.edAttainmentSpinner.getSelectedItem().toString().trim();
        final String course = this.course.getText().toString().trim();
        final String school = this.school.getText().toString().trim();
        final String joblocation = this.jobLocation.getText().toString().trim();
        final String preferredoccupation = this.preferredOccupation.getText().toString().trim();
        final String finalskills = this.finalskills.trim();
        final String otherskill = this.otherSkill.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, submitURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(JobSeekerSignup.this, "Registration success!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                if (message.equals("username already exists")){
                                    usernameEdit.requestFocus();
                                    usernameEdit.getText().clear();
                                    usernameEdit.setError("Username already exists!");
                                    loading.setVisibility(View.GONE);
                                    submitBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(JobSeekerSignup.this, "Registration error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(JobSeekerSignup.this, "Registration error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("js_username", username);
                params.put("js_password", password1);
                params.put("js_email", email);
                params.put("js_first_name", firstname);
                params.put("js_last_name", lastname);
                params.put("js_contactno", contactnumber);
                params.put("js_address", address);
                params.put("js_gender", gender);
                params.put("js_dateofbirth", dateofbirth);
                params.put("js_edattain", edattainment);
                params.put("js_course", course);
                params.put("js_school", school);
                params.put("js_skill", finalskills);
                params.put("js_o_skill", otherskill);
                params.put("js_joblocation", joblocation);
                params.put("js_jobpreferred", preferredoccupation);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = usernameEdit.getText().toString().trim();
            String password11 = password1.getText().toString();
            String password22 = password2.getText().toString();
            String emailAddressInput = emailAddress.getText().toString().trim();
            String firstNameInput = firstNameEdit.getText().toString().trim();
            String lastNameInput = lastName.getText().toString().trim();
            String contactNoInput = contactNo.getText().toString().trim();
            String addressInput = address.getText().toString().trim();
            String dateOfBirthInput = dateOfBirth.getText().toString().trim();
            String courseInput = course.getText().toString().trim();
            String schoolInput = school.getText().toString().trim();
            String jobLocationInput = jobLocation.getText().toString().trim();
            String preferredOccupationInput = preferredOccupation.getText().toString().trim();

            submitBtn.setEnabled(!usernameInput.isEmpty() && !password11.isEmpty() && !password22.isEmpty()
            && !emailAddressInput.isEmpty() && !firstNameInput.isEmpty() && !lastNameInput.isEmpty() && !contactNoInput.isEmpty()
            && !addressInput.isEmpty() && !dateOfBirthInput.isEmpty() && !courseInput.isEmpty()
            && !schoolInput.isEmpty() && !jobLocationInput.isEmpty() && !preferredOccupationInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}

