package com.example.jakub.pharmapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jakub.pharmapp.drugsItems.drugInCart;
import com.example.jakub.pharmapp.drugsItems.drugInCartAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class look4drug extends Activity {

    private ListView list;

    public ArrayList<drugInCart> listofd = new ArrayList<drugInCart>();


    private void refresharraylist(){
        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        if(list!=null)
        list.setAdapter(adapterd);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look4drug);
        list = (ListView) findViewById(R.id.searchresultlist) ;

        listofd.add(new drugInCart("Lek2",1,25.0f,25.0f,10.0f,25.0f));

        refresharraylist();

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                drugInCart a = listofd.get(position);
//                Context context = getApplicationContext();
//                CharSequence text = "Hello toast! "+a.getName();
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
                ShoppingCart.listofd.add(listofd.get(position));

                Intent intent = new Intent(view.getContext(), ShoppingCart.class);

                startActivity(intent);
                return false;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_look4drug, menu);
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
