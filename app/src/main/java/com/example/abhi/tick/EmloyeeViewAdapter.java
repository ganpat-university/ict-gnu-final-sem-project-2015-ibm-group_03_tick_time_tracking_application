package com.example.abhi.tick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EmloyeeViewAdapter extends ArrayAdapter<EmployeeList> {

    private List<EmployeeList> employeeList;

    //the context object
    private Context mCtx;

    //here we are getting the herolist and context
    //so while creating the object of this adapter class we need to give herolist and context
    public EmloyeeViewAdapter(List<EmployeeList> heroList, Context mCtx) {
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
        View listViewItem = inflater.inflate(R.layout.list_admin, null, true);

        //getting text views
        TextView name = listViewItem.findViewById(R.id.text_location);
        TextView email = listViewItem.findViewById(R.id.text_work);

        //Getting the hero for the specified position
        EmployeeList hero = employeeList.get(position);

        //setting hero values to textviews
        name.setText(hero.getName());
        email.setText(hero.getEmail());

        //returning the listitem
        return listViewItem;
    }
}
