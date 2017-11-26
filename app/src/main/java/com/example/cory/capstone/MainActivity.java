package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

public class MainActivity extends AppCompatActivity {

    //Need to code in social media features to use this function
    //static final String EXTRA_MAIN_NAME = "com.example.cory.capstone.EXTRA_MAIN_NAME";

    private ListView lvContent;
    private List<RowItem> content;
    private CustomAdapter contentadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets ListView to the MainActivity ListView and instantiates a new ArrayList
        lvContent = (ListView) findViewById(R.id.lv_main);
        content = new ArrayList<>();

        //Runs Async database request to populate XML
        GetData pullcontent = new GetData();
        pullcontent.execute();
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

    //Moves the user through the activities
    public void onClickShowProfile(View v) {
        Intent i = new Intent(getApplicationContext(), Profile.class);
        startActivity(i);
    }

    public void onClickOpenEvents(View v) {
        Intent i = new Intent(getApplicationContext(), Event.class);
        startActivity(i);
    }

    public void onClickOpenVenues(View v) {
        Intent i = new Intent(getApplicationContext(), Venue.class);
        startActivity(i);
    }

    public void onClickOpenTalent(View v) {
        Intent i = new Intent(getApplicationContext(), Talent.class);
        startActivity(i);
    }

    //Used for dynamic XML generation
    public class RowItem {
        private int id;
        private String username;
        private String content;
        private String timestamp;

        //Constructor
        public RowItem(int id, String username, String content, String timestamp) {
            this.id = id;
            this.username = username;
            this.content = content;
            this.timestamp = timestamp;
        }

        //Setters & Getters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public class CustomAdapter extends BaseAdapter {
        private Context context;
        private List<RowItem> rowItem;

        //Constructor
        public CustomAdapter(Context context, List<RowItem> rowItem) {
            this.context = context;
            this.rowItem = rowItem;
        }

        @Override
        public int getCount() {
            return rowItem.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //Inflates the target layout for dynamic XML and passes to variables
            View v = View.inflate(context, R.layout.main_row, null);
            TextView tvName = (TextView) v.findViewById(R.id.tv_username);
            TextView tvContent = (TextView) v.findViewById(R.id.tv_newscontent);
            TextView tvTimeStamp = (TextView) v.findViewById(R.id.tv_timestamp);
            //Set text for TextView
            tvName.setText(rowItem.get(position).getUsername());
            tvContent.setText(rowItem.get(position).getContent());
            tvTimeStamp.setText(rowItem.get(position).getTimestamp());

            //Set Tag for TextView
            v.setTag(rowItem.get(position).getId());

            return v;
        }
    }

    //Async process to pull data from the database, and put into List
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
                    //Pulls data from both the News and User tables for the most recent posts
                    String query = "SELECT colNewsID, colNewsSubject, colNewsContent, " +
                            "colNewsTimeStamp, tblNews.colUserID, colUserName FROM dbo.tblNews " +
                            "JOIN dbo.tblUser ON (tblNews.colUserID = tblUser.colUserID) " +
                            "WHERE tblNews.colUserID = tblUser.colUserID;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    content.clear();

                    //If there's content being pulled from the database
                    if (rs.next()) {
                        //Loop through the content and pass into variables
                        while (rs.next()) {
                            try {
                                content.add(new RowItem(rs.getInt("colNewsID"),
                                        rs.getString("colUserName"),
                                        rs.getString("colNewsContent"),
                                        rs.getString("colNewsTimeStamp")));
                            } catch (Exception e) {
                                Log.e("E-ERR", e.getMessage());
                            }
                        }
                        isSuccess = true;
                    } else {
                        msg = "Nothing to display";
                    }
                    stmt.close();
                    con.close();
                }
            } catch (SQLException se) {
                isSuccess = false;
                Log.e("SE-ERR", se.getMessage());
            } catch (Exception e) {
                isSuccess = false;
                Log.e("E-ERR", e.getMessage());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {
                //connect the adapter to the List
                contentadapter = new CustomAdapter(getApplicationContext(), content);
                lvContent.setAdapter(contentadapter);

                /*
                 * This is used for the social media functionality, which will not be finished
                 * in time for the final project. This can be added at a later date, and is
                 * used to take the user into the forum section of the app.

                lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getApplicationContext(), NewsDetail.class);
                        intent.putExtra(EXTRA_MAIN_NAME, view.getTag().toString());

                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                */
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

}