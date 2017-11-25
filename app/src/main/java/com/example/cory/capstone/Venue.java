package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Venue extends AppCompatActivity {

    static final String EXTRA_VENUE_NAME = "com.example.cory.capstone.EXTRA_VENUE_NAME";

    private ListView lvContent;
    private List<RowItem> content;
    private CustomAdapter contentadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        lvContent = (ListView) findViewById(R.id.lv_venue);
        content = new ArrayList<>();

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

    public class GetData extends AsyncTask<String, String, String> {
        String msg = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass();
                if (con == null) {
                    msg = "Check your Internet connection";
                } else {
                    String query = "SELECT colVenueID, colVenueName, colVenueLocation, colVenueDesc FROM tblVenue;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    content.clear();

                    if (rs.next()) {
                        while (rs.next()) {
                            content.add(new RowItem(rs.getInt("colVenueID"), rs.getString("colVenueName"), rs.getString("colVenueLocation"), rs.getString("colVenueDesc")));
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

            return msg;
        }

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {
                //do something
                contentadapter = new CustomAdapter(getApplicationContext(), content);
                lvContent.setAdapter(contentadapter);

                lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getApplicationContext(), VenueDetailMap.class);
                        intent.putExtra(EXTRA_VENUE_NAME, view.getTag().toString());

                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                //Toast.makeText(Venue.this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Venue.this, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
