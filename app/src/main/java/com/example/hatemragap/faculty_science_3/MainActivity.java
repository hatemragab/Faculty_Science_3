package com.example.hatemragap.faculty_science_3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DatabaseReference database;
    private TextView usernameTextView, userMailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set main activity
        setContentView(R.layout.activity_main);

        // support toolbar
        toolbar = findViewById(R.id.maintool);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        // navigation view
        bottomNavigationView = findViewById(R.id.mainBottomNav);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        usernameTextView = view.findViewById(R.id.username_textView);
        userMailTextView = view.findViewById(R.id.userMail_textView);

        // to show navigationView icon's
        navigationView.setItemIconTintList(null);

        // get user from intent


        //
        drawerLayout = findViewById(R.id.drower);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        database = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                usernameTextView.setText(name);
                userMailTextView.setText(email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });


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
                        startActivity(new Intent(MainActivity.this, Algorithms_New.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activty_menu, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.upload:

//your code


                startActivity(new Intent(MainActivity.this, ControlPanel.class));
                break;
        }

        return true;

    }
}
