package com.example.hatemragap.faculty_science_3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

        // set main activity
        setContentView(R.layout.activity_main);

        // support toolbar
        toolbar = findViewById(R.id.maintool);
        setSupportActionBar(toolbar);

        // navigation view
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //
        drawerLayout = findViewById(R.id.drower);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }


    @Override
    public void onBackPressed() {
        // don't change view if the drawer is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.algorithms:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("choose the lectures you want");

                builder.setPositiveButton("this year", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MainActivity.this, Algorizm_New.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });

                builder.setNegativeButton("old years", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MainActivity.this, OldAlgorithms.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });

                builder.setCancelable(false);
                builder.show();

                break;

            case R.id.hekalo_data:

                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
