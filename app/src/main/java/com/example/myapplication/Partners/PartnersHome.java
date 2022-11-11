package com.example.myapplication.Partners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class PartnersHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_home);

        Toolbar toolbar = findViewById(R.id.p_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String pname = intent.getStringExtra("p_name");

        drawer = findViewById(R.id.p_drawer_layout);
        NavigationView navigationView = findViewById(R.id.p_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.p_navName); //getting name from log in activity
        navName.setText(pname);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            String pId = getIntent().getStringExtra("p_id");
            p_HomeFragment homeFragment = new p_HomeFragment();
            setTitle("Partner Home");
            Bundle bundleH = new Bundle();
            bundleH.putString("id", pId);
            homeFragment.setArguments(bundleH);
            manager.beginTransaction().replace(R.id.p_fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.p_nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundleId = new Bundle();
        bundleId.putString("id", getIntent().getStringExtra("p_id"));
        switch (menuItem.getItemId()){
            case R.id.p_nav_home:
                setTitle("Partner Home");
                p_HomeFragment p_homeFragment = new p_HomeFragment();
                p_homeFragment.setArguments(bundleId);
                manager.beginTransaction().replace(R.id.p_fragment_container,
                        p_homeFragment).commit();
                break;
            case R.id.p_nav_jobfairs:
                setTitle("Previous Jobfairs");
                p_JobFairFragment p_jobFairFragment = new p_JobFairFragment();
                p_jobFairFragment.setArguments(bundleId);
                manager.beginTransaction().replace(R.id.p_fragment_container,
                        p_jobFairFragment).commit();
                break;
            case R.id.p_nav_logout:
                finishAffinity();
                startActivity(new Intent(PartnersHome.this, LoginActivity.class));
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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
