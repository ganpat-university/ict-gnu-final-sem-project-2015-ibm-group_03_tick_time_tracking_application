package com.example.abhi.tick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<employee> {

    //the hero list that will be displayed
    private List<employee> employeeList;

    //the context object
    private Context mCtx;

    //here we are getting the herolist and context
    //so while creating the object of this adapter class we need to give herolist and context
    public ListViewAdapter(List<employee> heroList, Context mCtx) {
        super(mCtx, R.layout.list_items, heroList);
        this.employeeList = heroList;
        this.mCtx = mCtx;
    }

    //this method will return the list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.list_items, null, true);

        //getting text views
        TextView location = listViewItem.findViewById(R.id.text_location);
        TextView work = listViewItem.findViewById(R.id.text_work);
        TextView time = listViewItem.findViewById(R.id.text_time);

        //Getting the hero for the specified position
        employee hero = employeeList.get(position);

        //setting hero values to textviews
        location.setText(hero.getLocation());
        work.setText(hero.getWork());
        time.setText(hero.getTime());

        //returning the listitem
        return listViewItem;
    }
}