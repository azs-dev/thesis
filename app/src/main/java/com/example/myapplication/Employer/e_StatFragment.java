package com.example.myapplication.Employer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.myapplication.Jobfair;
import com.example.myapplication.JobfairListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class e_StatFragment extends Fragment {

    String jobfairURL = "http://192.168.1.9/dole_php/e_stat_fragment.php";

    final ArrayList<Jobfair> previousJobFair = new ArrayList<>();
    final ArrayList<String> previousJobFairName= new ArrayList<>();
    final ArrayList<String> previousJobFairId = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.e_fragment_stat, container, false);
        final ListView listView = view.findViewById(R.id.previousListView);

        final TextView e_previousText = view.findViewById(R.id.e_previousText);
        final ImageView imageView = view.findViewById(R.id.imageView3);
        final Bundle bundle = getArguments();
        final String eId = bundle.getString("e_id");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (message.equals("error")) {
                                Toast.makeText(getContext(), "Nothing to show here yet.", Toast.LENGTH_SHORT).show();
                            }
                            if (success.equals("1")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("previous");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        for (int y = 0; y < 1; y++) {
                                            String jfName = object.getString("jf_name").trim();
                                            previousJobFairName.add(jfName);
                                            String jfDate = object.getString("jf_date").trim();
                                            String jfLoc = object.getString("jf_location").trim();
                                            String jfId = object.getString("jf_id").trim();
                                            previousJobFairId.add(jfId);
                                            Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                            previousJobFair.add(jobfair);
                                        }
                                    }
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,
                                            previousJobFair);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < previousJobFair.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), e_JobFairP.class);
                                                    intent.putExtra("jobfairName",previousJobFairName.get(i));
                                                    intent.putExtra("jobfairId", previousJobFairId.get(i));
                                                    intent.putExtra("e_id", eId);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                    imageView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.VISIBLE);
                                    e_previousText.setVisibility(View.VISIBLE);

                                } else if (success.equals("0")){
                                Toast.makeText(getContext(), "Nothing to show here yet.", Toast.LENGTH_SHORT).show();

                                    imageView.setVisibility(View.VISIBLE);
                                    e_previousText.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
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
                params.put("e_id", eId);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return view;
    }
}
