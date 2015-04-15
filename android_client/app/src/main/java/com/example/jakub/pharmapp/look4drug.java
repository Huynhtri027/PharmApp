package com.example.jakub.pharmapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakub.pharmapp.drugsItems.drugInCart;
import com.example.jakub.pharmapp.drugsItems.drugInCartAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


class Asyncsearch extends AsyncTask<String, String, String>{

    public look4drug orgclass;
    public Context context;

    private String url = "http://192.168.1.34:8888/search/";

    public Asyncsearch(look4drug orgclass,Context context){
        this.orgclass=orgclass;
        this.context=context;
    }
    @Override
    protected void onPostExecute(String result) {
        if(result.length()>0){
            orgclass.showerro();
        }

        orgclass.refresharraylist();
    }


    @Override
    protected String doInBackground(String... searchtext) {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 1000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 2500;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        orgclass.listofd.clear();
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        try {
            HttpResponse response = httpclient.execute(new HttpGet(url+searchtext[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                JSONArray arr = new JSONArray(responseString);
                for(int i = 0; i < arr.length(); i++){
                    orgclass.listofd.add(new drugInCart(arr.getJSONObject(i)));
                }
                //..more logic
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
/*            CharSequence text = "Problem z połączeniem";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/
//            e.printStackTrace();
            return "1";
        } catch (JSONException e) {
//            e.printStackTrace();
/*            CharSequence text = "Problem z połączeniem";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/
            return "2";
        }


//        listofd.add(new drugInCart("Lek2",1,25.0f,25.0f,10.0f,25.0f));
        return "";
    }

}

public class look4drug extends Activity {

    private ListView list;
    private EditText searchedittext;
    private Button cancelbut;
    private Button lookforbut;
    private TextView resultrecipe;
    private look4drug varlook4drug;

    public void showerro(){
        lookforbut.setEnabled(true);
        CharSequence text = "Problem z połączeniem";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }


    public ArrayList<drugInCart> listofd = new ArrayList<drugInCart>();

    private void makereduction(){
        for(drugInCart d : listofd)
        {
            d.setActualprice(d.getPriceWithReduction());
            d.setPriceMBCount(d.getActualprice());
        }
    }
    private void unmakereduction(){
        for(drugInCart d : listofd)
        {
            d.setActualprice(d.getPrice());
            d.setPriceMBCount(d.getActualprice());
        }
    }

    public void refresharraylist(){
        lookforbut.setEnabled(true);
        listofd.removeAll(ShoppingCart.listofd);

        if(ShoppingCart.discount){
            makereduction();
        }else{
            unmakereduction();
        }

        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        if(list!=null)
        list.setAdapter(adapterd);


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
                listofd.get(position).setCount(1);
                ShoppingCart.listofd.add(listofd.get(position));

                Intent intent = new Intent(view.getContext(), ShoppingCart.class);

                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look4drug);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        list = (ListView) findViewById(R.id.searchresultlist) ;
        searchedittext = (EditText) findViewById(R.id.searchtext);
        lookforbut = (Button) findViewById(R.id.lookforbut);
        cancelbut = (Button) findViewById(R.id.button5);
        resultrecipe = (TextView) findViewById(R.id.ifdiscount) ;

        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingCart.class);
                startActivity(intent);
            }
        });

        if(ShoppingCart.discount){
            resultrecipe.setText("Tak");
        }else{
            resultrecipe.setText("Nie");
        }

        varlook4drug = this;
        new Asyncsearch(this,getApplicationContext()).execute("");
        lookforbut.setEnabled(true);

        lookforbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookforbut.setEnabled(false);
                new Asyncsearch(varlook4drug,getApplicationContext()).execute(searchedittext.getText().toString());
                //searchfunc(searchedittext.getText().toString());
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
