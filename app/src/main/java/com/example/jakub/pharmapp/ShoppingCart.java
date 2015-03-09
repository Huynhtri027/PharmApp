package com.example.jakub.pharmapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import com.example.jakub.pharmapp.drugsItems.drugInCart;
import com.example.jakub.pharmapp.drugsItems.drugInCartAdapter;

import java.util.ArrayList;


public class ShoppingCart extends Activity {

   private Button addButton;
   private LinearLayout ribbon;
   private Switch rswitch;
   private ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);



        addButton =(Button) findViewById(R.id.addButton) ;
        ribbon = (LinearLayout) findViewById(R.id.colorRibbon) ;
        rswitch = (Switch) findViewById(R.id.switch1) ;
        list = (ListView) findViewById(R.id.expandableListView) ;

        final ArrayList<drugInCart> listofd = new ArrayList<drugInCart>();
        listofd.add(new drugInCart("lek1",1,25.0f,25.0f,25.0f));

        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        list.setAdapter(adapterd);


        final Button btnAddMore = new Button(this);
        btnAddMore.setText("Dodaj");

        list.addFooterView(btnAddMore);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ribbon.setBackgroundColor(0xA404FF60);
            }
        });

        rswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ribbon.setBackgroundColor(0xA404FF60);
                }else{
                    ribbon.setBackgroundColor(0xa4ff2a18);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
