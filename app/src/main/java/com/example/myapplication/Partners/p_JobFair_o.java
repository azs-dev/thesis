package com.example.myapplication.Partners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class p_JobFair_o extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Button pscanBtn, plistBtn;
    private String pId, jfId, jsId, jfName;
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_job_fair_o);

        Intent intent = getIntent();
        jfId = intent.getStringExtra("jobfairId");
        pId = intent.getStringExtra("partnerId");
        jfName = intent.getStringExtra("jobfairName");

        setTitle("Ongoing: " + jfName);
        plistBtn = findViewById(R.id.plistBtn);
        pscanBtn = findViewById(R.id.pscanBtn);

        plistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(p_JobFair_o.this, p_JobSeekerList.class);
                intent.putExtra("partnerId", pId);
                intent.putExtra("jobfairId", jfId);
                startActivity(intent);

            }
        });
    }

    public void pscan (View view) {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        jsId = result.getText();
        openDialog();
        //zXingScannerView.resumeCameraPreview(this);

        zXingScannerView.removeAllViews();
        //finish();
        //startActivity(getIntent());
    }

    public void openDialog(){
        pScanDialog scanDialog = new pScanDialog();
        Bundle bundle = new Bundle();
        bundle.putString("js_id", jsId);
        bundle.putString("jf_id", jfId);
        bundle.putString("p_id", pId);
        scanDialog.setArguments(bundle);
        scanDialog.show(getSupportFragmentManager(),"pScan Dialog");

    }
}
