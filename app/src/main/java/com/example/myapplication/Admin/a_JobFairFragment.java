package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class a_JobFairFragment extends Fragment {

    String jobfairURL = "http://192.168.1.9/dole_php/a_jobfair_fragment.php";

    final ArrayList<Jobfair> previousJobFair = new ArrayList<>();
    final ArrayList<String> previousJobFairName= new ArrayList<>();
    final ArrayList<String> previousJobFairId = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.a_fragment_jobfair, container, false);

        final ListView listView = view.findViewById(R.id.a_previousListView);
        final TextView titleText = view.findViewById(R.id.a_TitleText);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
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
                                        Jobfair jobfair = new Jobfair(jfName, jfLoc, jfDate);
                                        previousJobFair.add(jobfair);
                                    }
                                }
                                JobfairListAdapter adapter = new JobfairListAdapter(getContext(), R.layout.jf_adapter_view_layout,
                                        previousJobFair);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < previousJobFair.size(); i++) {
                                            if (position == i) {
                                                Intent intent = new Intent(getContext(), a_JobFair_p.class);
                                                intent.putExtra("jobfairName",previousJobFairName.get(i));
                                                intent.putExtra("jobfairId", previousJobFairId.get(i));
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });

                                listView.setVisibility(View.VISIBLE);
                                titleText.setVisibility(View.VISIBLE);
                            } else if (success.equals("0")){
                                Toast.makeText(getContext(), "Nothing to show here!", Toast.LENGTH_SHORT).show();

                                titleText.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! e" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! e" + error, Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return view;
    }
}
