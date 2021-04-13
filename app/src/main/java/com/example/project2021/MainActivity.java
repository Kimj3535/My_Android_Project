package com.example.project2021;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button goToCovidActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToCovidActivity = findViewById(R.id.goToCovidActivity);



        //This creates a transition to load Covid Activity
        Intent covidActivity = new Intent(MainActivity.this, CovidMainActivity.class);
        goToCovidActivity.setOnClickListener(click -> startActivity(covidActivity));


    }
}


