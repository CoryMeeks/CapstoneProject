package com.example.cory.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.cory.capstone.Login.EXTRA_USER_ID;

public class Profile extends AppCompatActivity {

    private ListView lvContent;
    private List<RowItem> content;
    private CustomAdapter contentadapter;
    private int value;
    private String msg;

    String colUserName;
    String colRealName;
    String colUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            value = extras.getInt(EXTRA_USER_ID);
        } else msg = "Nothing passed";

        lvContent = (ListView) findViewById(R.id.lv_profile);
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
                    String query = "SELECT colUserName, colUserFirstName, colUserLastName, " +
                            "colUserEmail, colNewsSubject FROM tblUser JOIN tblNews ON " +
                            "(tblNews.colUserID = tblUser.colUserID) WHERE tblNews.colUserID = " +
                            "tblUser.colUserID AND tblUser.colUserID = "+value;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    content.clear();

                    if (rs.next()) {
                        colUserName = rs.getString("colUserName");
                        colRealName = rs.getString("colUserFirstName")+" "+rs.getString("colUserLastName");
                        colUserEmail = rs.getString("colUserEmail");

                        while (rs.next()) {
                            content.add(new RowItem(rs.getString("colNewsSubject")));
                        }
                        //msg = "Something to display";
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
                TextView tvUserName = (TextView) findViewById(R.id.tv_username);
                TextView tvRealName = (TextView) findViewById(R.id.tv_realname);
                TextView tvUserEmail = (TextView) findViewById(R.id.tv_useremail);

                tvUserName.setText(colUserName);
                tvRealName.setText(colRealName);
                tvUserEmail.setText(colUserEmail);

                contentadapter = new CustomAdapter(getApplicationContext(), content);
                lvContent.setAdapter(contentadapter);

                lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getApplicationContext(), NewsDetail.class);
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
            View v = View.inflate(context, R.layout.profile_row, null);
            TextView tvSubject = (TextView) v.findViewById(R.id.tv_commentsubject);
            //Set text for TextView
            tvSubject.setText(rowItem.get(position).getSubject());

            return v;
        }
    }

    public class RowItem {
        private String subject;

        //Constructor

        public RowItem(String subject) {
            this.subject = subject;
        }

        //Setters & Getters
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    public void setNewVenue (View v) {
        Intent i = new Intent(getApplicationContext(), NewVenue.class);
        startActivity(i);
    }

    public void setNewTalent (View v) {
        Intent i = new Intent(getApplicationContext(), NewTalent.class);
        startActivity(i);
    }
}
