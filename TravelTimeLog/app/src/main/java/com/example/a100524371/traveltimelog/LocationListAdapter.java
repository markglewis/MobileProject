package com.example.a100524371.traveltimelog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
//adapter class for listview
/**
 * Created by 100524371 on 11/27/2016.
 */

public class LocationListAdapter extends ArrayAdapter<Location> {
    public LocationListAdapter(Context context, ArrayList<Location> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Location location = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        //what the listview will display
        TextView label = (TextView) convertView.findViewById(android.R.id.text1);label.setText("Times Visited: " +location.getTimesVisited()+ ",  " +location.getName() + ", " + location.getAddress1() );
        return convertView;

    }
}