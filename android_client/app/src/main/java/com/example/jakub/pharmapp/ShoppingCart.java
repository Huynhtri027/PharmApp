package com.example.jakub.pharmapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakub.pharmapp.drugsItems.drugInCart;
import com.example.jakub.pharmapp.drugsItems.drugInCartAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class ShoppingCart extends Activity {

   private Button endButton;
   private LinearLayout ribbon;
   private Switch rswitch;
   private ListView list;
   private ImageButton removeallb;
   private TextView finalprice;
   private String posturl="http://192.168.1.34:8888/test";

   public static ArrayList<drugInCart> listofd = new ArrayList<drugInCart>();
   public static boolean discount = false;

    private void refresharraylist(){

        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        list.setAdapter(adapterd);

        float tmp=0.0f;
        for(drugInCart d : listofd)
        {
            tmp+=d.getPriceMBCount();
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

    private StringEntity JsonSE(){
        JSONArray ja = new JSONArray();
        for(drugInCart dic:listofd){
            ja.put(dic.toJson());
        }
        StringEntity se = null;
        try {
            se = new StringEntity(ja.toString());
            Log.w("se", ja.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return se;
    }

    private String response2String(HttpResponse responseHandler){

        String responseString=null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            responseHandler.getEntity().writeTo(out);
            responseString = out.toString();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    private String getResultString(){
        StringEntity se = JsonSE();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(posturl);
        httpost.setEntity(se);
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        HttpResponse responseHandler =null;
        try {
            responseHandler=httpclient.execute(httpost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Context context = getApplicationContext();
        CharSequence text = "Hello toast! "+responseHandler.getStatusLine().getStatusCode();;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        return response2String(responseHandler);


    }

    private void showPositivResult(){
        Intent intent = new Intent(getApplicationContext(), ShoppingResult.class);
        ShoppingResult.finalprice = finalprice.getText().toString();
        ShoppingResult.recept = rswitch.isChecked();
        ShoppingResult.listofd = listofd;

        startActivity(intent);
    }
    private void showNegativResult(String res) throws JSONException {

        ArrayList<drugInCart> neglistres = new ArrayList<drugInCart>();

        JSONArray arr = null;
        arr = new JSONArray(res);
        for(int i = 0; i < arr.length(); i++){
            neglistres.add(new drugInCart(arr.getJSONObject(i)));
        }

        WrongShoppingResult.listofd = neglistres;
        Dialog test=new WrongShoppingResult(this);
        test.show();
    }

    private void closeShoppingCart(){


        if(listofd.size()>0){

            String responseString="";
            responseString=getResultString();

            try {
                if(!(responseString.length()>2)){
                    //positv
                    showPositivResult();
                }else{
                    //negativ
                    showNegativResult(responseString);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);



        endButton =(Button) findViewById(R.id.endButton) ;
        ribbon = (LinearLayout) findViewById(R.id.colorRibbon) ;
        rswitch = (Switch) findViewById(R.id.switch1) ;
        list = (ListView) findViewById(R.id.expandableListView) ;
        finalprice = (TextView) findViewById(R.id.finalprice);

        if(listofd.size()>0){
            endButton.setEnabled(true);
        }else{
            endButton.setEnabled(false);
        }



        try {
            Drawable drawnableicon = Drawable.createFromStream(getAssets().open("kosz.png"), null);
            removeallb = (ImageButton) findViewById(R.id.imageButton) ;
            removeallb.setBackground(drawnableicon);
        } catch (IOException e) {
            e.printStackTrace();
        }




        refresharraylist();

        final Button btnAddMore = new Button(this);
        btnAddMore.setText("Dodaj");

        list.addFooterView(btnAddMore);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), look4drug.class);

                startActivity(intent);
            }
        });

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

                np.setMaxValue(99);
                np.setMinValue(1);
                np.setValue(selecteddrug.getCount());
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



        rswitch.setChecked(discount);
        if(discount){
            ribbon.setBackgroundColor(0xA404FF60);
            makereduction();
            refresharraylist();
        }else{
            ribbon.setBackgroundColor(0xa4ff2a18);
            unmakereduction();
            refresharraylist();
        }
        rswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    discount=true;
                    ribbon.setBackgroundColor(0xA404FF60);
                    makereduction();
                    refresharraylist();
                }else{
                    discount=false;
                    ribbon.setBackgroundColor(0xa4ff2a18);
                    unmakereduction();
                    refresharraylist();
                }
            }
        });



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        removeallb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                listofd.clear();
                                refresharraylist();
                                endButton.setEnabled(false);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                break;
                        }
                    }
                };


                builder.setMessage("Czy chcesz usunąć wszystkie pola?").setPositiveButton("Tak", dialogClickListener)
                        .setNegativeButton("Nie", dialogClickListener).show();

            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View vi = v;

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                /*Intent intent = new Intent(vi.getContext(), ShoppingResult.class);
                                ShoppingResult.finalprice = finalprice.getText().toString();
                                ShoppingResult.recept = rswitch.isChecked();
                                ShoppingResult.listofd = listofd;

                                startActivity(intent);*/

                                closeShoppingCart();

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
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

