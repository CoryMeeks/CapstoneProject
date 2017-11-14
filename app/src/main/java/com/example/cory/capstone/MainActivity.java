package com.example.cory.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShowMenu() {

    }

    public void onClickOpenEvents() {
        Intent i = new Intent(getApplicationContext(), Event.class);
        startActivity(i);
    }

    public void onClickOpenVenues() {
        Intent i = new Intent(getApplicationContext(), Venue.class);
        startActivity(i);
    }

    public void onClickOpenTalent() {
        Intent i = new Intent(getApplicationContext(), Talent.class);
        startActivity(i);
    }

}