package com.example.cory.capstone;

import android.provider.ContactsContract;

import java.util.ArrayList;


public class DataManager {

    private static final DataManager ourInstance = new DataManager();

    public ArrayList<Event> eventList = new ArrayList<>();
    public ArrayList<Venue> venueList = new ArrayList<>();
    public ArrayList<Talent> talentList = new ArrayList<>();

    public String username = "";

    static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {

    }

}
