package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Employer.EmployerSignup;
import com.example.myapplication.JobSeeker.JobSeekerSignup;


public class EmployerOrJobseeker extends AppCompatActivity {

    Button jsButton, employerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_or_js_activity);
        jsButton = findViewById(R.id.jsButton);
        employerButton = findViewById(R.id.employerButton);

        jsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JobSeekerSignup.class);
                startActivity(intent);
            }
        });

        employerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployerSignup.class);
                startActivity(intent);
            }
        });

    }

}
