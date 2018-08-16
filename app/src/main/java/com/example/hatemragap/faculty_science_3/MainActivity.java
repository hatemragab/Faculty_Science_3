package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hatemragap.faculty_science_3.fragment.algorizem;
import com.example.hatemragap.faculty_science_3.fragment.hekalo_data;
import com.example.hatemragap.faculty_science_3.fragment.intro_to;
import com.example.hatemragap.faculty_science_3.fragment.intro_to_build;
import com.example.hatemragap.faculty_science_3.fragment.physics_math;
import com.example.hatemragap.faculty_science_3.fragment.physics_alkam;
import com.example.hatemragap.faculty_science_3.fragment.physics_naway;
import com.example.hatemragap.faculty_science_3.fragment.physics_solid;
import com.example.hatemragap.faculty_science_3.fragment.program_language;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        toolbar = findViewById(R.id.maintool);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drower);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.algorizem:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new algorizem()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.hekalo_data:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new hekalo_data()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.intri_to:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new intro_to()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.intri_to_bild:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new intro_to_build()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.physics_alkam:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new physics_alkam()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.physics_reuada:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new physics_math()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.physics_solid:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new physics_solid()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.physics_naway:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new physics_naway()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.program_language:
                getSupportFragmentManager().beginTransaction().replace(R.id.framContainer, new program_language()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.alluser:
                startActivity(new Intent(MainActivity.this,AllUsers.class));
                break;


        }


        return true;
    }
}
