package com.example.cory.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Event extends AppCompatActivity {

    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        /* arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, R.layout.todo_row,R.id.textView, arrayListToDo);
        ListView listView = (ListView)findViewById(R.id.toDoList);
        listView.setAdapter(arrayAdapterToDo);
        unregisterForContextMenu(listView);
        */
    }
}
