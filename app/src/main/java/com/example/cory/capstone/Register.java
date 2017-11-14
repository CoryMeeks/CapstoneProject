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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://sql11.ezhostingserver.com;DatabaseName=Msis4363;user=Msis4363;password=Msis4363;";
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

    public class getUser extends AsyncTask<String, String, String> {

        String msg = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass();
                if (con == null) {
                    msg = "Check your Internet connection";
                } else {
                    String query = "SELECT colUserId FROM tblUser WHERE colUserName LIKE '" + params[0] + "';";
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();
                    int userId;
                    if (rs.next()) {
                        userId = rs.getInt("colUserID");
                        stmt.close();
                        if (userId > 0) {
                            msg = "This user already exists";
                            isSuccess = false;
                        }
                    } else {
                        isSuccess = true;
                    }
                    con.close();
                }
            } catch (SQLException se) {
                Log.e("SE-ERR", se.getMessage());
            } catch (Exception e) {
                isSuccess = false;
                msg = e.getMessage();
                Log.e("E-ERR", e.getMessage());
            }

            return msg;
        }
    }

    public class setUser extends AsyncTask<String, String, String> {
        String msg = "";
        Boolean isSuccess = false;


        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass();
                if (con == null) {
                    msg = "Check your Internet connection";
                } else {
                    String query = "INSERT INTO tblUser (colUserName, colUserFirstName, colUserLastName, colUserPassword) VALUES('"+params[0]+"', '"+params[1]+"', '"+params[2]+"', '"+params[3]+"');";
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        stmt.close();
                        isSuccess = true;
                    } else {
                        isSuccess = false;
                    }
                    con.close();
                }
            } catch (SQLException se) {
                Log.e("SE-ERR", se.getMessage());
            } catch (Exception e) {
                isSuccess = false;
                msg = e.getMessage();
                Log.e("E-ERR", e.getMessage());
            }

            return msg;
        }
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

        //Initial check whether username already exists
        getUser check = new getUser();
        check.execute(username);

        //Checks whether content is filled out for all the required fields
        if (username.trim().equals("") || firstname.trim().equals("") || lastname.trim().equals("") || password.trim().equals("")) {
            msg = "Whoops! Looks like you left some information out...";
            Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
        } else {
            if(!confirm.trim().equals(password.trim())) {
                msg = "Uh oh! Your passwords don't match...";
                Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                //Initiates DB call to register new user
                setUser newuser = new setUser();
                newuser.execute(username, firstname, lastname, password);

                //Passes user to their login page
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }
    }

}
