package com.example.cory.capstone;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.cory.capstone.R.id.lv_venue;

/**
 * Created by Cory on 11/18/2017.
 */

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
        View v = View.inflate(context, R.layout.row, null);
        TextView tvName = (TextView) v.findViewById(R.id.tv_venuename);
        TextView tvAddress = (TextView) v.findViewById(R.id.tv_address);
        //Set text for TextView
        tvName.setText(rowItem.get(position).getName());
        tvAddress.setText(rowItem.get(position).getAddress());

        //Set Tag for TextView
        v.setTag(rowItem.get(position).getId());

        return v;
    }
}