package com.example.jakub.pharmapp.drugsItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jakub.pharmapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xnivel on 09/03/2015.
 */
public class drugInDialogAdapter extends ArrayAdapter<drugInCart> {
    public drugInDialogAdapter(Context context, ArrayList<drugInCart> drugs) {
        super(context, 0, drugs);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        drugInCart drug = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drugindialog, parent, false);
        }
        // Lookup view for data population
        TextView dname = (TextView) convertView.findViewById(R.id.drugdialogname);
        TextView dcount = (TextView) convertView.findViewById(R.id.drugdialogcount);
        // Populate the data into the template view using the data object
        dname.setText(drug.name);
        dcount.setText(Integer.toString(drug.count));
        // Return the completed view to render on screen
        return convertView;
    }
}
