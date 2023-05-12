package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.controller.HomeController;

public class HomeActivity extends AppCompatActivity {
    private Button goClientButton;
    private HomeController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        init();

        // Initialize controller
        controller = new HomeController(this);

        // Set click listener for GoClientButton
        goClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onGoClientButtonClicked();
            }
        });
    }

    private void init() {
        // Find the GoClientButton in the layout and assign it to the goClientButton variable
        goClientButton = findViewById(R.id.btnclt);
    }

    public void navigateToClientActivity() {
        // Create a new intent to navigate to the ClientActivity
        Intent returnIntent = new Intent(HomeActivity.this, ClientActivity.class);

        // Start the ClientActivity
        startActivity(returnIntent);
    }
}