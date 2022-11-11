package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.AdminHome;
import com.example.myapplication.Employer.EmployerHome;
import com.example.myapplication.JobSeeker.JobSeekerHome;
import com.example.myapplication.Partners.PartnersHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText usernameEditText, passwordEditText;
    private ProgressBar loading;

    String URL_LOGIN = "http://192.168.1.9/dole_php/login.php";

    //http://somandaraziz.com/connect.php //type in manually
    //http://192.168.1.9/dole_php/login.php

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
        setContentView(R.layout.activity_main);
        //signup//
        TextView signupTxt = findViewById(R.id.signupTxt);
        final int color = getResources().getColor(R.color.colorPrimary);
        String signup = "Not a member yet? Click HERE to sign up.";
        SpannableString ss = new SpannableString(signup);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
            @Override
            public void onClick(@NonNull View widget) {
                signup();
            }
        };
        ss.setSpan(clickableSpan,24,28,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTxt.setText(ss);
        signupTxt.setMovementMethod(LinkMovementMethod.getInstance());
        //end of sign up//

        loading = findViewById(R.id.loading);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername = usernameEditText.getText().toString().trim();
                String mPass = passwordEditText.getText().toString().trim();

                if (!mUsername.isEmpty() || !mPass.isEmpty()){
                    Login(mUsername,mPass);
                } else {
                    usernameEditText.setError("Please input username");
                    passwordEditText.setError("Please input password");
                }
            }
        });

    }

    public void signup() {
        Intent intent = new Intent(getApplicationContext(), EmployerOrJobseeker.class);
        startActivity(intent);
    }

    private void Login(final String username, final String password){

        loading.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String user = jsonObject.getString("user");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){
                                if(user.equals("jobseeker")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String fname = object.getString("js_first_name").trim();
                                        String lname = object.getString("js_last_name").trim();
                                        String email = object.getString("js_email").trim();
                                        String id = object.getString("js_id").trim();

                                        Intent intent = new Intent(LoginActivity.this, JobSeekerHome.class);
                                        intent.putExtra("name", fname);
                                        intent.putExtra("email", email);
                                        intent.putExtra("last", lname);
                                        intent.putExtra("id",id);
                                        intent.putExtra("finish", true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        loading.setVisibility(View.GONE);
                                        loginBtn.setVisibility(View.VISIBLE);

                                    }
                                } else if (user.equals("employer")){
                                    for (int i = 0; i < jsonArray.length(); i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String valid = object.getString("e_validation").trim();
                                        if (valid.equals("0")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
                                            builder.setTitle("Verify Account");
                                            builder.setMessage("Please verify your account over at DOLE to be able to log in.");
                                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.create();
                                            builder.show();
                                            loading.setVisibility(View.GONE);
                                            loginBtn.setVisibility(View.VISIBLE);

                                        } else {
                                            String cname = object.getString("e_cname").trim();
                                            String cemail = object.getString("e_email").trim();
                                            String cid = object.getString("e_id").trim();

                                            Intent intent = new Intent(LoginActivity.this, EmployerHome.class);
                                            intent.putExtra("e_cname", cname);
                                            intent.putExtra("e_email", cemail);
                                            intent.putExtra("e_id", cid);
                                            startActivity(intent);

                                            loading.setVisibility(View.GONE);
                                            loginBtn.setVisibility(View.VISIBLE);
                                        }

                                    }
                                } else if (user.equals("partner")){
                                    for (int i = 0; i < jsonArray.length(); i++){
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String pname = object.getString("p_name").trim();
                                        String pid = object.getString("p_id").trim();

                                        Intent intent = new Intent(LoginActivity.this, PartnersHome.class);
                                        intent.putExtra("p_name", pname);
                                        intent.putExtra("p_id", pid);
                                        startActivity(intent);

                                        loading.setVisibility(View.GONE);
                                        loginBtn.setVisibility(View.VISIBLE);


                                    }
                                } else if (user.equals("admin")){
                                    for (int i = 0; i < jsonArray.length(); i++){
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String aname = object.getString("a_name").trim();

                                        Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                                        intent.putExtra("a_name", aname);
                                        startActivity(intent);

                                        loading.setVisibility(View.GONE);
                                        loginBtn.setVisibility(View.VISIBLE);


                                    }
                                }
                            } else if(success.equals("0")) {
                                Toast.makeText(LoginActivity.this,"Invalid username or password!", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                loginBtn.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "User doesn't exist!" + e , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Database Error!" + error.toString() , Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
