package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class a_HomeFragment extends Fragment {

    String jobfairURL = "http://192.168.1.9/dole_php/a_home_fragment.php";

    TextView a_noupcomingText, a_upcomingText, a_ongoingText, a_upcomingText2,
            a_home_label_title_u2, a_home_label_loc_u2, a_home_label_title_u, a_home_label_loc_u,
            a_home_label_title_o, a_home_label_loc_o;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.a_fragment_home, container, false);

        final ListView jfongoingLv = view.findViewById(R.id.a_ongoingLv);
        final ListView jfupcomingLv = view.findViewById(R.id.a_upcomingLv);
        final ListView jfupcomingLv2 = view.findViewById(R.id.a_upcomingLv2);

        a_noupcomingText = view.findViewById(R.id.a_jftext);
        a_ongoingText = view.findViewById(R.id.a_ongoingText);
        a_upcomingText = view.findViewById(R.id.a_upcomingText);
        a_upcomingText2 = view.findViewById(R.id.a_upcomingText2);

        a_home_label_title_u = view.findViewById(R.id.a_home_label_title_u);
        a_home_label_loc_u = view.findViewById(R.id.a_home_label_loc_u);
        a_home_label_title_u2 = view.findViewById(R.id.a_home_label_title_u2);
        a_home_label_loc_u2 = view.findViewById(R.id.a_home_label_loc_u2);
        a_home_label_title_o = view.findViewById(R.id.a_home_label_title_o);
        a_home_label_loc_o = view.findViewById(R.id.a_home_label_loc_o);

        final ProgressBar progressBar = view.findViewById(R.id.a_progressBar);
        final ImageView a_sad = view.findViewById(R.id.a_sad);

        final ArrayList<Jobfair> upcomingList = new ArrayList<>();
        final ArrayList<Jobfair> ongoingList = new ArrayList<>();

        final ArrayList<String> jobfairUiD = new ArrayList<String>();//jf ID
        final ArrayList<String> jobfairOiD = new ArrayList<String>();//jf ID

        final ArrayList<String> jobfairOName = new ArrayList<String>();//jf name
        final ArrayList<String> jobfairUName = new ArrayList<String>();//jf name

        final SwipeRefreshLayout refreshLayout = view.findViewById(R.id.a_fragment_home_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().replace(R.id.a_fragment_container,
                        new a_HomeFragment()).commit();
            }
        });

        Button createJfBtn = view.findViewById(R.id.createJfBtn);
        createJfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateJfDialog createJfDialog = new CreateJfDialog();
                createJfDialog.show(getActivity().getSupportFragmentManager(),"Vacancy Dialog");
            }
        });

        //SCROLL LISTVIEW
        jfupcomingLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (jfupcomingLv.getChildAt(0) != null) {
                    refreshLayout.setEnabled(jfupcomingLv.getFirstVisiblePosition() == 0 && jfupcomingLv.getChildAt(0).getTop() == 0);
                }
            }
        });
        jfongoingLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (jfongoingLv.getChildAt(0) != null) {
                    refreshLayout.setEnabled(jfongoingLv.getFirstVisiblePosition() == 0 && jfongoingLv.getChildAt(0).getTop() == 0);
                }
            }
        });
        jfupcomingLv2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (jfupcomingLv2.getChildAt(0) != null) {
                    refreshLayout.setEnabled(jfupcomingLv2.getFirstVisiblePosition() == 0 && jfupcomingLv2.getChildAt(0).getTop() == 0);
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

                                        Jobfair jobfair = new Jobfair(jfName, jfLoc,jfDate);
                                        upcomingList.add(jobfair);
                                        jobfairUiD.add(jfId);
                                        jobfairUName.add(jfName);
                                    }
                                }

                                for (int i = 0; i <jsonArrayO.length(); i++){
                                    JSONObject object = jsonArrayO.getJSONObject(i);
                                    for (int y=0;y<1;y++){
                                        String jfName = object.getString("jf_name").trim();
                                        String jfDate = object.getString("jf_date").trim();
                                        String jfLoc = object.getString("jf_location").trim();
                                        String jfId = object.getString("jf_id").trim();

                                        Jobfair jobfair = new Jobfair(jfName,jfLoc,jfDate);
                                        ongoingList.add(jobfair);
                                        jobfairOiD.add(jfId);
                                        jobfairOName.add(jfName);
                                    }
                                }
                                if (!upcomingList.isEmpty()){
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,upcomingList);
                                    jfupcomingLv.setAdapter(adapter);
                                    //ONCLICK
                                    jfupcomingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < upcomingList.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), a_JobFair_u.class);
                                                    intent.putExtra("jobfairId", jobfairUiD.get(i));
                                                    intent.putExtra("jobfairName", jobfairUName.get(i));
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    jfupcomingLv.setVisibility(View.GONE);
                                    a_upcomingText.setVisibility(View.GONE);
                                    a_home_label_title_u.setVisibility(View.GONE);
                                    a_home_label_loc_u.setVisibility(View.GONE);
                                }
                                if (!ongoingList.isEmpty()){
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,ongoingList);
                                    jfongoingLv.setAdapter(adapter);
                                    jfongoingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < ongoingList.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), a_JobFair_o    .class);
                                                    intent.putExtra("jobfairId", jobfairOiD.get(i));
                                                    intent.putExtra("jobfairName", jobfairOName.get(i));
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    jfupcomingLv.setVisibility(View.GONE);
                                    a_upcomingText.setVisibility(View.GONE);
                                    jfongoingLv.setVisibility(View.GONE);
                                    a_ongoingText.setVisibility(View.GONE);
                                    a_home_label_loc_u.setVisibility(View.GONE);
                                    a_home_label_title_u.setVisibility(View.GONE);
                                    a_home_label_title_o.setVisibility(View.GONE);
                                    a_home_label_loc_o.setVisibility(View.GONE);

                                    jfupcomingLv2.setVisibility(View.VISIBLE);
                                    a_upcomingText2.setVisibility(View.VISIBLE);
                                    a_home_label_loc_u2.setVisibility(View.VISIBLE);
                                    a_home_label_title_u2.setVisibility(View.VISIBLE);
                                    JobfairListAdapter adapter = new JobfairListAdapter(getContext(),R.layout.jf_adapter_view_layout,upcomingList);
                                    jfupcomingLv2.setAdapter(adapter);
                                    //ONCLICK
                                    jfupcomingLv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i < upcomingList.size(); i++) {
                                                if (position == i) {
                                                    Intent intent = new Intent(getContext(), a_JobFair_u.class);
                                                    intent.putExtra("jobfairId", jobfairUiD.get(i));
                                                    intent.putExtra("jobfairName", jobfairUName.get(i));
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }
                                if (ongoingList.isEmpty() && upcomingList.isEmpty()){
                                    jfupcomingLv.setVisibility(View.GONE);
                                    a_upcomingText.setVisibility(View.GONE);
                                    jfongoingLv.setVisibility(View.GONE);
                                    a_ongoingText.setVisibility(View.GONE);
                                    jfupcomingLv2.setVisibility(View.GONE);
                                    a_upcomingText2.setVisibility(View.GONE);

                                    a_noupcomingText.setVisibility(View.VISIBLE);
                                    a_sad.setVisibility(View.VISIBLE);
                                }
                            } else {
                                a_upcomingText.setVisibility(View.GONE);
                                a_ongoingText.setVisibility(View.GONE);
                                jfongoingLv.setVisibility(View.GONE);
                                jfupcomingLv.setVisibility(View.GONE);
                                a_home_label_loc_u.setVisibility(View.GONE);
                                a_home_label_loc_u2.setVisibility(View.GONE);
                                a_home_label_loc_o.setVisibility(View.GONE);
                                a_home_label_title_u.setVisibility(View.GONE);
                                a_home_label_title_u2.setVisibility(View.GONE);
                                a_home_label_title_o.setVisibility(View.GONE);
                                a_noupcomingText.setVisibility(View.VISIBLE);
                                a_sad.setVisibility(View.VISIBLE);
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! " + error, Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return view;

    }

}
