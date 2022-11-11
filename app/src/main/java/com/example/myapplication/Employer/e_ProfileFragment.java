package com.example.myapplication.Employer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class e_ProfileFragment extends Fragment {

    String eprofileURL = "http://192.168.1.9/dole_php/e_profile_fragment.php";
    String editURL = "http://192.168.1.9/dole_php/e_profile_edit.php";

    private EditText nameEmployerEdit, emailEmployerEdit, numberEmployerEdit, addressEmployerEdit
            ,personEmployerEdit;

    private Button editBtnEprofile, eProfileCancelBtn, eProfileSaveBtn;
    private String eId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.e_fragment_profile, container, false);

        final ProgressBar e_progressBar = view.findViewById(R.id.e_progressBar);

        nameEmployerEdit = view.findViewById(R.id.nameEmployerEdit);
        emailEmployerEdit = view.findViewById(R.id.emailEmployerEdit);
        numberEmployerEdit = view.findViewById(R.id.numberEmployerEdit);
        addressEmployerEdit = view.findViewById(R.id.addressEmployerEdit);
        personEmployerEdit = view.findViewById(R.id.personEmployerEdit);

        editBtnEprofile = view.findViewById(R.id.editBtnEprofileE);
        eProfileCancelBtn = view.findViewById(R.id.eProfileCancelBtnE);
        eProfileSaveBtn = view.findViewById(R.id.eProfileSaveBtnE);

        final Bundle bundle = getArguments();
        eId = bundle.getString("e_id");

        editBtnEprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEmployerEdit.setFocusable(true);
                nameEmployerEdit.setFocusableInTouchMode(true);
                emailEmployerEdit.setFocusable(true);
                emailEmployerEdit.setFocusableInTouchMode(true);
                numberEmployerEdit.setFocusable(true);
                numberEmployerEdit.setFocusableInTouchMode(true);
                addressEmployerEdit.setFocusable(true);
                addressEmployerEdit.setFocusableInTouchMode(true);
                personEmployerEdit.setFocusable(true);
                personEmployerEdit.setFocusableInTouchMode(true);

                nameEmployerEdit.requestFocus();

                editBtnEprofile.setVisibility(View.GONE);
                eProfileCancelBtn.setVisibility(View.VISIBLE);
                eProfileSaveBtn.setVisibility(View.VISIBLE);

                eProfileCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editBtnEprofile.setVisibility(View.VISIBLE);

                        eProfileSaveBtn.setVisibility(View.GONE);

                        e_ProfileFragment profileFragment = new e_ProfileFragment();

                        Bundle bundleId = new Bundle();
                        bundleId.putString("e_id", eId);
                        profileFragment.setArguments(bundleId);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.e_fragment_container,
                                profileFragment).commit();
                    }
                });

                eProfileSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure you want to change your details?");
                        builder.setTitle("Confirm Action");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest request = new StringRequest(Request.Method.POST, editURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String success = jsonObject.getString("success");

                                                    if (success.equals("1")){
                                                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                                        e_ProfileFragment profileFragment = new e_ProfileFragment();

                                                        Bundle bundleId = new Bundle();
                                                        bundleId.putString("e_id", eId);
                                                        profileFragment.setArguments(bundleId);
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.e_fragment_container,
                                                                profileFragment).commit();

                                                    } else {
                                                        Toast.makeText(getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                                                        e_ProfileFragment profileFragment = new e_ProfileFragment();

                                                        Bundle bundleId = new Bundle();
                                                        bundleId.putString("e_id", eId);
                                                        profileFragment.setArguments(bundleId);
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.e_fragment_container,
                                                                profileFragment).commit();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
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
                                        params.put("employerId", eId);
                                        params.put("e_cname", nameEmployerEdit.getText().toString());
                                        params.put("e_cemail", emailEmployerEdit.getText().toString());
                                        params.put("e_address", addressEmployerEdit.getText().toString());
                                        params.put("e_number", numberEmployerEdit.getText().toString());
                                        params.put("e_person", personEmployerEdit.getText().toString());

                                        return params;
                                    }
                                };
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                queue.add(request);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                });

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, eprofileURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("employer");

                        if (success.equals("1")){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String email = object.getString("e_email").trim();
                            String name = object.getString("e_name").trim();
                            String number = object.getString("e_number").trim();
                            String address = object.getString("e_address").trim();
                            String person = object.getString("e_person").trim();

                            nameEmployerEdit.setText(name);
                            emailEmployerEdit.setText(email);
                            numberEmployerEdit.setText(number);
                            addressEmployerEdit.setText(address);
                            personEmployerEdit.setText(person);
                            e_progressBar.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Database Error!" + e , Toast.LENGTH_SHORT).show();
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Database Error!" + error.toString() , Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("e_id", eId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

       return view;
    }
}
