package com.fit2081.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class Dashboard extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager dashboardFragManager;
    FragmentListCategory categoryFrag;
    Button goToNewCateBtn;
    Button goToNewEventBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_drawer);

        toolbar = findViewById(R.id.dashboard_toolbar);
        fab = findViewById(R.id.dashboard_fab);
        drawer = findViewById(R.id.dashboard_drawer);
        navigationView = findViewById(R.id.dashboard_nav_view);
//        goToNewCateBtn = findViewById(R.id.goToNewCategoryBtn);
//        goToNewEventBtn = findViewById(R.id.goToNewEventBtn);

        // set up toolbar and then drawer, nav view
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // set up category fragment here
        dashboardFragManager = getSupportFragmentManager();

        // set up floating action button, another way to assign method to a button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello, I am floating action button", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", (new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(Dashboard.this, "I am undoing...", Toast.LENGTH_SHORT).show();
                            }
                        }))
                        .show();
            }
        });
    }

    public void onGoToNewCategoryClick(View view) {
        Intent goToNewCategoryIntent = new Intent(this, AddNewEventCategory.class);
        startActivity(goToNewCategoryIntent);
        finish();
    }

    public void onGoToNewEventClick(View view) {
        Intent goToNewEventIntent = new Intent(this, AddNewEvent.class);
        startActivity(goToNewEventIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_more_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id== R.id.option_refresh) {
            categoryFrag = new FragmentListCategory();
            FragmentTransaction fragmentTransaction = dashboardFragManager.beginTransaction();
            fragmentTransaction.replace(R.id.dashboardCategoryFragContainer, categoryFrag);
            fragmentTransaction.addToBackStack("dashboard_stack");
            fragmentTransaction.commit();
        } else if (id == R.id.option_clear_event_form) {
            Toast.makeText(this, "Hello2", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.option_delete_categories) {
            // delete from shared preferences
            SharedPreferences sharedPref = getSharedPreferences(KeyStore.CATEGORY_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(KeyStore.CATEGORY_LIST);
            editor.apply();
            Toast.makeText(this, "Categories are removed", Toast.LENGTH_SHORT).show();

            // update fragment with new fragment
            categoryFrag = new FragmentListCategory();
            FragmentTransaction fragmentTransaction = dashboardFragManager.beginTransaction();
            fragmentTransaction.replace(R.id.dashboardCategoryFragContainer, categoryFrag);
            fragmentTransaction.addToBackStack("dashboard_stack");
            fragmentTransaction.commit();
        } else if (id == R.id.option_delete_events) {
            Toast.makeText(this, "Hello4", Toast.LENGTH_SHORT).show();
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.nav_add_category) {
                Intent goToNewCategoryIntent = new Intent(getApplicationContext(), AddNewEventCategory.class);
                startActivity(goToNewCategoryIntent);
            } else if ( id == R.id.nav_view_categories) {
                Intent viewCategoriesIntent = new Intent(getApplicationContext(), ListCategoryActivity.class);
                startActivity(viewCategoriesIntent);
            } else if (id == R.id.nav_logout) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(signUpIntent);
                finish(); // remove activity from stack
            }

            // close the drawer
            drawer.closeDrawers();
            // tell the OS
            return true;
        }
    }
}