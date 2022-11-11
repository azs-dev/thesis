package com.example.myapplication.Partners;

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
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class pJobseekerDialog extends AppCompatDialogFragment {

    TextView pnameEdit, pbirthdateEdit, pgenderEdit, paddressEdit;

    private String retrieveURL = "http://192.168.1.9/dole_php/p_scan_retrieve.php";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.p_dialog_jobseeker,null);
        Bundle bundle = getArguments();
        final String jsId = bundle.getString("js_id");
        final String pId = bundle.getString("p_id");
        final String jfId = bundle.getString("jf_id");
        final ProgressBar progressBar = view.findViewById(R.id.progressBar7);

        builder.setView(view);

        pnameEdit = view.findViewById(R.id.pnameEdit2);
        pbirthdateEdit = view.findViewById(R.id.pbirthdateEdit2);
        pgenderEdit = view.findViewById(R.id.pgenderEdit2);
        paddressEdit = view.findViewById(R.id.paddressEdit2);

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
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        pnameEdit.setText(object.getString("js_first_name") + " " +object.getString("js_last_name"));
                                        paddressEdit.setText(object.getString("js_address").trim());
                                        pgenderEdit.setText(object.getString("js_gender").trim());
                                        pbirthdateEdit.setText(object.getString("js_dateofbirth").trim());
                                    }
                                    progressBar.setVisibility(View.GONE);
                            } else if (success.equals("0")){
                                Toast.makeText(getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dismiss();
                            Toast.makeText(getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
