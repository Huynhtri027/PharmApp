package com.example.jakub.pharmapp.drugsItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jakub.pharmapp.R;
import com.example.jakub.pharmapp.drugsItems.drugInCart;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xnivel on 09/03/2015.
 */
public class drugInCartAdapter extends ArrayAdapter<drugInCart> {
    public drugInCartAdapter(Context context, ArrayList<drugInCart> drugs) {
        super(context, 0, drugs);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        drugInCart drug = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drugincart, parent, false);
        }
        // Lookup view for data population
        TextView dname = (TextView) convertView.findViewById(R.id.drugcartname);
        TextView dcount = (TextView) convertView.findViewById(R.id.drugcartcount);
        TextView dprice = (TextView) convertView.findViewById(R.id.drugcartprice);
        // Populate the data into the template view using the data object
        dname.setText(drug.name);
        dcount.setText(Integer.toString(drug.count));
        dprice.setText(new DecimalFormat("0.00").format(drug.priceMBCount)+"zł");
        // Return the completed view to render on screen
        return convertView;
    }
}
