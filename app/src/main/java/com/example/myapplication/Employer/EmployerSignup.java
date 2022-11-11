package com.example.myapplication.Employer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.EmployerOrJobseeker;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class EmployerSignup extends AppCompatActivity {

    private EditText usernameEdit, password1, password2, emailAddress, companyName, companyAddress,
        companyNumber, contactPerson;

    private ProgressBar loading;

    private Button submitBtn;

    String submitURL = "http://192.168.1.9/dole_php/e_register.php"; //remember to check IPCONFIG in command prompt

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
        setContentView(R.layout.e_signup);
        setTitle("Employer Sign up");

        usernameEdit = findViewById(R.id.username);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        emailAddress = findViewById(R.id.emailAddress);
        companyName = findViewById(R.id.companyName);
        companyAddress = findViewById(R.id.companyAddress);
        companyNumber = findViewById(R.id.companyNumber);
        contactPerson = findViewById(R.id.contactPerson);
        loading = findViewById(R.id.loading);
        submitBtn = findViewById(R.id.submitBtn);

        usernameEdit.addTextChangedListener(loginTextWatcher);
        password1.addTextChangedListener(loginTextWatcher);
        password2.addTextChangedListener(loginTextWatcher);
        emailAddress.addTextChangedListener(loginTextWatcher);
        companyName.addTextChangedListener(loginTextWatcher);
        companyAddress.addTextChangedListener(loginTextWatcher);
        companyNumber.addTextChangedListener(loginTextWatcher);
        contactPerson.addTextChangedListener(loginTextWatcher);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1Check = password1.getText().toString().trim();
                String password2Check = password2.getText().toString().trim();
                boolean error = false;
                if (!password1Check.equals(password2Check)) {
                    password1.getText().clear();
                    password2.getText().clear();
                    password1.setError("Passwords do not match!");
                    password2.setError("Passwords do not match!");
                    error = true;
                } if (usernameEdit.getText().toString().length() < 5){
                    error = true;
                    usernameEdit.setError("Username must have more than 5 characters");
                } if (emailAddress.getText().toString().length() < 10 ) {
                    error = true;
                    emailAddress.setError("Please enter a valid e-mail");
                } if (companyName.getText().toString().length() < 5 ) {
                    error = true;
                    companyName.setError("Please enter a valid name");
                } if (companyAddress.getText().toString().length() < 5 ) {
                    error = true;
                    companyAddress.setError("Please enter a valid address");
                } if (companyNumber.getText().toString().length() < 10 ) {
                    error = true;
                    companyNumber.setError("Please enter a valid number");
                } if (contactPerson.getText().toString().length() < 5 ) {
                    error = true;
                    contactPerson.setError("Please enter a valid name");
                } if (usernameEdit.getText().toString().equals("admin") ) {
                    error = true;
                    usernameEdit.setError("Username unavailable.");
                } if (usernameEdit.getText().toString().equals("partner") ) {
                    error = true;
                    usernameEdit.setError("Username unavailable.");
                } if (!error){
                    Submit();
                } else {
                    usernameEdit.requestFocus();
                    usernameEdit.getText().clear();
                    usernameEdit.setError("Please re-enter username");
                }
            }
        });

    }

    private void Submit(){
        loading.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(GONE);

        final String username = this.usernameEdit.getText().toString().trim();
        final String password1 = this.password1.getText().toString().trim();
        final String password2 = this.password2.getText().toString().trim();
        final String email = this.emailAddress.getText().toString().trim();
        final String companyname = this.companyName.getText().toString().trim();
        final String companyaddress = this.companyAddress.getText().toString().trim();
        final String companynumber = this.companyNumber.getText().toString().trim();
        final String contactperson = this.contactPerson.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, submitURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")){
                                Toast.makeText(EmployerSignup.this,"Registration success!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EmployerSignup.this, "Registration error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployerSignup.this, "Registration error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        submitBtn.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("e_username", username);
                params.put("e_password", password1);
                params.put("e_email", email);
                params.put("e_cname", companyname);
                params.put("e_caddress", companyaddress);
                params.put("e_cnumber", companynumber);
                params.put("e_cperson", contactperson);
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
            String password1Input = password1.getText().toString().trim();
            String password2Input = password2.getText().toString().trim();
            String emailAddressInput = emailAddress.getText().toString().trim();
            String companyNameInput = companyName.getText().toString().trim();
            String companyAddressInput = companyAddress.getText().toString().trim();
            String companyNumberInput = companyNumber.getText().toString().trim();
            String contactPersonInput = contactPerson.getText().toString().trim();

            submitBtn.setEnabled(!usernameInput.isEmpty() && !password1Input.isEmpty() && !password2Input.isEmpty()
                    && !emailAddressInput.isEmpty() && !companyNameInput.isEmpty() && !companyAddressInput.isEmpty()
                    && !companyNumberInput.isEmpty() && !contactPersonInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
