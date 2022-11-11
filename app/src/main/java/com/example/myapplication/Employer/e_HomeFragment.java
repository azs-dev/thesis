package com.example.myapplication.Employer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class e_HomeFragment extends Fragment {

    private TextView e_noupcomingText, e_upcomingText, e_ongoingText, e_upcomingText2,
            e_home_label_title_u2,
            e_home_label_loc_u2, e_home_label_title_u, e_home_label_loc_u,
            e_home_label_title_o, e_home_label_loc_o;

    ImageView e_sad;

    String jobfairURL = "http://192.168.1.9/dole_php/e_home_fragment.php";

    final ArrayList<String> jobfairIdU = new ArrayList<String>();
    final ArrayList<String> jobfairIdO = new ArrayList<String>();

    final ArrayList<Jobfair> upcomingList = new ArrayList<>();
    final ArrayList<Jobfair> ongoingList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.e_fragment_home, container, false);

        final Bundle bundle = getArguments();
        final ListView jfListview = view.findViewById(R.id.jfListview);
        final ListView jfListview2 = view.findViewById(R.id.jfListview2);
        final ListView jfListview3 = view.findViewById(R.id.jfListview3);

        final SwipeRefreshLayout refreshLayout = view.findViewById(R.id.e_homefragment_refresh);
        final FragmentManager manager = getFragmentManager();

        e_ongoingText = view.findViewById(R.id.e_ongoingText);
        e_noupcomingText = view.findViewById(R.id.e_noupcomingText);
        e_upcomingText = view.findViewById(R.id.e_upcomingText);
        e_upcomingText2 = view.findViewById(R.id.e_upcomingText2);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        e_sad = view.findViewById(R.id.e_sad);

        e_home_label_title_u = view.findViewById(R.id.e_home_label_title_u);
        e_home_label_loc_u = view.findViewById(R.id.e_home_label_loc_u);
        e_home_label_title_u2 = view.findViewById(R.id.e_home_label_title_u2);
        e_home_label_loc_u2 = view.findViewById(R.id.e_home_label_loc_u2);
        e_home_label_title_o = view.findViewById(R.id.e_home_label_title_o);
        e_home_label_loc_o = view.findViewById(R.id.e_home_label_loc_o);

        final String e_Id = bundle.getString("e_id");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                e_HomeFragment homeFragment = new e_HomeFragment();
                Bundle bundleH = new Bundle();

                bundleH.putString("e_id", e_Id);
                homeFragment.setArguments(bundleH);
                manager.beginTransaction().replace(R.id.e_fragment_container,
                        homeFragment).commit();
            }
        });

        jfListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (jfListview.getChildAt(0) != null) {
                    refreshLayout.setEnabled(jfListview.getFirstVisiblePosition() == 0 && jfListview.getChildAt(0).getTop() == 0);
                }
            }
        });
        jfListview2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (jfListview2.getChildAt(0) != null) {
                    refreshLayout.setEnabled(jfListview2.getFirstVisiblePosition() == 0 && jfListview2.getChildAt(0).getTop() == 0);
                }
            }
        });
        jfListview3.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (jfListview3.getChildAt(0) != null) {
                    refreshLayout.setEnabled(jfListview3.getFirstVisiblePosition() == 0 && jfListview3.getChildAt(0).getTop() == 0);
                }
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, jobfairURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                                JSONArray jsonArrayU = jsonObject.getJSONArray("upcoming");
                                JSONArray jsonArrayO = jsonObject.getJSONArray("ongoing");
                                final ArrayList<String> jobfairNameU = new ArrayList<>();
                                final ArrayList<String> jobfairNameO = new ArrayList<>();

                                if (success.equals("1")) {
                                for (int i = 0; i < jsonArrayU.length(); i++) {
                                    JSONObject object = jsonArrayU.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String jfName = object.getString("jf_name").trim();
                                        jobfairNameU.add(jfName);
                                        String jfDate = object.getString("jf_date").trim();
                                        String jfLoc = object.getString("jf_location").trim();
                                        String jfId = object.getString("jf_id").trim();
                                        jobfairIdU.add(jfId);
                                        Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                        upcomingList.add(jobfair);
                                    }
                                }
                                for (int i = 0; i < jsonArrayO.length(); i++) {
                                    JSONObject object = jsonArrayO.getJSONObject(i);
                                    for (int y = 0; y < 1; y++) {
                                        String jfName = object.getString("jf_name").trim();
                                        jobfairNameO.add(jfName);
                                        String jfDate = object.getString("jf_date").trim();
                                        String jfLoc = object.getString("jf_location").trim();
                                        String jfId = object.getString("jf_id").trim();
                                        jobfairIdO.add(jfId);
                                        Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                        ongoingList.add(jobfair);
                                    }
                                }
                                if (!jobfairNameU.isEmpty()) {
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,
                                            upcomingList);
                                    jfListview.setAdapter(adapter);
                                    jfListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < jobfairNameU.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), e_JobFairU.class);
                                                    intent.putExtra("jobfairName", jobfairNameU.get(i));
                                                    intent.putExtra("jobfairId", jobfairIdU.get(i));
                                                    intent.putExtra("e_id", e_Id);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    jfListview.setVisibility(View.GONE);
                                    e_upcomingText.setVisibility(View.GONE);
                                    e_home_label_title_u.setVisibility(View.GONE);
                                    e_home_label_loc_u.setVisibility(View.GONE);
                                }
                                if (!jobfairNameO.isEmpty()) {
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,
                                            ongoingList);
                                    jfListview2.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                    jfListview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < jobfairNameO.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), e_JobFairO.class);
                                                    intent.putExtra("jobfairName", jobfairNameO.get(i));
                                                    intent.putExtra("jobfairId", jobfairIdO.get(i));
                                                    intent.putExtra("e_id", e_Id);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    jfListview2.setVisibility(View.GONE);
                                    e_ongoingText.setVisibility(View.GONE);
                                    jfListview.setVisibility(View.GONE);
                                    e_upcomingText.setVisibility(View.GONE);
                                    e_ongoingText.setVisibility(View.GONE);
                                    e_home_label_loc_u.setVisibility(View.GONE);
                                    e_home_label_title_u.setVisibility(View.GONE);
                                    e_home_label_title_o.setVisibility(View.GONE);
                                    e_home_label_loc_o.setVisibility(View.GONE);

                                    jfListview3.setVisibility(View.VISIBLE);
                                    e_upcomingText2.setVisibility(View.VISIBLE);
                                    e_home_label_loc_u2.setVisibility(View.VISIBLE);
                                    e_home_label_title_u2.setVisibility(View.VISIBLE);
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,
                                            upcomingList);
                                    jfListview3.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                    jfListview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < jobfairNameU.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), e_JobFairU.class);
                                                    intent.putExtra("jobfairName", jobfairNameU.get(i));
                                                    intent.putExtra("jobfairId", jobfairIdU.get(i));
                                                    intent.putExtra("e_id", e_Id);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });

                                }
                                if (jobfairNameO.isEmpty() && jobfairNameU.isEmpty()) {
                                    jfListview2.setVisibility(View.GONE);
                                    e_ongoingText.setVisibility(View.GONE);
                                    jfListview.setVisibility(View.GONE);
                                    e_upcomingText.setVisibility(View.GONE);
                                    jfListview3.setVisibility(View.GONE);
                                    e_upcomingText2.setVisibility(View.GONE);

                                    e_noupcomingText.setVisibility(View.VISIBLE);
                                    e_sad.setVisibility(View.VISIBLE);
                                }
                            } else {
                                e_upcomingText.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                e_ongoingText.setVisibility(View.GONE);
                                jfListview.setVisibility(View.GONE);
                                jfListview2.setVisibility(View.GONE);
                                e_home_label_loc_u.setVisibility(View.GONE);
                                e_home_label_loc_u2.setVisibility(View.GONE);
                                e_home_label_loc_o.setVisibility(View.GONE);
                                e_home_label_title_u.setVisibility(View.GONE);
                                e_home_label_title_u2.setVisibility(View.GONE);
                                e_home_label_title_o.setVisibility(View.GONE);

                                e_noupcomingText.setVisibility(View.VISIBLE);
                                e_sad.setVisibility(View.VISIBLE);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("e_id", e_Id);
                return params;

            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue1.add(stringRequest);

        return view;
    }
}
