package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.order;
import static com.example.cory.capstone.R.id.lv_venue;

public class Venue extends AppCompatActivity {

    //Holds the dynamic database driven content
    private ArrayList<String> content;
    //Is the bridge between the ListView GUI and the data in the Array
    private ArrayAdapter<String> contentadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        content = new ArrayList<>();
        //Gets dynamic XML from row.xml and the tv_description ID, and passes it to content Array
        contentadapter = new ArrayAdapter<>(this, R.layout.row, R.id.tv_venuename, content);
        final ListView lv = (ListView) findViewById(lv_venue);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //Defines the ListView as dynamic
        lv.setAdapter(contentadapter); //Defines adapter to use

        GetData pullcontent = new GetData();
        pullcontent.execute();
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

    public class GetData extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                Connection con = connectionClass();
                if (con == null) {
                    msg = "Check your Internet connection";
                } else {
                    String query = "SELECT colVenueName, colVenueDesc FROM tblVenue";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    content.clear();

                    if (rs.next()) {
                        while (rs.next()) {
                            content.add(rs.getString("colVenueDesc"));
                        }
                        msg = "Something to display";
                        isSuccess = true;
                    } else {
                        msg = "Nothing to display";
                    }
                    stmt.close();
                    con.close();
                }
            } catch (SQLException se) {
                Log.e("SE-ERR", se.getMessage());
            } catch (Exception e) {
                isSuccess = false;
                msg = e.getMessage();
                Log.e("E-ERR", e.getMessage());
            }

            return content;
        }

        @Override
        protected void onPostExecute(ArrayList<String> r) {
            if (isSuccess) {
                //do something

                Toast.makeText(Venue.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Venue.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
