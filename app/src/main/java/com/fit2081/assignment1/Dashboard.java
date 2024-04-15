package com.fit2081.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    Button goToNewCateBtn;
    Button goToNewEventBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        goToNewCateBtn = findViewById(R.id.goToNewCategoryBtn);
        goToNewEventBtn = findViewById(R.id.goToNewEventBtn);
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
}