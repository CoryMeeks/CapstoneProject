package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.example.cory.capstone.Venue.EXTRA_VENUE_NAME;

public class VenueDetailMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String strvenuedetailaddress;
    private String value;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fr_venuegooglemaps);
        mapFragment.getMapAsync(this);

        //Checks whether intent data got properly sent over
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            value = extras.getString(EXTRA_VENUE_NAME);

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
        String strvenuedetailname;
        String strvenuedetailaddress;
        String strvenuedetaildesc;
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
                    String query = "SELECT colVenueName, colVenueLocation, colVenueDesc FROM tblVenue WHERE colVenueID = '"+value+"';";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        //do something
                        strvenuedetailname = rs.getString("colVenueName");
                        strvenuedetailaddress = rs.getString("colVenueLocation");
                        strvenuedetaildesc = rs.getString("colVenueDesc");

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
                TextView venuedetailname = (TextView) findViewById(R.id.tv_venuedetailname);
                TextView venuedetailaddress = (TextView) findViewById(R.id.tv_venuedetailaddress);
                TextView venuedetaildesc = (TextView) findViewById(R.id.tv_venuedetaildesc);

                venuedetailname.setText(strvenuedetailname);
                venuedetailaddress.setText(strvenuedetailaddress);
                venuedetaildesc.setText(strvenuedetaildesc);

                onSearch(strvenuedetailaddress);
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
    }

    public void onSearch (String location) {
        mMap.clear();

        List<Address> addressList = null; //Instantiates an array to store address data

        if (location != null || !location.equals("")) { //Tests whether List is populated
            Geocoder geocoder = new Geocoder(this); //Instantiates object Geocoder
            try {
                addressList = geocoder.getFromLocationName(location, 1); //Converts address name to latitude, longitude data
            } catch (IOException e) {
                e.printStackTrace(); //else export stacktrace
            }
            android.location.Address address = addressList.get(0); //Retrieves new lat long data from List into usable variable
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude()); //Instantiates LatLng object with location lat long data
            mMap.addMarker(new MarkerOptions().position(latLng).title(location)); //Drops a pin on the map at given lat long
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)); //Moves 'camera' to new lat long location

        }
    }
}
