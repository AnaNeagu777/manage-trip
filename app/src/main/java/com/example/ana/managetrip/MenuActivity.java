package com.example.ana.managetrip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    static final int TRIP_ADD_REQUEST_CODE = 1;
    static final int TRIP_EDIT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ManageTripActivity.class);
                startActivityForResult(intent, TRIP_ADD_REQUEST_CODE);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RecyclerViewFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TRIP_ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("MAIN", "Result provided");
            if (data != null && data.hasExtra("entity")) {
                final TripEntity e = (TripEntity) data.getSerializableExtra("entity");

                final RecyclerViewFragment rvf = (RecyclerViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (rvf != null) {
                    rvf.trips.add(e);
                }
                FirebaseFirestore.getInstance().collection("trips").add(e).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        e.setId(documentReference.getId());
                        if (rvf != null) {
                            rvf.adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        } else if (requestCode == TRIP_EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("entity")) {
                TripEntity e = (TripEntity) data.getSerializableExtra("entity");

                RecyclerViewFragment rvf = (RecyclerViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (rvf != null) {
                    int position = data.getIntExtra("position", 0);
                    e.setId(rvf.trips.get(position).getId());
                    rvf.trips.set(position, e);
                    rvf.adapter.notifyDataSetChanged();
                }
                FirebaseFirestore.getInstance().collection("trips").document(e.getId()).set(e);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_favorite:
                Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_us:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:
                Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
