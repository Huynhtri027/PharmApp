package com.example.jakub.pharmapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jakub.pharmapp.drugsItems.drugInCart;
import com.example.jakub.pharmapp.drugsItems.drugInCartAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ShoppingCart extends Activity {

   private Button addButton;
   private LinearLayout ribbon;
   private Switch rswitch;
   private ListView list;

   public final ArrayList<drugInCart> listofd = new ArrayList<drugInCart>();

    private void refresharraylist(){
        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        list.setAdapter(adapterd);
        TextView finalprice = (TextView) findViewById(R.id.finalprice);
        float tmp=0.0f;
        for(drugInCart d : listofd)
        {
            tmp=d.getPriceMBCount();
        }
        finalprice.setText(new DecimalFormat("0.00").format(tmp));
    }

    private void makereduction(){
        for(drugInCart d : listofd)
        {
            d.setActualprice(d.getPriceWithReduction());
            d.setPriceMBCount(d.getActualprice()*d.getCount());
        }
    }
    private void unmakereduction(){
        for(drugInCart d : listofd)
        {
            d.setActualprice(d.getPrice());
            d.setPriceMBCount(d.getActualprice()*d.getCount());
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);



        addButton =(Button) findViewById(R.id.addButton) ;
        ribbon = (LinearLayout) findViewById(R.id.colorRibbon) ;
        rswitch = (Switch) findViewById(R.id.switch1) ;
        list = (ListView) findViewById(R.id.expandableListView) ;

        listofd.add(new drugInCart("Lek1",1,25.0f,25.0f,10.0f,25.0f));

        refresharraylist();

        final Button btnAddMore = new Button(this);
        btnAddMore.setText("Dodaj");

        list.addFooterView(btnAddMore);

        final Dialog dialog = new Dialog(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final drugInCart selecteddrug = listofd.get(position);
                dialog.setContentView(R.layout.dialogdrugcart);
                dialog.setTitle(selecteddrug.getName());
                final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
                TextView priceperd = (TextView) dialog.findViewById(R.id.pricepd);
                TextView pricedm =  (TextView) dialog.findViewById(R.id.priceM);


                // set the custom dialog components - text, image and button
                priceperd.setText(new DecimalFormat("0.00").format(selecteddrug.getActualprice()));
                pricedm.setText(new DecimalFormat("0.00").format(selecteddrug.getCount()*selecteddrug.getActualprice()));
                np.setValue(selecteddrug.getCount());
                np.setMaxValue(99);
                np.setMinValue(1);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        TextView price = (TextView) dialog.findViewById(R.id.pricepd);
                        TextView priceMP = (TextView) dialog.findViewById(R.id.priceM);
                        float fprice=Float.parseFloat(price.getText().toString());
                        float fpriceMP=fprice*newVal;
                        priceMP.setText(new DecimalFormat("0.00").format(fpriceMP));
                    }
                });

                Button changevalues=(Button) dialog.findViewById(R.id.changebuttondialog);
                Button removeone=(Button) dialog.findViewById(R.id.button2);
                Button changevaluesbutton=(Button) dialog.findViewById(R.id.button3);

                changevalues.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selecteddrug.setCount(np.getValue());
                        selecteddrug.setPriceMBCount(np.getValue()*selecteddrug.getPrice());
                        refresharraylist();
                        dialog.dismiss();
                    }
                });

                removeone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listofd.remove(position);
                        refresharraylist();
                        dialog.dismiss();
                    }
                });

                changevaluesbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });




        rswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ribbon.setBackgroundColor(0xA404FF60);
                    makereduction();
                    refresharraylist();
                }else{
                    ribbon.setBackgroundColor(0xa4ff2a18);
                    unmakereduction();
                    refresharraylist();
                }
            }
        });



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };


                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



                //testy


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

