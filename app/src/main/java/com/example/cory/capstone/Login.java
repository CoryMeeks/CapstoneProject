package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent (Login.this, Register.class);
                Login.this.startActivity((registerIntent));
            }
        });
    }

    //Gets user input text data, passes to check function
    public void onLogin (View v) {
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        String userStr = etUsername.getText().toString();
        String passStr = etPassword.getText().toString();

        onLoginManager login = new onLoginManager();
        login.execute(userStr, passStr);
    }

    //Connection to database
    @SuppressLint("NewApi")
    public Connection connectionClass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://sql11.ezhostingserver.com;DatabaseName=Msis4363new;user=Msis4363new;password=Msis4363;";
            con = DriverManager.getConnection(connectionURL);
        } catch (SQLException se) {
            Log.e("SE-ERR", se.getMessage());
        } catch (ClassNotFoundException ce) {
            Log.e("CE-ERR", ce.getMessage());
        } catch (Exception e) {
            Log.e("E-ERR", e.getMessage());
        }
        return con;
    }

    //Checks whether user input data is valid inside the database
    public class onLoginManager extends AsyncTask<String, String, String> {
        String msg = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass();
                if (con == null) {
                    msg = "Check your Internet connection";
                } else {
                    String query = "SELECT colUserID FROM tblUser WHERE colUserName = '" + params[0] + "' AND colUserPassword = '" + params[1] + "'";
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();
                    int userId;
                    String userName = "";
                    if (rs.next()) {
                        userId = rs.getInt("colUserID");
                        stmt.close();
                        if (userId > 0) {
                            //Successful login
                            String doublecheck = "SELECT colUserName FROM tblUser WHERE colUserID = " + userId;
                            PreparedStatement secstmt = con.prepareStatement(doublecheck);
                            ResultSet dblchk = secstmt.executeQuery();
                            if(dblchk.next()) {
                                userName = dblchk.getString("colUserName");
                            } else {
                                Log.e("ERR", "Login double check unsuccessful");
                            }
                            dblchk.close();
                            isSuccess = true;
                        } else {
                            Log.e("ERR", "Login query unsuccessful");
                        }
                    } else {
                        msg = "Login incorrect";
                    }
                    con.close();
                }
            } catch (SQLException se) {
                Log.e("ERR", se.getMessage());
            } catch (Exception e) {
                isSuccess = false;
                msg = e.getMessage();
                Log.e("ERR", e.getMessage());
            }

            return msg;
        }

    }
}