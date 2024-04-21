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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Dashboard extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager dashboardFragManager;
    FragmentListCategory categoryFrag;
    EditText eventIdView;
    EditText eventNameView;
    EditText categoryIdView;
    EditText ticketsView;
    Switch isActiveView;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_drawer);

        toolbar = findViewById(R.id.dashboard_toolbar);
        fab = findViewById(R.id.dashboard_fab);
        drawer = findViewById(R.id.dashboard_drawer);
        navigationView = findViewById(R.id.dashboard_nav_view);
        eventIdView = findViewById(R.id.dashboard_event_id_input);
        eventNameView = findViewById(R.id.dashboard_event_name_input);
        categoryIdView = findViewById(R.id.dashboard_event_category_id_input);
        ticketsView = findViewById(R.id.dashboard_event_tickets_input);
        isActiveView = findViewById(R.id.dashboard_event_is_active_input);


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
                if (isEventFormValid()) {
                    String newEventId = addNewEvent();
                    Snackbar.make(view, "New event saved", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    undoNewEvent(newEventId);
                                    Toast.makeText(Dashboard.this, "Undo successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void undoNewEvent(String newEventId) {
        SharedPreferences sharedPref = getSharedPreferences(KeyStore.EVENT_FILE, MODE_PRIVATE);
        String eventListRestoredString = sharedPref.getString(KeyStore.EVENT_LIST, "[]");
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        ArrayList<Event> eventListRestored = gson.fromJson(eventListRestoredString,type);
        eventListRestored.remove(eventListRestored.size()-1);

        String newEventListString = gson.toJson(eventListRestored);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KeyStore.EVENT_LIST, newEventListString);
        editor.apply();
    }

    private String addNewEvent() {
        // get input field values
        String eventNameInput = eventNameView.getText().toString();
        String categoryIdInput = categoryIdView.getText().toString();
        int ticketsInput = ticketsView.getText().toString().isEmpty() ? 0 : Integer.parseInt(ticketsView.getText().toString());
        boolean isActiveInput = isActiveView.isChecked();
        String eventId = generateEventId();

        // SAVE INTO SHARED PREFERENCES WITH EXISTING DATA
        // get event list from shared preferences
        SharedPreferences sharedPref = getSharedPreferences(KeyStore.EVENT_FILE, MODE_PRIVATE);
        String eventListRestoredString = sharedPref.getString(KeyStore.EVENT_LIST, "[]");
        // parse into java objects to add a new event
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        ArrayList<Event> eventListRestored = gson.fromJson(eventListRestoredString,type);
        eventListRestored.add(new Event(eventId, eventNameInput, categoryIdInput, ticketsInput, isActiveInput));

        // parse back into string to save into the preferences
        String newEventListString = gson.toJson(eventListRestored);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KeyStore.EVENT_LIST, newEventListString);
        editor.apply();

        // populate the event ID
        eventIdView.setText(eventId);

        return eventId;
    }

    private boolean isEventFormValid() {
        // validate event name
        String eventNameInput = eventNameView.getText().toString();
        if (!isEventNameValid(eventNameInput)) {
            Toast.makeText(this, "Invalid event name", Toast.LENGTH_SHORT).show();
            return false;
        }
        // validate category id
        String categoryIdInput = categoryIdView.getText().toString();
        if (!isCategoryIdInputValid(categoryIdInput)) {
            Toast.makeText(this, "Invalid category id", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isEventNameValid(String eventNameInput) {
        String[] eventNameInputArray = eventNameInput.split("\\s+");
        boolean isValid = true;

        // rule 1: no characters other than alpha-numeric
        for (String s: eventNameInputArray) {
            if (!s.matches("[a-zA-Z0-9]+")) {
                isValid = false;
            }
        }
        // rule 2: if length == 1 and the string is made up of only numeric => Invalid
        if (eventNameInputArray.length == 1 && eventNameInputArray[0].matches("[0-9]+")) {
            isValid = false;
        }
        return isValid;
    }

    private boolean isCategoryIdInputValid(String categoryIdInput) {
        SharedPreferences sharedPref = getSharedPreferences(KeyStore.CATEGORY_FILE, MODE_PRIVATE);
        String categoryListRestoredString = sharedPref.getString(KeyStore.CATEGORY_LIST, "[]");
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        ArrayList<Category> categoryList = gson.fromJson(categoryListRestoredString, type);

        for (Category c: categoryList) {
            if (c.getId().equals(categoryIdInput)) {
                return true;
            }
        }
        return false;
    }

    private String generateEventId() {
        String[] alphabets = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int num = (int) Math.round(Math.random() * 100) % 26;
        int num2 = (int) Math.round(Math.random() * 100) % 26;
        StringBuilder res = new StringBuilder(String.format("%s%s%s-", "E", alphabets[num], alphabets[num2]));
        for (int i = 0; i < 5; i++) {
            long num3 = Math.round(Math.random() * 100) % 10;
            res.append(num3);
        }

        return String.valueOf(res);
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
            eventIdView.setText("");
            eventNameView.setText("");
            categoryIdView.setText("");
            ticketsView.setText("");
            isActiveView.setChecked(false);
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
            SharedPreferences sharedPref = getSharedPreferences(KeyStore.EVENT_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(KeyStore.EVENT_LIST);
            editor.apply();
            Toast.makeText(this, "Events are removed", Toast.LENGTH_SHORT).show();
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

            if ( id == R.id.nav_view_categories) {
                Intent viewCategoriesIntent = new Intent(getApplicationContext(), ListCategoryActivity.class);
                startActivity(viewCategoriesIntent);
            } else if (id == R.id.nav_add_category) {
                Intent goToNewCategoryIntent = new Intent(getApplicationContext(), AddNewEventCategory.class);
                startActivity(goToNewCategoryIntent);
            } else if (id == R.id.nav_view_events) {
                Intent viewEventsIntent = new Intent(getApplicationContext(), ListEventActivity.class);
                startActivity(viewEventsIntent);
            } else {
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