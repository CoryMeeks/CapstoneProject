package com.example.cory.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickOpenEvents(View v) {
        Intent i = new Intent(getApplicationContext(), Event.class);
        startActivity(i);
    }

    public void onClickOpenVenues(View v) {
        Intent i = new Intent(getApplicationContext(), Venue.class);
        startActivity(i);
    }

    public void onClickOpenTalent(View v) {
        Intent i = new Intent(getApplicationContext(), Talent.class);
        startActivity(i);
    }

}