package com.example.megha.todoapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ToDoListAdapter extends ArrayAdapter<ToDoListContents> {

    Context context;
    ArrayList<ToDoListContents> objects;
    public ToDoListAdapter(Context context, ArrayList<ToDoListContents> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.to_do_list_layout , null);
        }
        TextView titleView = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateTextView);
        titleView.setText(objects.get(position).title);
        dateView.setText(objects.get(position).date);
        return convertView;
    }
}
