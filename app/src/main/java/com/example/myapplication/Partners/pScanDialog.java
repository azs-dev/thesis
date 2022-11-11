package com.example.myapplication.Partners;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

public class pScanDialog extends AppCompatDialogFragment {

    TextView pnameEdit, pbirthdateEdit, pgenderEdit, paddressEdit;
    Button regBtn;

    ProgressBar progressBar;
    private String retrieveURL = "http://192.168.1.9/dole_php/p_scan_retrieve.php";
    private String insertURL = "http://192.168.1.9/dole_php/p_scan_insert.php";

    private FragmentActivity view6;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.p_dialog_scan,null);
        Bundle bundle = getArguments();
        final String jsId = bundle.getString("js_id");
        final String pId = bundle.getString("p_id");
        final String jfId = bundle.getString("jf_id");

        view6 = getActivity();

        pnameEdit = view.findViewById(R.id.pnameEdit);
        pbirthdateEdit = view.findViewById(R.id.pbirthdateEdit);
        pgenderEdit = view.findViewById(R.id.pgenderEdit);
        paddressEdit = view.findViewById(R.id.paddressEdit);
        regBtn = view.findViewById(R.id.regBtn);
        progressBar = view.findViewById(R.id.progressBar4);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, retrieveURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String attend = jsonObject.getString("attend");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobseeker");

                            if (success.equals("1")) {
                                if (attend.equals("0")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        pnameEdit.setText(object.getString("js_first_name") + " " + object.getString("js_last_name"));
                                        paddressEdit.setText(object.getString("js_address").trim());
                                        pgenderEdit.setText(object.getString("js_gender").trim());
                                        pbirthdateEdit.setText(object.getString("js_dateofbirth").trim());
                                    }
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getContext(), "JOB SEEKER ALREADY REGISTERED!", Toast.LENGTH_SHORT).show();
                                    view6.recreate();
                                    dismiss();
                                }

                            } else if (success.equals("0")){
                                Toast.makeText(getContext(), "Jobseeker NOT registered for this jobfair.", Toast.LENGTH_SHORT).show();
                                view6.recreate();
                                dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                            view6.recreate();
                            dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
                        view6.recreate();
                        dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobseekerId", jsId);
                params.put("jobfairId", jfId);
                params.put("partnerId", pId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view6.recreate();
                        dismiss();
                    }
                });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, insertURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");

                                    if (success.equals("1")){
                                        Toast.makeText(view.getContext(), "Jobseeker registered!", Toast.LENGTH_SHORT).show();
                                        view6.recreate();
                                        dismiss();
                                    } else {
                                        Toast.makeText(view.getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                        view6.recreate();
                                        dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(view.getContext(), "Error! " + e , Toast.LENGTH_SHORT).show();
                                    view6.recreate();
                                    dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(view.getContext(), "Error! " + error , Toast.LENGTH_SHORT).show();
                                view6.recreate();
                                dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("jobseekerId", jsId);
                        params.put("jobfairId", jfId);
                        params.put("partnerId", pId);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(request);
            }
        });

        return builder.create();
    }
}
