package com.example.cory.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onRegister (View v) {
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) findViewById(R.id.etLastName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        String msg;

        String username = etUsername.getText().toString();
        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String password = etPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();

        if (username.trim().equals("") || firstname.trim().equals("") || lastname.trim().equals("") || password.trim().equals("")) {
            msg = "Whoops! Looks like you left some information out...";
        } else {
            if(!confirm.trim().equals(password.trim())) {
                msg = "Uh oh! Your passwords don't match...";
            } else {
                Connect con = new Connect();
                con.setUser(username, firstname, lastname, password);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }
    }
}
