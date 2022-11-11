package com.example.myapplication.JobSeeker;

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

public class  js_MatchFragment extends Fragment {

    TextView js_noupcomingText, js_upcomingText;
    ImageView js_sad;
    private String jobseekerId;

    final ArrayList<String> jobfairName = new ArrayList<String>();
    final ArrayList<String> jobfairId = new ArrayList<String>();
    final ArrayList<String> jobfairDate = new ArrayList<String>();
    final ArrayList<String> jobfairLoc = new ArrayList<String>();

    final ArrayList<Jobfair> jobfairList = new ArrayList<>();
    String jobfairURL = "http://192.168.1.9/dole_php/js_match_fragment.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.js_fragment_match, container, false);
        final Bundle bundle = getArguments();
        if (bundle != null){
            jobseekerId = bundle.getString("id");
        }

        final ListView jfListview = view.findViewById(R.id.jfListview);
        js_noupcomingText = view.findViewById(R.id.js_noupcomingText);
        js_upcomingText = view.findViewById(R.id.js_upcomingText);
        js_sad = view.findViewById(R.id.js_sad);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String success = jsonObject.getString("success");
                JSONArray jsonArray = jsonObject.getJSONArray("jobfairs");

                    if (success.equals("1")){
                        for (int i = 0; i <jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            for (int y=0;y<1;y++){
                                String jfId = object.getString("jf_id").trim();
                                jobfairId.add(jfId);
                                String jfName = object.getString("jf_name").trim();
                                jobfairName.add(jfName);
                                String jfDate = object.getString("jf_date").trim();
                                jobfairDate.add(jfDate);
                                String jfLoc = object.getString("jf_location").trim();
                                jobfairLoc.add(jfLoc);
                                Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                jobfairList.add(jobfair);
                            }
                        }
                        JobfairListAdapter jobfairListAdapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout, jobfairList);
                        jfListview.setAdapter(jobfairListAdapter);
                        jfListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (int i = 0; i < jobfairName.size(); i++) {
                                    if (position == i) {
                                        Intent intent = new Intent(getContext(), js_MatchMe.class);
                                        intent.putExtra("jobfairName",jobfairName.get(i));
                                        intent.putExtra("jobfairId",jobfairId.get(i));
                                        intent.putExtra("jobseekerId", jobseekerId);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }
                    else {
                        js_upcomingText.setVisibility(View.GONE);
                        js_noupcomingText.setVisibility(View.VISIBLE);
                        js_sad.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue1.add(stringRequest);


        return view;
    }
}
