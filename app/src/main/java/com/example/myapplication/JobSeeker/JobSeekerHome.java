package com.example.myapplication.JobSeeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobSeekerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_home);

        Intent intent = getIntent();
        String extraName = intent.getStringExtra("name");
        String extraEmail = intent.getStringExtra("email");
        String extraLast = intent.getStringExtra("last");

        //nav bar
        Toolbar toolbar = findViewById(R.id.js_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.js_drawer_layout);
        NavigationView navigationView = findViewById(R.id.js_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0); //idk
        TextView navName = headerView.findViewById(R.id.js_navName); //getting name from log in activity
        TextView navLname = headerView.findViewById(R.id.js_navLastName);
        TextView navEmail = headerView.findViewById(R.id.js_navEmail); //getting email from log in activity
        navLname.setText(extraLast);
        navEmail.setText(extraEmail);
        navName.setText(extraName);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            String idH = getIntent().getStringExtra("id");
            setTitle("Jobseeker Home");
            js_HomeFragment homeFragment = new js_HomeFragment();
            Bundle bundleH = new Bundle();
            bundleH.putString("id", idH);
            homeFragment.setArguments(bundleH);
            manager.beginTransaction().replace(R.id.js_fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.js_nav_home);
        }
        //end of navbaron

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundleId = new Bundle();
        bundleId.putString("id", getIntent().getStringExtra("id"));
        switch (menuItem.getItemId()){
            case R.id.js_nav_home:
                setTitle("Jobseeker Home");
                js_HomeFragment homeFragment = new js_HomeFragment();
                homeFragment.setArguments(bundleId);
                manager.beginTransaction().replace(R.id.js_fragment_container, homeFragment).commit();
                break;
            case R.id.js_nav_profile:
                setTitle("My Profile");
                js_ProfileFragment profileFragment = new js_ProfileFragment();
                profileFragment.setArguments(bundleId);
                manager.beginTransaction().replace(R.id.js_fragment_container, profileFragment).commit();
                break;
            case R.id.js_nav_find:
                setTitle("Match Me!");
                js_MatchFragment matchFragment = new js_MatchFragment();
                matchFragment.setArguments(bundleId);
                manager.beginTransaction().replace(R.id.js_fragment_container, matchFragment).commit();
                break;
            case R.id.js_nav_logout:
                finishAffinity();
                startActivity(new Intent(JobSeekerHome.this, LoginActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

}
