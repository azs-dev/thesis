package com.example.myapplication.Employer;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class EmployerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    FragmentManager manager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_home);

        Intent intent = getIntent();
        String extraName = intent.getStringExtra("e_cname");
        String extraEmail = intent.getStringExtra("e_email");

        Toolbar toolbar = findViewById(R.id.e_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.e_drawer_layout);
        NavigationView navigationView =findViewById(R.id.e_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navEname = headerView.findViewById(R.id.e_navName);
        TextView navEemail = headerView.findViewById(R.id.e_navEmail);
        navEname.setText(extraName);
        navEemail.setText(extraEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            setTitle("Employer Home");
            String e_idH = getIntent().getStringExtra("e_id");

            e_HomeFragment homeFragment = new e_HomeFragment();
            Bundle bundleH = new Bundle();

            bundleH.putString("e_id", e_idH);
            homeFragment.setArguments(bundleH);

            manager.beginTransaction().replace(R.id.e_fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.e_nav_home);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.e_nav_home:
                setTitle("Employer Home");
                String e_idH = getIntent().getStringExtra("e_id");

                e_HomeFragment homeFragment = new e_HomeFragment();
                Bundle bundleH = new Bundle();

                bundleH.putString("e_id", e_idH);
                homeFragment.setArguments(bundleH);
                manager.beginTransaction().replace(R.id.e_fragment_container, homeFragment)
                        .commit();
                break;
            case R.id.e_nav_profile:
                setTitle("My Profile");
                String e_idP = getIntent().getStringExtra("e_id");
                e_ProfileFragment profileFragment = new e_ProfileFragment();
                Bundle bundleP = new Bundle();

                bundleP.putString("e_id", e_idP);
                profileFragment.setArguments(bundleP);
                manager.beginTransaction().replace(R.id.e_fragment_container, profileFragment)
                        .commit();
                break;
            case R.id.e_nav_statistics:
                setTitle("Statistics");
                String e_idS = getIntent().getStringExtra("e_id");
                e_StatFragment statFragment = new e_StatFragment();
                Bundle bundleS = new Bundle();

                bundleS.putString("e_id", e_idS);
                statFragment.setArguments(bundleS);
                manager.beginTransaction().replace(R.id.e_fragment_container, statFragment)
                        .commit();
                break;
            case R.id.e_nav_logout:
                finishAffinity();
                startActivity(new Intent(EmployerHome.this, LoginActivity.class));
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
