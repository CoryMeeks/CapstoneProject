package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.cory.capstone.Talent.EXTRA_TALENT_NAME;
import static com.example.cory.capstone.Venue.EXTRA_VENUE_NAME;

public class TalentDetail extends AppCompatActivity {

    String value;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_detail);

        //Checks whether intent data got properly sent over
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            value = extras.getString(EXTRA_TALENT_NAME);
        } else msg = "Nothing passed";

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

    public class GetData extends AsyncTask<String, String, String> {
        String strtalentdetailname;
        String strtalentdetailprice;
        String strtalentdetaildesc;
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
                    String query = "SELECT colTalentName, colTalentAskingRate, colTalentDesc FROM tblTalent WHERE colTalentID = '"+value+"';";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        //do something
                        strtalentdetailname = rs.getString("colTalentName");
                        strtalentdetailprice = rs.getString("colTalentAskingRate");
                        strtalentdetaildesc = rs.getString("colTalentDesc");

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
                TextView talentdetailname = (TextView) findViewById(R.id.tv_talentdetailname);
                TextView talentdetailprice = (TextView) findViewById(R.id.tv_talentdetailprice);
                TextView talentdetaildesc = (TextView) findViewById(R.id.tv_talentdetaildesc);

                talentdetailname.setText(strtalentdetailname);
                talentdetailprice.setText("$"+strtalentdetailprice);
                talentdetaildesc.setText(strtalentdetaildesc);
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
