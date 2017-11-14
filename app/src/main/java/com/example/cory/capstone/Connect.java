package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.BoolRes;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple database connection class.
 */

public class Connect {
    String ip = "216.15.165.85.";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "Msis4363";
    String un = "Msis4363";
    String password = "Msis4363";
    String msg = "";
    Boolean isSuccess;

    public final static String Extra_String = "com.example.cory.capstone.Extra";

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String pass, String db, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName(classs);
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + db + ";user=" + user + ";password=" + pass + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERR", se.getMessage());
        } catch (ClassNotFoundException ce) {
            Log.e("ERR", ce.getMessage());
        } catch (Exception e) {
            Log.e("ERR", e.getMessage());
        }
        return connection;
    }

    public Boolean tryPass (String username) {
        Connection con = connectionClass(un, password, db, ip);
        if(con == null) {
            msg = "Check your Internet connection";
        } else {
            String query = "select colUserPassword from tblUser where colUserName = '" + username + "';";
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if(rs.next()) {
                    msg = "Login successful!";
                    isSuccess = true;
                    con.close();
                } else {
                    msg = "Invalid credentials";
                    isSuccess = false;
                }
            } catch (SQLException se) {
                Log.e("ERR", se.getMessage());
            }
        }
        return isSuccess;
    }

    public void setUser (String username, String firstname, String lastname, String password) {
        Connection con = connectionClass(un, password, db, ip);
        if(con == null) {
            msg = "Check your Internet connection";
        } else {
            String query = "insert into dbo.tblUser(colUserName, colFirstName, colLastName, colPassword) values('"+username+"','"+firstname+"','"+lastname+"','"+password+"');";
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    msg = "You've successfully registered!";
                }
            } catch (SQLException se) {
                Log.e("ERR", se.getMessage());
            } catch (Exception e) {
                Log.e("ERR", e.getMessage());
            }
        }
    }

    public void setEvent () {

    }

    public void setVenue () {

    }

    public void setTalent () {

    }
}