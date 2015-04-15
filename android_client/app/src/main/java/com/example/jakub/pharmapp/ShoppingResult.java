package com.example.jakub.pharmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jakub.pharmapp.drugsItems.drugInCart;
import com.example.jakub.pharmapp.drugsItems.drugInCartAdapter;

import java.util.ArrayList;


public class ShoppingResult extends Activity {

    static String finalprice;
    static boolean recept;
    static ArrayList<drugInCart> listofd;
    private ListView list;
    private TextView resultprice;
    private TextView resultrecipe;
    private Button newOrder;



    void showdrugs(){
        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        list.setAdapter(adapterd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_result);

        list = (ListView) findViewById(R.id.resultlistView) ;
        resultprice = (TextView) findViewById(R.id.resultprice) ;
        resultrecipe = (TextView) findViewById(R.id.resultrecipe) ;
        newOrder = (Button) findViewById(R.id.neworderbutton) ;

        resultprice.setText(finalprice);
        if(recept){
            resultrecipe.setText("Tak");
        }else{
            resultrecipe.setText("Nie");
        }


        showdrugs();

        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingCart.class);
                ShoppingCart.listofd.clear();
                startActivity(intent);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
