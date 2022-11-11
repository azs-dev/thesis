package com.example.myapplication.Admin;

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

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_home);


        Toolbar toolbar = findViewById(R.id.a_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.a_drawer_layout);
        NavigationView navigationView = findViewById(R.id.a_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            setTitle("Admin Home");
            manager.beginTransaction().replace(R.id.a_fragment_container, new a_HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.a_nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.a_nav_home:
                setTitle("Admin Home");
                manager.beginTransaction().replace(R.id.a_fragment_container,
                        new a_HomeFragment()).commit();
                break;
            case R.id.a_nav_jobfairs:
                setTitle("Job Fairs");
                manager.beginTransaction().replace(R.id.a_fragment_container,
                        new a_JobFairFragment()).commit();
                break;
            case R.id.a_nav_logout:
                finishAffinity();
                startActivity(new Intent(AdminHome.this, LoginActivity.class));
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
