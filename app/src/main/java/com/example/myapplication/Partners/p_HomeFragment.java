package com.example.myapplication.Partners;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class p_HomeFragment extends Fragment {

    private TextView p_upcomingText, p_ongoingText, p_upcomingText3, p_noupcomingText,
            p_home_label_title_u2, p_home_label_loc_u2, p_home_label_title_u, p_home_label_loc_u,
            p_home_label_title_o, p_home_label_loc_o;
    private ImageView p_sad;

    String jobfairURL = "http://192.168.1.9/dole_php/p_home_fragment.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.p_fragment_home, container, false);

        final ListView jfListview = view.findViewById(R.id.jfListview); //upcoming
        final ListView jfListview2 = view.findViewById(R.id.jfListview2); //ongoing
        final ListView jfListview3 = view.findViewById(R.id.jfListview3); //upcoming alone

        p_upcomingText = view.findViewById(R.id.p_upcomingText); //upcoming
        p_ongoingText = view.findViewById(R.id.p_ongoingText); //ongoing
        p_upcomingText3 = view.findViewById(R.id.p_upcomingText3); //upcoming alone
        p_noupcomingText = view.findViewById(R.id.p_noupcomingText); // noupcoming

        p_sad = view.findViewById(R.id.p_sad);

        final ArrayList<Jobfair> upcomingList = new ArrayList<>();//for JFlist adapter
        final ArrayList<Jobfair> ongoingList = new ArrayList<>();//for JFlist adapter

        final ArrayList<String> ongoingTitle = new ArrayList<>();//Title of ongoing
        final ArrayList<String> upcomingTitle = new ArrayList<>();//Title of ongoing

        final ArrayList<String> jobfairIdU = new ArrayList<>(); //ID of upcoming
        final ArrayList<String> jobfairIdO = new ArrayList<>(); //ID of ongoing

        final Bundle bundle = getArguments();
        final String p_Id = bundle.getString("id");

        final FragmentManager manager = getFragmentManager();

        p_home_label_title_u = view.findViewById(R.id.p_home_label_title_u);
        p_home_label_loc_u = view.findViewById(R.id.p_home_label_loc_u);
        p_home_label_title_u2 = view.findViewById(R.id.p_home_label_title_u2);
        p_home_label_loc_u2 = view.findViewById(R.id.p_home_label_loc_u2);
        p_home_label_title_o = view.findViewById(R.id.p_home_label_title_o);
        p_home_label_loc_o = view.findViewById(R.id.p_home_label_loc_o);

        final SwipeRefreshLayout refreshLayout = view.findViewById(R.id.p_homefragment_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p_HomeFragment p_homeFragment = new p_HomeFragment();

                Bundle bundleH = new Bundle();
                bundleH.putString("id", p_Id);
                p_homeFragment.setArguments(bundleH);
                manager.beginTransaction().replace(R.id.p_fragment_container,
                        p_homeFragment).commit();
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

                            if (success.equals("1")){
                                for (int i = 0; i <jsonArrayU.length(); i++){
                                    JSONObject object = jsonArrayU.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        String jfName = object.getString("jf_name").trim();
                                        String jfDate = object.getString("jf_date").trim();
                                        String jfLoc = object.getString("jf_location").trim();
                                        String jfId = object.getString("jf_id").trim();

                                        upcomingTitle.add(jfName);
                                        jobfairIdU.add(jfId);
                                        Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                        upcomingList.add(jobfair);
                                    }
                                }
                                for (int i = 0; i <jsonArrayO.length(); i++){
                                    JSONObject object = jsonArrayO.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        String jfName = object.getString("jf_name").trim();
                                        String jfDate = object.getString("jf_date").trim();
                                        String jfLoc = object.getString("jf_location").trim();
                                        String jfId = object.getString("jf_id").trim();

                                        ongoingTitle.add(jfName);
                                        jobfairIdO.add(jfId);
                                        Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                        ongoingList.add(jobfair);
                                    }
                                }
                                if (!upcomingList.isEmpty()) {
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,upcomingList);
                                    jfListview.setAdapter(adapter);
                                    jfListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < upcomingList.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), p_JobFair_u.class);
                                                    intent.putExtra("jobfairId", jobfairIdU.get(i));
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }
                                else {
                                    jfListview.setVisibility(View.GONE);
                                    p_upcomingText.setVisibility(View.GONE);
                                    p_home_label_title_u.setVisibility(View.GONE);
                                    p_home_label_loc_u.setVisibility(View.GONE);
                                }
                                if (!ongoingList.isEmpty()){
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,ongoingList);
                                    jfListview2.setAdapter(adapter);
                                    jfListview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < ongoingList.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), p_JobFair_o.class);
                                                    intent.putExtra("jobfairId", jobfairIdO.get(i));
                                                    intent.putExtra("jobfairName", ongoingTitle.get(i));
                                                    intent.putExtra("partnerId", p_Id);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }
                                else {
                                    jfListview2.setVisibility(View.GONE);
                                    p_ongoingText.setVisibility(View.GONE);
                                    jfListview.setVisibility(View.GONE);
                                    p_upcomingText.setVisibility(View.GONE);
                                    p_home_label_loc_u.setVisibility(View.GONE);
                                    p_home_label_title_u.setVisibility(View.GONE);
                                    p_home_label_title_o.setVisibility(View.GONE);
                                    p_home_label_loc_o.setVisibility(View.GONE);

                                    jfListview3.setVisibility(View.VISIBLE);
                                    p_upcomingText3.setVisibility(View.VISIBLE);
                                    p_home_label_loc_u2.setVisibility(View.VISIBLE);
                                    p_home_label_title_u2.setVisibility(View.VISIBLE);
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,upcomingList);
                                    jfListview3.setAdapter(adapter);
                                    jfListview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < upcomingList.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), p_JobFair_u.class);
                                                    intent.putExtra("jobfairId", jobfairIdU.get(i));
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }
                                if (ongoingList.isEmpty() && upcomingList.isEmpty()){
                                    jfListview2.setVisibility(View.GONE);
                                    p_ongoingText.setVisibility(View.GONE);
                                    jfListview.setVisibility(View.GONE);
                                    p_upcomingText.setVisibility(View.GONE);
                                    jfListview3.setVisibility(View.GONE);
                                    p_upcomingText3.setVisibility(View.GONE);

                                    p_noupcomingText.setVisibility(View.VISIBLE);
                                    p_sad.setVisibility(View.VISIBLE);
                                }
                            } else {
                                p_upcomingText.setVisibility(View.GONE);
                                jfListview2.setVisibility(View.GONE);
                                jfListview.setVisibility(View.GONE);
                                p_ongoingText.setVisibility(View.GONE);
                                p_home_label_loc_u.setVisibility(View.GONE);
                                p_home_label_loc_u2.setVisibility(View.GONE);
                                p_home_label_loc_o.setVisibility(View.GONE);
                                p_home_label_title_u.setVisibility(View.GONE);
                                p_home_label_title_u2.setVisibility(View.GONE);
                                p_home_label_title_o.setVisibility(View.GONE);

                                p_noupcomingText.setVisibility(View.VISIBLE);
                                p_sad.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error! " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return view;
    }
}
