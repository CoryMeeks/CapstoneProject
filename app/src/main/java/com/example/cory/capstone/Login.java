package com.example.cory.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Connect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        connect = new Connect();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent (Login.this, Register.class);
                Login.this.startActivity((registerIntent));
            }
        });
    }

    public void onLogin (View v) {
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        Button btLogin = (Button) findViewById(R.id.btLogin);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String msg = "";
        if(username.trim().equals("")||password.trim().equals("")) {
            msg = "Please enter your username and password";
        } else {
            Connect con = new Connect();
            Boolean flag = con.tryPass(username);
            if(flag = true) {
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else msg = "Invalid username or password";

        }
    }
}
