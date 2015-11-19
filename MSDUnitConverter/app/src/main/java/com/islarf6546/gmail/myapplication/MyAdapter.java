package com.islarf6546.gmail.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YamiVegeta on 07/11/2015.
 */
public class MyAdapter extends ArrayAdapter<String> {
    public MyAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.categories_layout, values);
    }

    public View getView(int position, View convert, ViewGroup parent)  {
        View theView = convert;
        LayoutInflater theInflater = LayoutInflater.from(getContext());
        theView = theInflater.inflate(R.layout.categories_layout,
                parent, false);

        String content = getItem(position);
        TextView theTextView = (TextView) theView.findViewById(R.id.textView1);
        theTextView.setText(content);

        return theView;
    }
}
