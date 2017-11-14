package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple database connection class.
 */

public class Connect extends Fragment {
    String msg = "";
    Boolean isSuccess;

    public final static String Extra_String = "com.example.cory.capstone.Extra";

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

    public class setUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {

            } else {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }

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

    public class setEvent extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {

            } else {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            /* TODO */
            return msg;
        }

    }

    public class setVenue extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {

            } else {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            /* TODO */
            return msg;
        }

    }

    public class setTalent extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {

            } else {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            /* TODO */
            return msg;
        }

    }

    public class getExistingUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {

            } else {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

    public class getUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            if (isSuccess) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass();
                if (con == null) {
                    msg = "Check your Internet connection";
                } else {
                    String query = "SELECT colUserID FROM tblUser WHERE colUserName LIKE '" + params[0] + "' AND colUserPassword LIKE '" + params[1] + "'";
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

    /* TODO
     * public class getEvent extends AsyncTask<String, String, String> {}
     * public class getTalent extends AsyncTask<String, String, String> {}
     * public class getVenue extends AsyncTask<String, String, String> {}
     */

}