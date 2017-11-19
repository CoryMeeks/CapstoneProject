package com.example.cory.capstone;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.cory.capstone.R.id.lv_venue;

/**
 * Created by Cory on 11/18/2017.
 */
/*
public class CustomAdapter extends ArrayAdapter<String>
{
    private Context context;
    private List<String> strings;

    public void ListViewAdapter(Context context, List<String> strings)
    {
        super(context, R.layout.activity_venue, lv_venue);
        this.context = context;
        this.strings = new ArrayList<String>();
        this.strings = strings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView your_first_text_view = (TextView) rowView.findViewById(R.id.tv_venuename);
        TextView your_second_text_view = (TextView) rowView.findViewById(R.id.tv_description);

        your_first_text_view.setText(strings.get(position));
        your_second_text_view.setText(strings.get(position)); //Instead of the same value use position + 1, or something appropriate

        return rowView;
    }
}
}
*/