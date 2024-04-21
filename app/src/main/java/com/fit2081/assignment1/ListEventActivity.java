package com.fit2081.assignment1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ListEventActivity extends AppCompatActivity {
    Toolbar toolbar;
    FragmentManager fragmentManager;
    FragmentListEvent eventFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_toolbar);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.event_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        eventFrag = new FragmentListEvent();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.eventFragContainer, eventFrag);
        fragmentTransaction.addToBackStack("event_backstack");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}