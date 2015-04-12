package com.example.jakub.pharmapp.drugsItems;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xnivel on 09/03/2015.
 */
public class drugInCart implements Comparable {
    public String name;
    public int count;
    public float actualprice;
    public float price;
    public float priceWithReduction;
    public float priceMBCount;

    @Override
    public int hashCode() {
     return name.hashCode();
    }


    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof drugInCart))return false;
        drugInCart otherMyClass = (drugInCart)other;
        if(otherMyClass.name.hashCode()==this.name.hashCode()) return true;
        return false;
    }


    public JSONObject toJson(){
        JSONObject json = new JSONObject();

        try {
            json.put("name",this.name);
            json.put("count",this.count);
            json.put("price",this.priceMBCount/this.count);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public drugInCart(JSONObject jobj) {
        try {
            this.name = jobj.getString("name");
            this.count = jobj.getInt("count");
            this.price =  (float)jobj.getDouble("price");
            if(jobj.has("priceWR")){
                this.priceWithReduction = (float)jobj.getDouble("priceWR");
            }else{
                this.priceWithReduction = this.price;
            }


            this.actualprice = this.price;
            this.priceMBCount = this.price;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public drugInCart(String asd, int i, float v) {
        this.name = asd;
        this.count = i;
        this.price = v;
        this.priceWithReduction = v;
        this.priceMBCount = this.price*this.count;
        this.actualprice = this.price;
    }

    public drugInCart(String name, int count, float actualprice, float price, float priceWithReduction, float priceMBCount) {
        this.name = name;
        this.count = count;
        this.actualprice = actualprice;
        this.price = price;
        this.priceWithReduction = priceWithReduction;
        this.priceMBCount = priceMBCount;
    }

    public float getActualprice() {
        return actualprice;
    }

    public void setActualprice(float actualprice) {
        this.actualprice = actualprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceWithReduction() {
        return priceWithReduction;
    }

    public void setPriceWithReduction(float priceWithReduction) {
        this.priceWithReduction = priceWithReduction;
    }

    public float getPriceMBCount() {
        return priceMBCount;
    }

    public void setPriceMBCount(float priceMBCount) {
        this.priceMBCount = priceMBCount;
    }

    @Override
    public int compareTo(Object another) {
        drugInCart otherMyClass = (drugInCart)another;


        return name.compareTo(otherMyClass.name);
    }
}
