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
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


class AsyncStatistic extends AsyncTask<String, String, String>{

    private Statistics orgclass;
    private Calendar currentdate;
    private int add;
    private String url = "http://192.168.1.34:8888/statisticFrom/";

    public AsyncStatistic (Statistics orgclass,Calendar currentdate,int add){
        this.orgclass=orgclass;
        this.currentdate=currentdate;
        this.add=add;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 1000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 2500;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        orgclass.listofd.clear();
        try {
            String newurl;
            if((currentdate.get(Calendar.MONTH)+1)<10)
                newurl=url+'0'+(currentdate.get(Calendar.MONTH)+1)+"-"+currentdate.get(Calendar.YEAR);
            else
                newurl=url+(currentdate.get(Calendar.MONTH)+1)+"-"+currentdate.get(Calendar.YEAR);


            HttpResponse response = httpclient.execute(new HttpGet(newurl));
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
            return "1";
        } catch (JSONException e) {
            return "2";
        }
        return "";
    }
    @Override
    protected void onPostExecute(String result) {
        if(result.length()>0){
            orgclass.changeback(this.add);
        }

        orgclass.changedateonButtons();
        orgclass.refresharraylist();
    }
}

public class Statistics extends Activity {

    private Button cancelbut;
    private ListView list;
    private TextView textdate;
    private Button nextbut;
    private Button prevbut;
    private Calendar prevdate;
    private Calendar nextdate;
    private Calendar currentdate;
    private Statistics thisstat;

    public ArrayList<drugInCart> listofd = new ArrayList<drugInCart>();

    public void changeback(int i){
        currentdate.add(Calendar.MONTH,i);
        prevdate.add(Calendar.MONTH,i);
        nextdate.add(Calendar.MONTH,i);
        showerro();
    }

    public void refresharraylist(){
        nextbut.setEnabled(true);
        prevbut.setEnabled(true);
        if(nextdate.after(Calendar.getInstance())){
            nextbut.setEnabled(false);
        }
        drugInCartAdapter adapterd = new drugInCartAdapter(this,listofd);
        if(list!=null)
            list.setAdapter(adapterd);
    }
    public void showerro(){
        CharSequence text = "Problem z połączeniem";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
    public void changedateonButtons(){
        textdate.setText("(1-"+currentdate.getActualMaximum(Calendar.DAY_OF_MONTH)+")-"+(currentdate.get(Calendar.MONTH)+1)+"-"+currentdate.get(Calendar.YEAR));

        prevbut.setText((prevdate.get(Calendar.MONTH)+1)+"-"+prevdate.get(Calendar.YEAR));
        nextbut.setEnabled(true);

        nextbut.setText((nextdate.get(Calendar.MONTH)+1)+"-"+nextdate.get(Calendar.YEAR));
        if(nextdate.after(Calendar.getInstance())){
            nextbut.setEnabled(false);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        cancelbut = (Button) findViewById(R.id.button6);
        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingCart.class);
                startActivity(intent);
            }
        });
        list = (ListView) findViewById(R.id.listStatistic) ;

        textdate = (TextView) findViewById(R.id.currDateText) ;
        nextbut = (Button) findViewById(R.id.nextdatastatbut) ;
        prevbut = (Button) findViewById(R.id.prevdatestatbut) ;
        this.currentdate=Calendar.getInstance();
        this.prevdate=Calendar.getInstance();
        this.nextdate=Calendar.getInstance();
        nextdate.add(Calendar.MONTH,1);
        prevdate.add(Calendar.MONTH,-1);

        thisstat=this;

        changedateonButtons();
        new AsyncStatistic(thisstat,currentdate,0).execute("");

        Log.w("stat",nextdate.toString());
        Log.w("stat",prevdate.toString());

        nextbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentdate.add(Calendar.MONTH,1);
                prevdate.add(Calendar.MONTH,1);
                nextdate.add(Calendar.MONTH,1);
                new AsyncStatistic(thisstat,currentdate,-1).execute("");
                nextbut.setEnabled(false);
                prevbut.setEnabled(false);
            }
        });
        prevbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentdate.add(Calendar.MONTH,-1);
                prevdate.add(Calendar.MONTH,-1);
                nextdate.add(Calendar.MONTH,-1);
                new AsyncStatistic(thisstat,currentdate,1).execute("");
                nextbut.setEnabled(false);
                prevbut.setEnabled(false);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
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
