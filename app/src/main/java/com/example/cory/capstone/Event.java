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
import android.widget.ArrayAdapter;
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

public class Event extends AppCompatActivity {

    //Key used to pass data to Detail Activity
    static final String EXTRA_EVENT_NAME = "com.example.cory.capstone.EXTRA_EVENT_NAME";

    private ListView lvContent;
    private List<RowItem> content;
    private CustomAdapter contentadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        lvContent = (ListView) findViewById(R.id.lv_event);
        content = new ArrayList<>();

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

    //Used for dynamic XML generation
    public class RowItem {
        private int id;
        private String name;
        private String address;
        private String date;
        private String description;

        //Constructor
        public RowItem(int id, String name, String address, String date, String description) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.date = date;
            this.description = description;
        }

        //Setters & Getters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
            View v = View.inflate(context, R.layout.event_row, null);
            TextView tvName = (TextView) v.findViewById(R.id.tv_eventname);
            TextView tvType = (TextView) v.findViewById(R.id.tv_eventaddress);
            TextView tvPrice = (TextView) v.findViewById(R.id.tv_eventdate);
            //Set text for TextView
            tvName.setText(rowItem.get(position).getName());
            tvType.setText(rowItem.get(position).getAddress());
            tvPrice.setText(rowItem.get(position).getDate());

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
                    String query = "SELECT colEventID, colEventName, colEventLocation, " +
                            "colEventTime, colEventDesc FROM tblEvent;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    content.clear();

                    if (rs.next()) {
                        while (rs.next()) {
                            content.add(new RowItem(rs.getInt("colEventID"),
                                    rs.getString("colEventName"),
                                    rs.getString("colEventLocation"),
                                    rs.getString("colEventTime"),
                                    rs.getString("colEventDesc")));
                        }
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
                //Connects the adapter to the List
                contentadapter = new CustomAdapter(getApplicationContext(), content);
                lvContent.setAdapter(contentadapter);

                //Passes tag (EventID) to the EventDetail activity
                lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getApplicationContext(), EventDetail.class);
                        intent.putExtra(EXTRA_EVENT_NAME, view.getTag().toString());

                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}